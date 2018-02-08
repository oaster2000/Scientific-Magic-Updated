package net.oaster2000.newmod.world;

import java.util.Random;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.oaster2000.newmod.blocks.ModBlocks;

public class NewModWorldGen implements IWorldGenerator{

	private WorldGenerator gen_dStone_ore;
	private WorldGenerator gen_copperOre;
	private WorldGenerator gen_tinOre;
	private WorldGenerator gen_silverOre;
	private WorldGenerator gen_dStone_ore_nether;
	
	public NewModWorldGen(){
		this.gen_dStone_ore = new WorldGenMinable(ModBlocks.dStone_ore.getDefaultState(), 8);
		this.gen_copperOre = new WorldGenMinable(ModBlocks.copperOre.getDefaultState(), 8);
		this.gen_tinOre = new WorldGenMinable(ModBlocks.tinOre.getDefaultState(), 8);
		this.gen_silverOre = new WorldGenMinable(ModBlocks.silverOre.getDefaultState(), 8);
		this.gen_dStone_ore_nether = new WorldGenMinable(ModBlocks.dStone_ore_nether.getDefaultState(), 8, BlockMatcher.forBlock(Blocks.NETHERRACK));
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case 0: // Overworld
			this.runGenerator(gen_dStone_ore, world, random, chunkX, chunkZ, 15, 0, 64);
			this.runGenerator(gen_copperOre, world, random, chunkX, chunkZ, 35, 0, 64);
			this.runGenerator(gen_tinOre, world, random, chunkX, chunkZ, 35, 0, 64);
			this.runGenerator(gen_silverOre, world, random, chunkX, chunkZ, 35, 0, 64);
			break;
		case -1: //Nether
			this.runGenerator(gen_dStone_ore_nether, world, random, chunkX, chunkZ, 15, 0, 64);
			break;
		case 1: //The End

			break;
		}
	}
	
	private void runGenerator(WorldGenerator generator, World world, Random rand, int chunkX, int chunkZ, int chanceToSpawn, int minHeight, int maxHeight){
		if(minHeight < 0 || maxHeight > 256 || minHeight > maxHeight){
			throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");
		}
		
		int heightDiff = maxHeight - minHeight;
		for(int i = 0; i < chanceToSpawn; i++){
			int x = chunkX * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightDiff);
			int z = chunkZ * 16 + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}

}
