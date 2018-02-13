package net.oaster2000.newmod.gui.toasts;

import java.util.List;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.oaster2000.newmod.research.Research;

@SideOnly(Side.CLIENT)
public class ResearchToast implements IToast {
	private final Research advancement;
	private boolean hasPlayedSound = false;

	public ResearchToast(Research advancementIn) {
		this.advancement = advancementIn;
	}

	public IToast.Visibility draw(GuiToast toastGui, long delta) {
		toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

		List<String> list = toastGui.getMinecraft().fontRenderer.listFormattedStringToWidth(advancement.getName(), 125);
		int i = 16776960;

		if (list.size() == 1) {
			toastGui.getMinecraft().fontRenderer.drawString("Research Found!", 30, 7, i | -16777216);
			toastGui.getMinecraft().fontRenderer.drawString(advancement.getName(), 30, 18, -1);
		} else {
			int j = 1500;
			float f = 300.0F;

			if (delta < 1500L) {
				int k = MathHelper.floor(MathHelper.clamp((float) (1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24
						| 67108864;
				toastGui.getMinecraft().fontRenderer.drawString("Research Found!", 30, 11, i | k);
			} else {
				int i1 = MathHelper.floor(MathHelper.clamp((float) (delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24
						| 67108864;
				int l = 16 - list.size() * toastGui.getMinecraft().fontRenderer.FONT_HEIGHT / 2;

				for (String s : list) {
					toastGui.getMinecraft().fontRenderer.drawString(s, 30, l, 16777215 | i1);
					l += toastGui.getMinecraft().fontRenderer.FONT_HEIGHT;
				}
			}
		}

		if (!this.hasPlayedSound && delta > 0L) {
			this.hasPlayedSound = true;

		}

		RenderHelper.enableGUIStandardItemLighting();
		toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase) null,
				advancement.getIcon(), 8, 8);
		return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
	}
}