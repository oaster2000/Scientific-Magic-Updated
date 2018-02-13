package net.oaster2000.newmod.gui;

import net.minecraft.client.gui.GuiScreen;

public class GUIObscural extends GuiScreen{

	@Override
	public void initGui() {
	}
	
	@Override
	public boolean doesGuiPauseGame() {
	    return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	    this.drawDefaultBackground();
	    super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
