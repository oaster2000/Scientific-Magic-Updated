package net.oaster2000.newmod.client.render.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.blocks.ModBlocks;

public final class BlockRenderRegister {

	public static String modid = Main.MODID;

	public static void registerBlockRenderer() {
		reg(ModBlocks.firstBlock);
		reg(ModBlocks.dStone_ore);
		reg(ModBlocks.dStone_ore_nether);
		reg(ModBlocks.aCrucible);
		reg(ModBlocks.macerator);
		reg(ModBlocks.macerator_ON);
		reg(ModBlocks.furnace);
		reg(ModBlocks.furnace_on);
		reg(ModBlocks.gen);
		reg(ModBlocks.gen_on);
		reg(ModBlocks.wire);
		reg(ModBlocks.sunGen);
		reg(ModBlocks.sunGen_on);
		reg(ModBlocks.copperOre);
		reg(ModBlocks.tinOre);
		reg(ModBlocks.silverOre);
		reg(ModBlocks.deCon);
		reg(ModBlocks.deCon_on);
		reg(ModBlocks.obscural);
	}

	public static void reg(Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}

}