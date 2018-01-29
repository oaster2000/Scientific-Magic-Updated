package net.oaster2000.newmod.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.oaster2000.newmod.blocks.ModBlocks;
import net.oaster2000.newmod.items.ModItems;

public class CraftingHandler {
	
	public static void addSmeltingRecipies(){
		GameRegistry.addSmelting(ModItems.ironDust, new ItemStack(Items.IRON_INGOT), 5.0f);
		GameRegistry.addSmelting(ModItems.goldDust, new ItemStack(Items.GOLD_INGOT), 5.0f);
		GameRegistry.addSmelting(ModItems.dDust, new ItemStack(ModItems.dStone), 5.0f);
		GameRegistry.addSmelting(ModItems.bronzeDust, new ItemStack(ModItems.bronze), 5.0f);
		GameRegistry.addSmelting(ModItems.copperDust, new ItemStack(ModItems.copper), 5.0f);
		GameRegistry.addSmelting(ModItems.tinDust, new ItemStack(ModItems.tin), 5.0f);
		GameRegistry.addSmelting(ModItems.silverDust, new ItemStack(ModItems.silver), 5.0f);

		GameRegistry.addSmelting(ModBlocks.copperOre, new ItemStack(ModItems.copper), 5.0f);
		GameRegistry.addSmelting(ModBlocks.tinOre, new ItemStack(ModItems.tin), 5.0f);
		GameRegistry.addSmelting(ModBlocks.silverOre, new ItemStack(ModItems.silver), 5.0f);
	}

}
