package net.minestom.worldgen.biomes.impl;

import de.articdive.jnoise.JNoise;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomes.BiomeConfig;

public class Desert extends BiomeConfig {

	public Desert(WorldGen wg, int salt) {
		super(wg, Biome.builder().name(NamespaceID.from("test:desert")).build(), 1, salt);
	}

	JNoise noise = JNoise.newBuilder().openSimplex().setFrequency(0.02).setSeed((int) (seed + 1)).build();
	JNoise noise2 = JNoise.newBuilder().openSimplex().setFrequency(0.5).setSeed((int) (seed + 2)).build();
	JNoise noise3 = JNoise.newBuilder().openSimplex().setFrequency(0.01).setSeed((int) (seed + 3)).build();

	@Override
	public int getHeight(int x, int z, int biomeId) {
		return (int) (64 + noise.getNoise(x,z) * 5 + Math.abs(noise2.getNoise(x,z)) * 1.4 + noise3.getNoise(x,z) * 7);
	}

}
