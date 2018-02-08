package net.oaster2000.newmod.items;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.oaster2000.newmod.Main;

public final class ModItems {

	public static Item hammer;
	public static Item dStone;
	public static Item dGem;
	public static Item dCrystal;
	public static Item pCrystal;
	public static Item fCrystal;
	public static Item hCrystal;
	public static Item battery;
	public static Item ironDust;
	public static Item goldDust;
	public static Item dDust;
	public static Item stringUniverse;
	public static Item matter;
	public static Item bronze;
	public static Item copper;
	public static Item silver;
	public static Item tin;
	public static Item bronzeDust;
	public static Item copperDust;
	public static Item silverDust;
	public static Item tinDust;
	public static Item bronzePlate;
	public static Item copperPlate;
	public static Item silverPlate;
	public static Item tinPlate;
	public static Item ironPlate;
	public static Item goldPlate;
	public static Item magicalTome;

	public static void createItems() {
		hammer = (new HammerItem("hammer"));
		dStone = (new BasicItem("dStone"));
		dGem = (new BasicItem("dGem"));
		dCrystal = (new CrystalItem("dCrystal"));
		pCrystal = (new CrystalItem("pCrystal"));
		fCrystal = (new CrystalItem("fCrystal"));
		hCrystal = (new CrystalItem("hCrystal"));
		battery = (new ElectricItem("battery"));
		ironDust = (new BasicItem("ironDust"));
		goldDust = (new BasicItem("goldDust"));
		dDust = (new BasicItem("dDust"));
		stringUniverse = (new StringItem("stringUniverse"));
		matter = (new BasicItem("matter"));
		bronze = (new BasicItem("bronze"));
		copper = (new BasicItem("copper"));
		silver = (new BasicItem("silver"));
		tin = (new BasicItem("tin"));
		bronzeDust = (new BasicItem("bronzeDust"));
		copperDust = (new BasicItem("copperDust"));
		silverDust = (new BasicItem("silverDust"));
		tinDust = (new BasicItem("tinDust"));
		bronzePlate = (new BasicItem("bronzePlate"));
		copperPlate = (new BasicItem("copperPlate"));
		silverPlate = (new BasicItem("silverPlate"));
		tinPlate = (new BasicItem("tinPlate"));
		ironPlate = (new BasicItem("ironPlate"));
		goldPlate = (new BasicItem("goldPlate"));
		magicalTome = (new TomeItem("magicalTome"));
	}

	@Mod.EventBusSubscriber(modid = Main.MODID)
	public static class RegistrationHandler {
		public static final Set<Item> ITEMS = new HashSet<>();

		/**
		 * Register this mod's {@link Item}s.
		 *
		 * @param event
		 *            The event
		 */
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final Item[] items = { hammer, dStone, dGem, dCrystal, pCrystal, fCrystal,
					hCrystal, battery, ironDust, goldDust, dDust, stringUniverse, matter,
					bronze, copper, silver, tin, bronzeDust, copperDust, silverDust,
					tinDust, bronzePlate, copperPlate, silverPlate, tinPlate, ironPlate,
					goldPlate, magicalTome};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}
		}
	}

}
