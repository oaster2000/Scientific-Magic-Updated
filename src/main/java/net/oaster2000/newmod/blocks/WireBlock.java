package net.oaster2000.newmod.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.tileentity.TileEntityWire;

public class WireBlock extends BlockContainer {

	public static final PropertyEnum Connection = PropertyEnum.create("connection", EnumConnection.class);

	public WireBlock(String unlocalizedName, Material material, float hardness, float resistance) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(unlocalizedName);
		this.setCreativeTab(Main.creativeTab);
		this.setHardness(hardness);
		this.setResistance(resistance);
		setDefaultState(this.blockState.getBaseState().withProperty(Connection, EnumConnection.NONE));
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
		EnumConnection connection = EnumConnection.NONE;
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityWire) {
			TileEntityWire wireEntity = (TileEntityWire) te;
			String isDevice = wireEntity.isNeighborDevice(Minecraft.getMinecraft().world, pos.getX(), pos.getY(), pos.getZ());
			if (!isDevice.equals("none")) {
				switch (isDevice) {
				case "north":
					 connection = EnumConnection.N;
					 break;
				case "south":
					 connection = EnumConnection.S;
					 break;
				case "east":
					 connection = EnumConnection.E;
					 break;
				case "west":
					 connection = EnumConnection.W;
					 break;
				case "up":
					 connection = EnumConnection.U;
					 break;
				case "down":
					 connection = EnumConnection.D;
					 break;
				case "updown":
					connection = EnumConnection.UD;
					break;
				case "upnorth":
					connection = EnumConnection.UN;
					break;
				case "upsouth":
					connection = EnumConnection.US;
					break;
				case "upeast":
					connection = EnumConnection.UE;
					break;
				case "upwest":
					connection = EnumConnection.UW;
					break;
				case "upnorthsouth":
					connection = EnumConnection.UNS;
					break;
				case "upeastwest":
					connection = EnumConnection.UEW;
					break;
				case "upnortheast":
					connection = EnumConnection.UNE;
					break;
				case "upnorthwest":
					connection = EnumConnection.UNW;
					break;
				case "upsoutheast":
					connection = EnumConnection.USE;
					break;
				case "upsouthwest":
					connection = EnumConnection.USW;
					break;
				}
			}
		}
		
		return state.withProperty(Connection, connection);
	}

	public IBlockState getStateFromMeta(int meta) {
		return super.getStateFromMeta(meta).withProperty(Connection, EnumConnection.NONE);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { Connection });
	}	
	public int getMetaFromState(IBlockState state)
    {
		return 0;
    }

	public static enum EnumConnection implements IStringSerializable {
		NONE, U, D, N, S, E, W, UD, UN, US, UE, UW, UNS, UEW, UNE, UNW, USE, USW, ALL;

		private EnumConnection() {
		}

		public String getName() {
			return name().toLowerCase();
		}
	}

}
