package net.oaster2000.newmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.oaster2000.newmod.Main;

public class StrongBlock extends Block {

    public StrongBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setRegistryName(unlocalizedName);
		this.setCreativeTab(Main.creativeTab);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setHarvestLevel("pickaxe", 3);
    }

    public StrongBlock(String unlocalizedName, float hardness, float resistance) {
        this(unlocalizedName, Material.ROCK, hardness, resistance);
    }

    public StrongBlock(String unlocalizedName) {
        this(unlocalizedName, 5.0f, 30.0f);
    }
}