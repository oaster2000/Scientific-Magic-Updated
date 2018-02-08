package net.oaster2000.newmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.tileentity.TileEntityGenerator;

public class ObscuralBlock extends Block {

	public ObscuralBlock(String unlocalizedName, Material material, float hardness, float resistance) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(unlocalizedName);
		this.setCreativeTab(Main.creativeTab);
		this.setHardness(hardness);
		this.setResistance(resistance);
	}

	public ObscuralBlock(String unlocalizedName, float hardness, float resistance) {
		this(unlocalizedName, Material.CIRCUITS, hardness, resistance);
	}

	public ObscuralBlock(String unlocalizedName) {
		this(unlocalizedName, 2.0f, 10.0f);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		FMLNetworkHandler.openGui(playerIn, Main.instance, 7, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
