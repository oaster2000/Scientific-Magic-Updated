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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.oaster2000.newmod.container.ContainerCrucible;
import net.oaster2000.newmod.crafting.CrucibleRecipies;
import net.oaster2000.newmod.items.ModItems;
import net.oaster2000.newmod.slot.SlotCrucibleFuel;

public class TileEntityCrucible extends TileEntityMachine implements ITickable, ISidedInventory
{
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    /** The ItemStacks that hold the items currently being used in the crucible */
    private NonNullList<ItemStack> crucibleItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
	/** The number of ticks that the crucible will keep burning */
    private int crucibleBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the crucible burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String crucibleCustomName;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.crucibleItemStacks.size();
    }
    
	public boolean isEmpty()
    {
        for (ItemStack itemstack : this.crucibleItemStacks)
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
        return this.crucibleItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.crucibleItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.crucibleItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        boolean flag = !stack.isEmpty() && stack.isItemEqual(this.crucibleItemStacks.get(index)) && ItemStack.areItemStackTagsEqual(stack, this.crucibleItemStacks.get(index));
        this.crucibleItemStacks.set(index, stack);

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
        return this.hasCustomName() ? this.crucibleCustomName : "container.crucible";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.crucibleCustomName != null && !this.crucibleCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.crucibleCustomName = p_145951_1_;
    }

    public static void registerFixesCrucible(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityCrucible.class, new String[] {"Items"}));
    }
    
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.crucibleItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.crucibleItemStacks);
        this.crucibleBurnTime = compound.getInteger("CrucuibleBurnTime");
        this.cookTime = compound.getInteger("CrucuibleCookTime");
        this.totalCookTime = compound.getInteger("CrucuibleCookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.crucibleItemStacks.get(1));

        if (compound.hasKey("CustomName", 8))
        {
            this.crucibleCustomName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("CrucuibleBurnTime", (short)this.crucibleBurnTime);
        compound.setInteger("CrucuibleCookTime", (short)this.cookTime);
        compound.setInteger("CrucuibleCookTimeTotal", (short)this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.crucibleItemStacks);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.crucibleCustomName);
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
     * Crucible isBurning
     */
    public boolean isBurning()
    {
        return this.crucibleBurnTime > 0;
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
            --this.crucibleBurnTime;
        }

        if (!this.world.isRemote)
        {
            if (this.isBurning() || !this.crucibleItemStacks.get(1).isEmpty() && this.crucibleItemStacks.get(0) != null)
            {
                if (!this.isBurning() && this.canSmelt())
                {
                    this.crucibleBurnTime = getItemBurnTime(this.crucibleItemStacks.get(1));
                    this.currentItemBurnTime = this.crucibleBurnTime;

                    if (this.isBurning())
                    {
                        flag1 = true;

                        if (!this.crucibleItemStacks.get(1).isEmpty())
                        {
                            this.crucibleItemStacks.get(1).shrink(1);

                            if (this.crucibleItemStacks.get(1).getCount() == 0)
                            {
                                this.crucibleItemStacks.set(1, crucibleItemStacks.get(1).getItem().getContainerItem(crucibleItemStacks.get(1)));
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.crucibleItemStacks.get(0));
                        this.smeltItem();
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
     * Returns true if the crucible can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (this.crucibleItemStacks.get(0).isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack itemstack = CrucibleRecipies.instance().getSmeltingResult(this.crucibleItemStacks.get(0));
            if (itemstack.isEmpty()) return false;
            if (this.crucibleItemStacks.get(2).isEmpty()) return true;
            if (!this.crucibleItemStacks.get(2).isItemEqual(itemstack)) return false;
            int result = crucibleItemStacks.get(2).getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <= this.crucibleItemStacks.get(2).getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the crucible source stack into the appropriate smelted item in the crucible result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = CrucibleRecipies.instance().getSmeltingResult(this.crucibleItemStacks.get(0));

            if (this.crucibleItemStacks.get(2).isEmpty())
            {
                this.crucibleItemStacks.set(2, itemstack.copy());
            }
            else if (this.crucibleItemStacks.get(2).getItem() == itemstack.getItem())
            {
                this.crucibleItemStacks.get(2).grow(itemstack.getCount()); // Forge BugFix: Results may have multiple items
            }

            if (this.crucibleItemStacks.get(0).getItem() == Item.getItemFromBlock(Blocks.SPONGE) && this.crucibleItemStacks.get(0).getMetadata() == 1 && this.crucibleItemStacks.get(1) != null && this.crucibleItemStacks.get(1).getItem() == Items.BUCKET)
            {
                this.crucibleItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            this.crucibleItemStacks.get(0).shrink(1);

            if (this.crucibleItemStacks.get(0).getCount() <= 0)
            {
                this.crucibleItemStacks.set(0, ItemStack.EMPTY);
            }
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the crucible burning, or 0 if the item isn't
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

            if (item == Items.BLAZE_POWDER) return 200;
            if (item == ModItems.pCrystal) return 2400;
            return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(stack);
        }
    }

    public static boolean isItemFuel(ItemStack stack)
    {
        /**
         * Returns the number of ticks that the supplied fuel item will keep the crucible burning, or 0 if the item isn't
         * fuel
         */
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
        if (index == 2)
        {
            return false;
        }
        else if (index != 1)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.crucibleItemStacks.get(1);
            return isItemFuel(stack) || SlotCrucibleFuel.isBucket(stack) && (itemstack.isEmpty() || itemstack.getItem() != Items.BUCKET);
        }
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
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
        return "minecraft:crucible";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerCrucible(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.crucibleBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.crucibleBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        for (int i = 0; i < this.crucibleItemStacks.size(); ++i)
        {
            this.crucibleItemStacks.set(i, ItemStack.EMPTY);
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
        return super.getCapability(capability, facing);
    }

	@Override
	public void markDirty() {
		
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString("Alembic Crucible");
	}
}


/*

	SO! Right now my generator is producing energy however it is not being displayed on the generator, so i am going to move onto the wire/cable to connect it.
	My problem is due to my knowledge of 1.10.2 modding being not at the level of changeable block models i am going to use a full sized block as of this moment.
	So you see my process for texturing XD. This will be fun!

*/