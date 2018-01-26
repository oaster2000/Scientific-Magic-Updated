package net.oaster2000.newmod.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.oaster2000.newmod.Main;

public class CapabilityHandler {
	
	public static final ResourceLocation MANA_CAP = new ResourceLocation(Main.MODID, "mana");

	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event){
		if(!(event.getObject() instanceof EntityPlayer)) return;
		
		event.addCapability(MANA_CAP, new ManaProvider());
	}
}
