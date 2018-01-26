package net.oaster2000.newmod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.oaster2000.newmod.Main;
import net.oaster2000.newmod.items.ModItems;

public class NetherOreBlock extends Block {
	
    public NetherOreBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setRegistryName(unlocalizedName);
		this.setCreativeTab(Main.creativeTab);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setHarvestLevel("pickaxe", 2);
    }

    public NetherOreBlock(String unlocalizedName, float hardness, float resistance) {
        this(unlocalizedName, Material.ROCK, hardness, resistance);
    }

    public NetherOreBlock(String unlocalizedName) {
        this(unlocalizedName, 2.0f, 10.0f);
    }
    
    @Override
    public Item getItemDropped(IBlockState blockstate, Random random, int fortune){
    	return ModItems.dStone;
    }
    
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
    	return random.nextInt(6 + (fortune * 2));
    }
    
}