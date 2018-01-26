package net.oaster2000.newmod.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.tileentity.TileEntityGenerator;
import net.oaster2000.newmod.tileentity.TileEntityWire;

public class WireBlock extends BlockContainer {

	public WireBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setRegistryName(unlocalizedName);
		this.setCreativeTab(Main.creativeTab);
        this.setHardness(hardness);
        this.setResistance(resistance);
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
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
		System.out.println(pos + "");
    }
	
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
