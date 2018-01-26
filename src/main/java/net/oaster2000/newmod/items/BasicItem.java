package net.oaster2000.newmod.items;

import net.minecraft.item.Item;
import net.oaster2000.newmod.Main;

public class BasicItem extends Item{

	public BasicItem(String unlocalizedName){		
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(unlocalizedName);
		this.setCreativeTab(Main.creativeTab);
	}
	
}
