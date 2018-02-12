package net.oaster2000.newmod.utils;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.oaster2000.newmod.research.Research;

public class NBTUtils {
	public static NBTTagCompound saveAllItems(NBTTagCompound tag, List<Research> list) {
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < list.size(); ++i) {
			Research r = list.get(i);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Research", (byte) r.getID());
			r.writeToNBT(nbttagcompound);
			nbttaglist.appendTag(nbttagcompound);

		}

		if (!nbttaglist.hasNoTags())

		{
			tag.setTag("ResearchItems", nbttaglist);
		}

		return tag;
	}

	public static void loadAllItems(NBTTagCompound tag, List<Research> list) {
		NBTTagList nbttaglist = tag.getTagList("ResearchItems", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Research");
			list.get(j).readFromNBT(nbttagcompound);
		}
	}
}
