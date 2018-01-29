package net.oaster2000.newmod.blocks;

import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.tileentity.TileEntityWire;

public class WireBlock extends BlockContainer {

	public static final PropertyEnum Connection = PropertyEnum.create("connection", EnumConnection.class);
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public WireBlock(String unlocalizedName, Material material, float hardness, float resistance) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(unlocalizedName);
		this.setCreativeTab(Main.creativeTab);
		this.setHardness(hardness);
		this.setResistance(resistance);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(Connection,
				EnumConnection.NONE));
	}

	public WireBlock(String unlocalizedName, float hardness, float resistance) {
		this(unlocalizedName, Material.CIRCUITS, hardness, resistance);
	}

	public WireBlock(String unlocalizedName) {
		this(unlocalizedName, 2.0f, 10.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityWire();
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		System.out.println(pos + "");
	}

	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return super.getActualState(state, worldIn, pos).withProperty(Connection, EnumConnection.NONE);
	}

	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(Connection, EnumConnection.NONE);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, Connection});
	}

	public static enum EnumConnection implements IStringSerializable {
		NONE, U, D, N, S, E, W, ALL;

		private EnumConnection() {
		}

		public String getName() {
			return name().toLowerCase();
		}
	}

}
