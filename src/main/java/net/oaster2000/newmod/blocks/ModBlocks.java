package net.oaster2000.newmod.blocks;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.oaster2000.newmod.Main;

public final class ModBlocks {

	public static Block firstBlock;
	public static Block dStone_ore;
	public static Block dStone_ore_nether;
	public static Block aCrucible;
	public static Block macerator;
	public static Block macerator_ON;
	public static Block furnace;
	public static Block furnace_on;
	public static Block gen;
	public static Block gen_on;
	public static Block wire;
	public static Block sunGen;
	public static Block sunGen_on;
	public static Block copperOre;
	public static Block tinOre;
	public static Block silverOre;
	public static Block deCon;
	public static Block deCon_on;
	public static Block obscural;

	public static void createBlocks() {
		firstBlock = (new StrongBlock("firstblock"));
		dStone_ore = (new StoneOreBlock("dstone_ore"));
		dStone_ore_nether = (new NetherOreBlock("dstone_ore_nether"));
		aCrucible = (new CrucibleBlock("acrucible"));
		macerator = (new MaceratorBlock(false, "macerator")).setCreativeTab(Main.creativeTab);
		macerator_ON = (new MaceratorBlock(true, "macerator_on"));
		gen = (new GeneratorBlock(false, "gen")).setCreativeTab(Main.creativeTab);
		gen_on = (new GeneratorBlock(true, "gen_on"));
		wire = (new WireBlock("wire"));
		sunGen = (new SolarGeneratorBlock(false, "sungen")).setCreativeTab(Main.creativeTab);
		sunGen_on = (new SolarGeneratorBlock(true, "sungen_on"));
		furnace = (new ElectricFurnaceBlock(false, "furnaceel")).setCreativeTab(Main.creativeTab);
		furnace_on = (new ElectricFurnaceBlock(true, "furnaceel_on"));
		copperOre = (new StoneOreBlock("copperore"));
		tinOre = (new StoneOreBlock("tinore"));
		silverOre = (new StoneOreBlock("silverore"));
		deCon = (new DeconstructorBlock(false, "decon")).setCreativeTab(Main.creativeTab);
		deCon_on = (new DeconstructorBlock(true, "decon_on"));
		obscural = (new ObscuralBlock("obscural"));
	}

	@Mod.EventBusSubscriber(modid = Main.MODID)
	public static class RegistrationHandler {
		public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();

		/**
		 * Register this mod's {@link Block}s.
		 *
		 * @param event
		 *            The event
		 */
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			final Block[] blocks = { firstBlock, dStone_ore, dStone_ore_nether, aCrucible, macerator, macerator_ON,
					furnace, furnace_on, gen, gen_on, wire, sunGen, sunGen_on, copperOre, tinOre, silverOre, deCon,
					deCon_on, obscural };

			registry.registerAll(blocks);
		}

		/**
		 * Register this mod's {@link ItemBlock}s.
		 *
		 * @param event
		 *            The event
		 */
		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final ItemBlock[] items = { new ItemBlock(firstBlock), new ItemBlock(dStone_ore),
					new ItemBlock(dStone_ore_nether), new ItemBlock(aCrucible), new ItemBlock(macerator),
					new ItemBlock(macerator_ON), new ItemBlock(furnace), new ItemBlock(furnace_on), new ItemBlock(gen),
					new ItemBlock(gen_on), new ItemBlock(wire), new ItemBlock(sunGen), new ItemBlock(sunGen_on),
					new ItemBlock(copperOre), new ItemBlock(tinOre), new ItemBlock(silverOre), new ItemBlock(deCon),
					new ItemBlock(deCon_on),  new ItemBlock(obscural)};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final ItemBlock item : items) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(),
						"Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				ITEM_BLOCKS.add(item);
			}
		}
	}

}