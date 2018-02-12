package net.oaster2000.newmod.tileentity;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.oaster2000.newmod.research.ResearchList;
import net.oaster2000.newmod.utils.NBTUtils;

public class ObscuralTileEntity extends TileEntity {

	ResearchList rL = new ResearchList();
	
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTUtils.loadAllItems(compound, rL.getAll());
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		NBTUtils.saveAllItems(compound, ResearchList.getAll());

		return compound;
	}
	
}
