package net.oaster2000.newmod.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.blocks.ModBlocks;

public class TutorialTab extends CreativeTabs{

	public TutorialTab() {
		super(Main.MODID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModBlocks.gen);
	}
	
}
