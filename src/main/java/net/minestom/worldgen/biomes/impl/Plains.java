package net.minestom.worldgen.biomes.impl;

import de.articdive.jnoise.JNoise;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.features.impl.TreeFeature;

public class Plains extends BiomeConfig {

	public Plains() {
		super(Biome.builder().name(NamespaceID.from("test:desert")).build(), 1, new TreeFeature(0.01f, Block.OAK_LEAVES, Block.OAK_LOG));
	}

	JNoise noise = JNoise.newBuilder().openSimplex().setFrequency(0.01).setSeed(0).build();
	JNoise noise2 = JNoise.newBuilder().openSimplex().setFrequency(0.05).setSeed(1).build();
	JNoise flowers = JNoise.newBuilder().openSimplex().setFrequency(0.1).setSeed(2).build();

	@Override
	public int getHeight(int x, int z, int biomeId) {
		return (int) (64 + noise.getNoise(x,z) * 4 + Math.abs(noise2.getNoise(x,z)) * 3);
	}

	@Override
	public void generate(ChunkBatch batch, int x, int z, int height, int biomeId, ChunkRandom rng) {
		for (int i = 0; i < height; i++) {
			batch.setBlock(x, i, z, Block.DIRT);
		}
		batch.setBlock(x, height, z, Block.GRASS_BLOCK);
		if (flowers.getNoise(x,z)+1 < 0.4 && rng.nextFloat() < 0.5) {
			batch.setBlock(x, height+1, z, Block.DANDELION);
		} else if (rng.nextFloat() < 0.04) {
			batch.setBlock(x, height+1, z, Block.GRASS);
		}
	}

}
