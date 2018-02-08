package net.oaster2000.newmod.client.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.items.ModItems;

public class ItemRenderRegister {

	public static String modid = Main.MODID;
	
	public static void registerItemRenderer(){
		reg(ModItems.hammer);
		reg(ModItems.dStone);
		reg(ModItems.dGem);
		reg(ModItems.dCrystal);
		reg(ModItems.pCrystal);
		reg(ModItems.fCrystal);
		reg(ModItems.hCrystal);
		reg(ModItems.battery);
		reg(ModItems.ironDust);
		reg(ModItems.goldDust);
		reg(ModItems.dDust);
		reg(ModItems.matter);
		reg(ModItems.stringUniverse);
		reg(ModItems.bronze);
		reg(ModItems.copper);
		reg(ModItems.tin);
		reg(ModItems.silver);
		reg(ModItems.bronzeDust);
		reg(ModItems.copperDust);
		reg(ModItems.tinDust);
		reg(ModItems.silverDust);
		reg(ModItems.bronzePlate);
		reg(ModItems.copperPlate);
		reg(ModItems.tinPlate);
		reg(ModItems.silverPlate);
		reg(ModItems.ironPlate);
		reg(ModItems.goldPlate);
		reg(ModItems.magicalTome);
	}
	
	public static void reg(Item item){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
	
}
