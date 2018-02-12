package net.oaster2000.newmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.oaster2000.newmod.capability.CapabilityHandler;
import net.oaster2000.newmod.capability.IMana;
import net.oaster2000.newmod.capability.Mana;
import net.oaster2000.newmod.capability.ManaEventHandler;
import net.oaster2000.newmod.capability.ManaStorage;
import net.oaster2000.newmod.handler.ItemCraftingHandler;
import net.oaster2000.newmod.handler.ResearchUpdateHandler;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e){
		
	}
	
	public void Init(FMLInitializationEvent e){
		CapabilityManager.INSTANCE.register(IMana.class, new ManaStorage(), Mana.class);
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new ManaEventHandler());
		MinecraftForge.EVENT_BUS.register(new ItemCraftingHandler());
		MinecraftForge.EVENT_BUS.register(new ResearchUpdateHandler());
	
	}
	
	public void postInit(FMLPostInitializationEvent e){
		
	}
	
}
