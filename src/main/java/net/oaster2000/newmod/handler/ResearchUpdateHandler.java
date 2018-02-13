package net.oaster2000.newmod.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.oaster2000.newmod.gui.toasts.ResearchToast;
import net.oaster2000.newmod.items.ModItems;
import net.oaster2000.newmod.research.Research;
import net.oaster2000.newmod.research.Research.EnumResearchState;
import net.oaster2000.newmod.research.ResearchList;

public class ResearchUpdateHandler {

	@SubscribeEvent
	public void pickupItem(PlayerTickEvent event) {
		ItemStack stack = new ItemStack(ModItems.magicalTome);
		if (event.player.inventory.hasItemStack(stack) && ResearchList.start.getState().equals(EnumResearchState.UNDISCOVERED)) {
			if(ResearchList.start.shouldShowToast()) {
				Minecraft.getMinecraft().getToastGui().add(new ResearchToast(ResearchList.start));
				ResearchList.start.setShowToast(false);
			}
			ResearchList.start.setState(EnumResearchState.FOUND);
			for (Research children : ResearchList.start.getChildren()) {
				children.setState(EnumResearchState.UNDISCOVERED);
			}
		}

		if (ResearchList.start.getState().equals(EnumResearchState.FOUND)) {
			ItemStack stack1 = new ItemStack(ModItems.hCrystal);
			if (event.player.inventory.hasItemStack(stack1) && ResearchList.hope.getState().equals(EnumResearchState.UNDISCOVERED)) {
				if(ResearchList.hope.shouldShowToast()) {
					Minecraft.getMinecraft().getToastGui().add(new ResearchToast(ResearchList.hope));
					ResearchList.hope.setShowToast(false);
				}
				ResearchList.hope.setState(EnumResearchState.FOUND);
				for (Research children : ResearchList.hope.getChildren()) {
					children.setState(EnumResearchState.UNDISCOVERED);
				}
				ResearchList.faith.setState(EnumResearchState.HIDDEN);
				ResearchList.power.setState(EnumResearchState.HIDDEN);
			}

			ItemStack stack2 = new ItemStack(ModItems.fCrystal);
			if (event.player.inventory.hasItemStack(stack2) && ResearchList.faith.getState().equals(EnumResearchState.UNDISCOVERED)) {
				if(ResearchList.faith.shouldShowToast()) {
					Minecraft.getMinecraft().getToastGui().add(new ResearchToast(ResearchList.faith));
					ResearchList.faith.setShowToast(false);
				}
				ResearchList.faith.setState(EnumResearchState.FOUND);
				for (Research children : ResearchList.faith.getChildren()) {
					children.setState(EnumResearchState.UNDISCOVERED);
				}
				ResearchList.hope.setState(EnumResearchState.HIDDEN);
				ResearchList.power.setState(EnumResearchState.HIDDEN);
			}

			ItemStack stack3 = new ItemStack(ModItems.pCrystal);
			if (event.player.inventory.hasItemStack(stack3) && ResearchList.power.getState().equals(EnumResearchState.UNDISCOVERED)) {
				if(ResearchList.power.shouldShowToast()) {
					Minecraft.getMinecraft().getToastGui().add(new ResearchToast(ResearchList.power));
					ResearchList.power.setShowToast(false);
				}
				ResearchList.power.setState(EnumResearchState.FOUND);
				for (Research children : ResearchList.power.getChildren()) {
					children.setState(EnumResearchState.UNDISCOVERED);
				}
				ResearchList.faith.setState(EnumResearchState.HIDDEN);
				ResearchList.hope.setState(EnumResearchState.HIDDEN);
			}
		}
	}
}
