package net.oaster2000.newmod.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.oaster2000.newmod.container.ContainerSolarGenerator;
import net.oaster2000.newmod.tileentity.TileEntitySolarGenerator;

@SideOnly(Side.CLIENT)
public class GuiSolarGenerator extends GuiContainer
{
    private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("newmod", "textures/gui/gensolar.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final TileEntitySolarGenerator tileFurnace;

    public GuiSolarGenerator(InventoryPlayer playerInv, TileEntitySolarGenerator furnaceInv)
    {
        super(new ContainerSolarGenerator(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;
    }

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
    
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        List<String> hoveringText = new ArrayList<String>();

		// If the mouse is over the progress bar add the progress bar hovering text
		if (isInRect(guiLeft + 81, guiTop + 23, 13, 43, mouseX, mouseY)){
			hoveringText.add("Energy:");
			int energy = tileFurnace.getField(4);
			int capacity = tileFurnace.getField(5);
			hoveringText.add(energy + "/" + capacity);
		}
		
		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}
    
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int k = this.getEnergyScaled(44);
        this.drawTexturedModalRect(i + 81, j + 66 - k, 176, 74 - k, 14, k);
    }

    private int getEnergyScaled(int pixels)
    {
    	int i = this.tileFurnace.getField(4);
    	int j = this.tileFurnace.getField(5);
        return j !=0 && i != 0 ? i * pixels / j : 0;
    }
    
    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
}