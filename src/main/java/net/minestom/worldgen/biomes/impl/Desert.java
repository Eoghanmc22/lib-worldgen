package net.minestom.worldgen.biomes.impl;

import de.articdive.jnoise.JNoise;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.utils.MutLong;

public class Desert extends BiomeConfig {

	public Desert() {
		super(Biome.builder().name(NamespaceID.from("test:desert")).build(), 1);
	}

	JNoise noise = JNoise.newBuilder().openSimplex().setFrequency(0.02).setSeed(0).build();
	JNoise noise2 = JNoise.newBuilder().openSimplex().setFrequency(0.5).setSeed(1).build();
	JNoise noise3 = JNoise.newBuilder().openSimplex().setFrequency(0.01).setSeed(2).build();

	@Override
	public int getHeight(int x, int z, int biomeId, MutLong data) {
		return (int) (64 + noise.getNoise(x,z) * 5 + Math.abs(noise2.getNoise(x,z)) * 1.4 + noise3.getNoise(x,z) * 7);
	}

	@Override
	public boolean generate(ChunkBatch batch, int x, int z, int height, int chunkX, int chunkZ, int biomeId, ChunkRandom rng, MutLong data) {
		for (int i = 0; i <= height; i++) {
			batch.setBlock(x, i, z, Block.SAND);
		}
		if (rng.nextFloat() < 0.01) {
			batch.setBlock(x, height+1, z, Block.DEAD_BUSH);
		} else if (rng.nextFloat() < 0.005) {
			for (int i = 1; i < rng.nextInt(4)+1; i++) {
				batch.setBlock(x, height+i, z, Block.CACTUS);
			}
		}
		return true;
	}

}
