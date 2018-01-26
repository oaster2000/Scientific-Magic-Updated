package net.oaster2000.newmod.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.oaster2000.newmod.blocks.ModBlocks;
import net.oaster2000.newmod.items.ModItems;

public class CraftingHandler {

	public static void addShapedRecipies() {
		/*GameRegistry.addShapedRecipe(new ItemStack(ModItems.dGem), new Object[]{
				"AAA",
				"ADA",
				"AAA",
				'A', ModItems.dStone,
				'D', Items.DIAMOND
    			}
    		);
		
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.firstBlock), new Object[]{
				"DDD",
				"DAD",
				"DDD",
				'A', ModItems.pCrystal,
				'D', ModItems.dGem
    			}
    		);
		
		GameRegistry.addRecipe(new ItemStack(ModBlocks.firstBlock), new Object[]{
				"DDD",
				"DAD",
				"DDD",
				'A', new ItemStack(ModItems.pCrystal, 1, OreDictionary.WILDCARD_VALUE),
				'D', ModItems.dGem
        });
		
		GameRegistry.addRecipe(new ItemStack(ModBlocks.gen), new Object[]{
				"DRD",
				"RAR",
				"DRD",
				'A', ModItems.battery,
				'D', Blocks.IRON_BLOCK,
				'R', Items.REDSTONE
        });
		
		GameRegistry.addRecipe(new ItemStack(ModItems.battery), new Object[]{
				" T ",
				"TRT",
				"TCT",
				'T', ModItems.tinPlate,
				'R', Items.REDSTONE,
				'C', ModBlocks.wire
        });
		
		GameRegistry.addRecipe(new ItemStack(ModItems.hammer), new Object[]{
				"III",
				"III",
				" S ",
				'S', Items.STICK,
				'I', Items.IRON_INGOT
        });
		
		GameRegistry.addRecipe(new ItemStack(ModBlocks.wire), new Object[]{
				"WWW",
				"CCC",
				"WWW",
				'C', ModItems.copper,
				'W', Blocks.WOOL
        });*/
	}
	
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

	public static void addShapelessRecipies() {
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.tinPlate), new Object[]{ new ItemStack(ModItems.hammer, 1, OreDictionary.WILDCARD_VALUE), ModItems.tin });
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.copperPlate), new Object[]{ new ItemStack(ModItems.hammer, 1, OreDictionary.WILDCARD_VALUE), ModItems.copper });
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.bronzePlate), new Object[]{ new ItemStack(ModItems.hammer, 1, OreDictionary.WILDCARD_VALUE), ModItems.bronze });
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.silverPlate), new Object[]{ new ItemStack(ModItems.hammer, 1, OreDictionary.WILDCARD_VALUE), ModItems.silver });
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ironPlate), new Object[]{ new ItemStack(ModItems.hammer, 1, OreDictionary.WILDCARD_VALUE), Items.IRON_INGOT });
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.goldPlate), new Object[]{ new ItemStack(ModItems.hammer, 1, OreDictionary.WILDCARD_VALUE), Items.GOLD_INGOT });
		
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.bronzeDust), new Object[]{ModItems.copperDust, ModItems.copperDust, ModItems.copperDust, ModItems.tinDust,});
	}
	
}
