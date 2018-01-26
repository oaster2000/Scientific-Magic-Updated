package net.oaster2000.newmod.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class ManaEventHandler {

	@SubscribeEvent
	public void onDeath(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			IMana mana = event.getOriginal().getCapability(ManaProvider.MANA_CAP, null);
			IMana newMana = event.getEntityPlayer().getCapability(ManaProvider.MANA_CAP, null);

			newMana.set(mana.getMana());
		}
	}

	@SubscribeEvent

	public void onPlayerLogsIn(PlayerLoggedInEvent event)

	{

		EntityPlayer player = event.player;
		
		IMana mana = player.getCapability(ManaProvider.MANA_CAP, null);

		String message = String.format("Hello there, you have §7%d§r mana left.", (int) mana.getMana());

		player.sendMessage(new TextComponentString(message));

	}

	@SubscribeEvent

	public void onPlayerSleep(PlayerSleepInBedEvent event)

	{

		EntityPlayer player = event.getEntityPlayer();

		if (player.world.isRemote)
			return;

		IMana mana = player.getCapability(ManaProvider.MANA_CAP, null);

		mana.fill(50);

		String message = String.format(
				"You refreshed yourself in the bed. You received 50 mana, you have §7%d§r mana left.",
				(int) mana.getMana());

		player.sendMessage(new TextComponentString(message));

	}

	@SubscribeEvent

	public void onPlayerFalls(LivingFallEvent event)

	{

		Entity entity = event.getEntity();

		if (entity.world.isRemote || !(entity instanceof EntityPlayerMP) || event.getDistance() < 3)
			return;

		EntityPlayer player = (EntityPlayer) entity;

		IMana mana = player.getCapability(ManaProvider.MANA_CAP, null);

		float points = mana.getMana();

		float cost = event.getDistance() * 2;

		if (points > cost)

		{

			mana.consume(cost);

			String message = String.format(
					"You absorbed fall damage. It costed §7%d§r mana, you have §7%d§r mana left.", (int) cost,
					(int) mana.getMana());

			player.sendMessage(new TextComponentString(message));

			event.setCanceled(true);

		}

	}

}
