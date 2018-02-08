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
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.oaster2000.newmod.blocks.ElectricFurnaceBlock;
import net.oaster2000.newmod.container.ContainerElectricFurnace;
import net.oaster2000.newmod.energy.EnergyStorageMod;
import net.oaster2000.newmod.slot.SlotElectricFurnaceFuel;

public class TileEntityElectricFurnace extends TileEntityMachine implements ITickable, ISidedInventory {
	private static final int[] SLOTS_TOP = new int[] { 0 };
	private static final int[] SLOTS_BOTTOM = new int[] { 2, 1 };
	private static final int[] SLOTS_SIDES = new int[] { 1 };

	int tick = 0;

	/**
	 * The ItemStacks that hold the items currently being used in the
	 * electricFurnace
	 */
	private NonNullList<ItemStack> electricFurnaceItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
	/** The number of ticks that the electricFurnace will keep burning */
	private int electricFurnaceBurnTime;
	/**
	 * The number of ticks that a fresh copy of the currently-burning item would
	 * keep the electricFurnace burning for
	 */
	private int currentItemBurnTime;
	private int cookTime;
	private int totalCookTime;
	private String electricFurnaceCustomName;

	protected EnergyStorageMod storage;

	public TileEntityElectricFurnace() {
		storage = new EnergyStorageMod(1600);
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory() {
		return this.electricFurnaceItemStacks.size();
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : this.electricFurnaceItemStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Nullable
	public ItemStack getStackInSlot(int index) {
		return this.electricFurnaceItemStacks.get(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns
	 * them in a new stack.
	 */
	@Nullable
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.electricFurnaceItemStacks, index, count);
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Nullable
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.electricFurnaceItemStacks, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		boolean flag = !stack.isEmpty() && stack.isItemEqual(this.electricFurnaceItemStacks.get(index))
				&& ItemStack.areItemStackTagsEqual(stack, this.electricFurnaceItemStacks.get(index));
		this.electricFurnaceItemStacks.set(index, stack);

		if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.totalCookTime = this.getCookTime(stack);
			this.cookTime = 0;
			this.markDirty();
		}
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	public String getName() {
		return this.hasCustomName() ? this.electricFurnaceCustomName : "container.electricFurnace";
	}

	/**
	 * Returns true if this thing is named
	 */
	public boolean hasCustomName() {
		return this.electricFurnaceCustomName != null && !this.electricFurnaceCustomName.isEmpty();
	}

	public void setCustomInventoryName(String p_145951_1_) {
		this.electricFurnaceCustomName = p_145951_1_;
	}

	public static void registerFixesElectricFurnace(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY,
				new ItemStackDataLists(TileEntityElectricFurnace.class, new String[] { "Items" }));
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.electricFurnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.electricFurnaceItemStacks);
		this.electricFurnaceBurnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentItemBurnTime = getItemBurnTime(this.electricFurnaceItemStacks.get(1));
		storage.setEnergy(compound.getInteger("Energy"));

		if (compound.hasKey("CustomName", 8)) {
			this.electricFurnaceCustomName = compound.getString("CustomName");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short) this.electricFurnaceBurnTime);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
		compound.setInteger("Energy", storage.getEnergyStored());
		ItemStackHelper.saveAllItems(compound, this.electricFurnaceItemStacks);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.electricFurnaceCustomName);
		}

		return compound;
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64,
	 * possibly will be extended.
	 */
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * ElectricFurnace isBurning
	 */
	public boolean isBurning() {
		return this.electricFurnaceBurnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {
		tick++;

		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.electricFurnaceBurnTime;
		}

		if (!this.world.isRemote) {
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			isNeighborGen(world, pos.getX(), pos.getY(), pos.getZ());
			isNeighborSolar(world, pos.getX(), pos.getY(), pos.getZ());
			isNeighborWire(world, pos.getX(), pos.getY(), pos.getZ());
			if (this.isBurning() || !this.electricFurnaceItemStacks.get(0).isEmpty()) {
				if (!this.isBurning() && this.canSmelt()) {
					this.electricFurnaceBurnTime = storage.getEnergyStored();
					this.currentItemBurnTime = this.electricFurnaceBurnTime;

					if (this.isBurning()) {
						flag1 = true;
						world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

						if (!this.electricFurnaceItemStacks.get(1).isEmpty() && storage.getEnergyStored() > 0) {
							this.electricFurnaceItemStacks.get(1).shrink(1);

							if (this.electricFurnaceItemStacks.get(1).getCount() == 0) {
								this.electricFurnaceItemStacks.set(1, electricFurnaceItemStacks.get(1).getItem()
										.getContainerItem(electricFurnaceItemStacks.get(1)));
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;

					if (this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(this.electricFurnaceItemStacks.get(0));
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}

			if (flag != this.isBurning()) {
				flag1 = true;
				ElectricFurnaceBlock.setState(this.isBurning(), this.world, this.pos);
			}
		}

		if (this.isBurning()) {
			storage.extractEnergy(1, false);
		}

		if (flag1) {
			this.markDirty();
		}

		if (tick >= 20) {
			tick = 0;
		}
	}

	public int getCookTime(@Nullable ItemStack stack) {
		return 100;
	}

	/**
	 * Returns true if the electricFurnace can smelt an item, i.e. has a source
	 * item, destination stack isn't full, etc.
	 */
	private boolean canSmelt() {
		if (this.electricFurnaceItemStacks.get(0).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.electricFurnaceItemStacks.get(0));
			if (itemstack.isEmpty())
				return false;
			if (this.electricFurnaceItemStacks.get(2).isEmpty())
				return true;
			if (!this.electricFurnaceItemStacks.get(2).isItemEqual(itemstack))
				return false;
			int result = electricFurnaceItemStacks.get(2).getCount() + itemstack.getCount();
			return result <= getInventoryStackLimit()
					&& result <= this.electricFurnaceItemStacks.get(2).getMaxStackSize(); // Forge
			// properly.
		}
	}

	/**
	 * Turn one item from the electricFurnace source stack into the appropriate
	 * smelted item in the electricFurnace result stack
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.electricFurnaceItemStacks.get(0));

			if (this.electricFurnaceItemStacks.get(2).isEmpty()) {
				this.electricFurnaceItemStacks.set(2, itemstack.copy());
			} else if (this.electricFurnaceItemStacks.get(2).getItem() == itemstack.getItem()) {
				this.electricFurnaceItemStacks.get(2).grow(itemstack.getCount()); // Forge
																					// //
																					// items
			}

			if (this.electricFurnaceItemStacks.get(0).getItem() == Item.getItemFromBlock(Blocks.SPONGE)
					&& this.electricFurnaceItemStacks.get(0).getMetadata() == 1
					&& !this.electricFurnaceItemStacks.get(1).isEmpty()
					&& this.electricFurnaceItemStacks.get(1).getItem() == Items.BUCKET) {
				this.electricFurnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
			}

			this.electricFurnaceItemStacks.get(0).shrink(1);
			;

			if (this.electricFurnaceItemStacks.get(0).getCount() <= 0) {
				this.electricFurnaceItemStacks.set(0, ItemStack.EMPTY);
			}
		}
	}

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * electricFurnace burning, or 0 if the item isn't fuel
	 */
	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		} else {
			Item item = stack.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {
				Block block = Block.getBlockFromItem(item);

			}

			if (item == Items.COAL)
				return 200;
			return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(stack);
		}
	}

	public static boolean isItemFuel(ItemStack stack) {
		/**
		 * Returns the number of ticks that the supplied fuel item will keep the
		 * electricFurnace burning, or 0 if the item isn't fuel
		 */
		return getItemBurnTime(stack) > 0;
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false
				: player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
						(double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public void openInventory(EntityPlayer player) {
	}

	public void closeInventory(EntityPlayer player) {
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot. For guis use Slot.isItemValid
	 */
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 2) {
			return false;
		} else if (index != 1) {
			return true;
		} else {
			ItemStack itemstack = this.electricFurnaceItemStacks.get(1);
			return isItemFuel(stack) || SlotElectricFurnaceFuel.isBucket(stack)
					&& (itemstack.isEmpty() || itemstack.getItem() != Items.BUCKET);
		}
	}

	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from
	 * the given side.
	 */
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from
	 * the given side.
	 */
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			Item item = stack.getItem();

			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}

		return true;
	}

	public String getGuiID() {
		return "minecraft:electricFurnace";
	}

	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerElectricFurnace(playerInventory, this);
	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return this.electricFurnaceBurnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		case 4:
			return storage.getEnergyStored();
		case 5:
			return storage.getMaxEnergyStored();
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.electricFurnaceBurnTime = value;
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

	public int getFieldCount() {
		return 6;
	}

	public void clear() {
		for (int i = 0; i < this.electricFurnaceItemStacks.size(); ++i) {
			this.electricFurnaceItemStacks.set(i, ItemStack.EMPTY);
		}
	}

	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this,
			net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this,
			net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this,
			net.minecraft.util.EnumFacing.WEST);
	
	@Override
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability,
			net.minecraft.util.EnumFacing facing) {
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
		return new TextComponentString("Electric Furnace");
	}

	public void isNeighborGen(World world, int x, int y, int z) {
		if (world.getTileEntity(new BlockPos(x + 1, y, z)) instanceof TileEntityGenerator) {
			TileEntityGenerator tileentity = (TileEntityGenerator) world.getTileEntity(new BlockPos(x + 1, y, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x - 1, y, z)) instanceof TileEntityGenerator) {
			TileEntityGenerator tileentity = (TileEntityGenerator) world.getTileEntity(new BlockPos(x - 1, y, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y + 1, z)) instanceof TileEntityGenerator) {
			TileEntityGenerator tileentity = (TileEntityGenerator) world.getTileEntity(new BlockPos(x, y + 1, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y - 1, z)) instanceof TileEntityGenerator) {
			TileEntityGenerator tileentity = (TileEntityGenerator) world.getTileEntity(new BlockPos(x, y - 1, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y, z + 1)) instanceof TileEntityGenerator) {
			TileEntityGenerator tileentity = (TileEntityGenerator) world.getTileEntity(new BlockPos(x, y, z + 1));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y, z - 1)) instanceof TileEntityGenerator) {
			TileEntityGenerator tileentity = (TileEntityGenerator) world.getTileEntity(new BlockPos(x, y, z - 1));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
	}

	public void isNeighborSolar(World world, int x, int y, int z) {
		if (world.getTileEntity(new BlockPos(x + 1, y, z)) instanceof TileEntitySolarGenerator) {
			TileEntitySolarGenerator tileentity = (TileEntitySolarGenerator) world
					.getTileEntity(new BlockPos(x + 1, y, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x - 1, y, z)) instanceof TileEntitySolarGenerator) {
			TileEntitySolarGenerator tileentity = (TileEntitySolarGenerator) world
					.getTileEntity(new BlockPos(x - 1, y, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y + 1, z)) instanceof TileEntitySolarGenerator) {
			TileEntitySolarGenerator tileentity = (TileEntitySolarGenerator) world
					.getTileEntity(new BlockPos(x, y + 1, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y - 1, z)) instanceof TileEntitySolarGenerator) {
			TileEntitySolarGenerator tileentity = (TileEntitySolarGenerator) world
					.getTileEntity(new BlockPos(x, y - 1, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y, z + 1)) instanceof TileEntitySolarGenerator) {
			TileEntitySolarGenerator tileentity = (TileEntitySolarGenerator) world
					.getTileEntity(new BlockPos(x, y, z + 1));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y, z - 1)) instanceof TileEntitySolarGenerator) {
			TileEntitySolarGenerator tileentity = (TileEntitySolarGenerator) world
					.getTileEntity(new BlockPos(x, y, z - 1));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
	}

	public void isNeighborWire(World world, int x, int y, int z) {
		if (world.getTileEntity(new BlockPos(x + 1, y, z)) instanceof TileEntityWire) {
			TileEntityWire tileentity = (TileEntityWire) world.getTileEntity(new BlockPos(x + 1, y, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x - 1, y, z)) instanceof TileEntityWire) {
			TileEntityWire tileentity = (TileEntityWire) world.getTileEntity(new BlockPos(x - 1, y, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y + 1, z)) instanceof TileEntityWire) {
			TileEntityWire tileentity = (TileEntityWire) world.getTileEntity(new BlockPos(x, y + 1, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y - 1, z)) instanceof TileEntityWire) {
			TileEntityWire tileentity = (TileEntityWire) world.getTileEntity(new BlockPos(x, y - 1, z));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y, z + 1)) instanceof TileEntityWire) {
			TileEntityWire tileentity = (TileEntityWire) world.getTileEntity(new BlockPos(x, y, z + 1));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
		if (world.getTileEntity(new BlockPos(x, y, z - 1)) instanceof TileEntityWire) {
			TileEntityWire tileentity = (TileEntityWire) world.getTileEntity(new BlockPos(x, y, z - 1));
			TileEntityElectricFurnace tileentityThis = (TileEntityElectricFurnace) world
					.getTileEntity(new BlockPos(x, y, z));
			if (tileentity.getStorage().getEnergyStored() > 0 && tileentityThis.getStorage()
					.getEnergyStored() < tileentityThis.getStorage().getMaxEnergyStored()) {
				tileentity.getStorage().extractEnergy(1, false);
				tileentityThis.getStorage().receiveEnergy(1, false);
			}
		}
	}

	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}

	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	public EnergyStorageMod getStorage() {
		return storage;
	}
}