package net.oaster2000.newmod.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.items.ModItems;

public class ItemCraftingHandler {

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
			if (event.craftMatrix.getStackInSlot(i) != null) {

				ItemStack item0 = event.craftMatrix.getStackInSlot(i);
				if (item0 != null && item0.getItem() == ModItems.hammer) {
					ItemStack k = new ItemStack(ModItems.hammer, 2, item0.getItemDamage() + 1);

					if (k.getItemDamage() >= k.getMaxDamage()) {
						k.shrink(1);
					}

					event.craftMatrix.setInventorySlotContents(i, k);
				}
			}
		}
	}
	
}
