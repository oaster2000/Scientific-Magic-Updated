package net.oaster2000.newmod.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.oaster2000.newmod.container.ContainerCrucible;
import net.oaster2000.newmod.container.ContainerDeconstructor;
import net.oaster2000.newmod.container.ContainerElectricFurnace;
import net.oaster2000.newmod.container.ContainerGenerator;
import net.oaster2000.newmod.container.ContainerMacerator;
import net.oaster2000.newmod.container.ContainerSolarGenerator;
import net.oaster2000.newmod.gui.GUIObscural;
import net.oaster2000.newmod.gui.GUITome;
import net.oaster2000.newmod.gui.GuiCrucible;
import net.oaster2000.newmod.gui.GuiDeconstructor;
import net.oaster2000.newmod.gui.GuiElectricFurnace;
import net.oaster2000.newmod.gui.GuiGenerator;
import net.oaster2000.newmod.gui.GuiMacerator;
import net.oaster2000.newmod.gui.GuiSolarGenerator;
import net.oaster2000.newmod.tileentity.MaceratorTileEntity;
import net.oaster2000.newmod.tileentity.TileEntityCrucible;
import net.oaster2000.newmod.tileentity.TileEntityDeconstructor;
import net.oaster2000.newmod.tileentity.TileEntityElectricFurnace;
import net.oaster2000.newmod.tileentity.TileEntityGenerator;
import net.oaster2000.newmod.tileentity.TileEntitySolarGenerator;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));

		if (entity != null) {
			switch (ID) {
			case 0:
				if (entity instanceof TileEntityCrucible) {
					return new ContainerCrucible(player.inventory, (TileEntityCrucible) entity);
				}
			case 1:
				if (entity instanceof MaceratorTileEntity) {
					return new ContainerMacerator(player.inventory, (MaceratorTileEntity) entity);
				}
			case 2:
				if (entity instanceof TileEntityElectricFurnace) {
					return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace) entity);
				}
			case 3:
				if (entity instanceof TileEntityGenerator) {
					return new ContainerGenerator(player.inventory, (TileEntityGenerator) entity);
				}
			case 4:
				if (entity instanceof TileEntitySolarGenerator) {
					return new ContainerSolarGenerator(player.inventory, (TileEntitySolarGenerator) entity);
				}
			case 5:
				if (entity instanceof TileEntityDeconstructor) {
					return new ContainerDeconstructor(player.inventory, (TileEntityDeconstructor) entity);
				}
			}
		}
		
		return null;

	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 6) {
			return new GUITome();
		}
		if(ID == 7) {
			return new GUIObscural();
		}
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		if (entity != null) {
			switch (ID) {
			case 0:
				if (entity instanceof TileEntityCrucible) {
					return new GuiCrucible(player.inventory, (TileEntityCrucible) entity);
				}
			case 1:
				if (entity instanceof MaceratorTileEntity) {
					return new GuiMacerator(player.inventory, (MaceratorTileEntity) entity);
				}
			case 2:
				if (entity instanceof TileEntityElectricFurnace) {
					return new GuiElectricFurnace(player.inventory, (TileEntityElectricFurnace) entity);
				}
			case 3:
				if (entity instanceof TileEntityGenerator) {
					return new GuiGenerator(player.inventory, (TileEntityGenerator) entity);
				}
			case 4:
				if (entity instanceof TileEntitySolarGenerator) {
					return new GuiSolarGenerator(player.inventory, (TileEntitySolarGenerator) entity);
				}
			case 5:
				if (entity instanceof TileEntityDeconstructor) {
					return new GuiDeconstructor(player.inventory, (TileEntityDeconstructor) entity);
				}
			}
		}
		return null;
	}

}
