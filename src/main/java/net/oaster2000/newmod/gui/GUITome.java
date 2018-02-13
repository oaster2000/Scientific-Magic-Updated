package net.oaster2000.newmod.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.oaster2000.newmod.research.Research;
import net.oaster2000.newmod.research.ResearchList;
import net.oaster2000.newmod.research.Research.EnumResearchState;

public class GUITome extends GuiScreen {

	private static final ResourceLocation BG_GUI_TEXTURES = new ResourceLocation("newmod",
			"textures/gui/obscural_bg.png");
	private static final ResourceLocation FG_GUI_TEXTURES = new ResourceLocation("newmod",
			"textures/gui/obscural_fg.png");
	private static final ResourceLocation ICON_GUI_TEXTURES = new ResourceLocation("newmod",
			"textures/gui/obscural_icons.png");

	RenderItem rI;

	private List<Research> research = new ArrayList<Research>();

	@Override
	public void initGui() {
		ResearchList.initList();
		research = ResearchList.getAll();
		rI = this.itemRender;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		int i = (this.width - 256) / 2;
		int j = (this.height - 206) / 2;
		this.mc.getTextureManager().bindTexture(BG_GUI_TEXTURES);
		this.drawTexturedModalRect(i + 4, j + 4, 0, 0, 248, 198);
		this.mc.getTextureManager().bindTexture(FG_GUI_TEXTURES);
		this.drawTexturedModalRect(i, j, 0, 0, 256, 206);

		drawResearch(i, j);

		super.drawScreen(mouseX, mouseY, partialTicks);

	}

	private void drawResearch(int i, int j) {
		int xItemPadding = 4;
		int yItemPadding = 4;
		for (Research r : research) {
			int xPos = r.getX();
			int yPos = r.getY();
			if (!(r.getState().equals(EnumResearchState.HIDDEN))) {
				if (!r.isChildrenEmpty()) {
					drawConnectivity(i, j, r);
				}
				this.mc.getTextureManager().bindTexture(ICON_GUI_TEXTURES);
				switch (r.getState()) {
				case UNDISCOVERED:
					switch (r.getType()) {
					case "ALL":
						this.drawTexturedModalRect(i + xPos, j + yPos, 0, 0, 24, 24);
						break;
					case "HOPE":
						this.drawTexturedModalRect(i + xPos, j + yPos, 24, 0, 24, 24);
						break;
					case "POWER":
						this.drawTexturedModalRect(i + xPos, j + yPos, 48, 0, 24, 24);
						break;
					case "FAITH":
						this.drawTexturedModalRect(i + xPos, j + yPos, 72, 0, 24, 24);
						break;
					}
					break;
				case HIDDEN:
					break;
				case FOUND:
					switch (r.getType()) {
					case "ALL":
						this.drawTexturedModalRect(i + xPos, j + yPos, 0, 24, 24, 24);
						break;
					case "HOPE":
						this.drawTexturedModalRect(i + xPos, j + yPos, 24, 24, 24, 24);
						break;
					case "POWER":
						this.drawTexturedModalRect(i + xPos, j + yPos, 48, 24, 24, 24);
						break;
					case "FAITH":
						this.drawTexturedModalRect(i + xPos, j + yPos, 72, 24, 24, 24);
						break;
					}
				}
				if (r.getIcon() != null)
					rI.renderItemAndEffectIntoGUI((EntityLivingBase) null, r.getIcon(), i + xPos + xItemPadding,
							j + yPos + yItemPadding);
			}
		}
	}

	public void drawConnectivity(int x, int y, Research r) {
		for (Research child : r.getChildren()) {
			int parentX = x + r.getX() + 12;
			int parentY = y + r.getY() + 12;
			int childX = x + child.getX() + 12;
			int childY = y + child.getY() + 12;

			if(!child.getState().equals(EnumResearchState.HIDDEN)) {
			drawLine(parentX, parentY, childX, childY);
			}	
		}
	}

	private void drawLine(int xStart, int yStart, int xEnd, int yEnd) {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		Color c = new Color(255, 255, 255, 150);
		GL11.glColor4d(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		GL11.glLineWidth(0.5f);
		GL11.glDepthMask(false);
		Tessellator t = Tessellator.getInstance();
		BufferBuilder bufferBuilder = t.getBuffer();
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

		bufferBuilder.pos(xStart, yStart, 0.00f).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();
		bufferBuilder.pos(xEnd, yEnd, 0.00f).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).endVertex();

		t.draw();

		GL11.glDepthMask(true);
		GL11.glPopAttrib();
	}
}
