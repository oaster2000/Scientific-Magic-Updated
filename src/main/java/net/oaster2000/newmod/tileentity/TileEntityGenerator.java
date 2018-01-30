package net.oaster2000.newmod.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.oaster2000.newmod.blocks.GeneratorBlock;
import net.oaster2000.newmod.container.ContainerGenerator;
import net.oaster2000.newmod.energy.EnergyStorageMod;
import net.oaster2000.newmod.items.ModItems;
import net.oaster2000.newmod.slot.SlotGeneratorFuel;

public class TileEntityGenerator extends TileEntityEnergyDevice implements ITickable, ISidedInventory
{	
    private static final int[] SLOTS_BOTTOM = new int[] {1};
    /** The ItemStacks that hold the items currently being used in the generator */
    private NonNullList<ItemStack> generatorItemStacks = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    /** The number of ticks that the generator will keep burning */
    private int generatorBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the generator burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String generatorCustomName;
    
    public EnergyStorageMod storage = new EnergyStorageMod(1000);

	/**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.generatorItemStacks.size();
    }
    
	public boolean isEmpty()
    {
        for (ItemStack itemstack : this.generatorItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Nullable
    public ItemStack getStackInSlot(int index)
    {
        return this.generatorItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.generatorItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.generatorItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        boolean flag = !stack.isEmpty() && stack.isItemEqual(this.generatorItemStacks.get(index)) && ItemStack.areItemStackTagsEqual(stack, this.generatorItemStacks.get(index));
        this.generatorItemStacks.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.generatorCustomName : "container.generator";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.generatorCustomName != null && !this.generatorCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.generatorCustomName = p_145951_1_;
    }

    public static void registerFixesGenerator(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityGenerator.class, new String[] {"Items"}));
    }
    
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.generatorItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.generatorItemStacks);
        this.generatorBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.generatorItemStacks.get(0));
		storage.setEnergy(compound.getInteger("Energy"));

        if (compound.hasKey("CustomName", 8))
        {
            this.generatorCustomName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short)this.generatorBurnTime);
        compound.setInteger("CookTime", (short)this.cookTime);
        compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
		compound.setInteger("Energy", storage.getEnergyStored());
        ItemStackHelper.saveAllItems(compound, this.generatorItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.generatorCustomName);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Generator isBurning
     */
    public boolean isBurning()
    {
        return this.generatorBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
		boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning())
        {
            --this.generatorBurnTime;
        }

        if (!this.world.isRemote)
        {
        	if (this.isBurning() || !this.generatorItemStacks.get(0).isEmpty())
            {
                if (!this.isBurning() && this.canGen())
                {
                    this.generatorBurnTime = getItemBurnTime(this.generatorItemStacks.get(0));
                    this.currentItemBurnTime = this.generatorBurnTime;

                    if (this.isBurning())
                    {
                        flag1 = true;

                        if (!this.generatorItemStacks.get(0).isEmpty())
                        {
                            this.generatorItemStacks.get(0).shrink(1);

                            if (this.generatorItemStacks.get(0).getCount() == 0)
                            {
                                this.generatorItemStacks.set(0, generatorItemStacks.get(0).getItem().getContainerItem(generatorItemStacks.get(0)));
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canGen())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.generatorItemStacks.get(0));
                        flag1 = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0)
            {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }
            
            if (flag != this.isBurning())
            {
                flag1 = true;
                GeneratorBlock.setState(this.isBurning(), this.world, this.pos);
            }
            
            if(this.isBurning()){
                storage.receiveEnergy(1, false);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public int getCookTime(@Nullable ItemStack stack)
    {
        return 200;
    }

    /**
     * Returns true if the generator can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canGen()
    {
       return true;
    }


    /**
     * Returns the number of ticks that the supplied fuel item will keep the generator burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return 0;
        }
        else
        {
            Item item = stack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR)
            {
                Block block = Block.getBlockFromItem(item);
                
            }

            if (item == ModItems.battery) return 3200; //A battery SHOULD give 3200 unit of electricity
            if (item == Items.COAL) return 200; //A Piece of coal SHOULD give 200 units of electricity
            return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(stack);
        }
    }

    public static boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack) > 0;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
            ItemStack itemstack = this.generatorItemStacks.get(0);
            return isItemFuel(stack) || SlotGeneratorFuel.isBucket(stack) && (itemstack.isEmpty() || itemstack.getItem() != Items.BUCKET);
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? null : null);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET)
            {
                return false;
            }
        }

        return true;
    }

    public String getGuiID()
    {
        return "minecraft:generator";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerGenerator(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.generatorBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            case 4:
                return this.storage.getEnergyStored();
            case 5:
                return this.storage.getMaxEnergyStored();
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.generatorBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
                break;
            case 4:
            	storage.setEnergy(value);
                break;
            case 5:
            	storage.setCapacity(value);
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        for (int i = 0; i < this.generatorItemStacks.size(); ++i)
        {
            this.generatorItemStacks.set(i, ItemStack.EMPTY);
        }
    }

    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
        if(capability == net.minecraftforge.energy.CapabilityEnergy.ENERGY){
        	return (T) storage;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, net.minecraft.util.EnumFacing facing)
    {
        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == net.minecraftforge.energy.CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }
    
	@Override
	public void markDirty() {
		
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString("Generator");
	}
	
	public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
	
	public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }
	
    public EnergyStorageMod getStorage() {
		return storage;
	}
}