package net.minestom.worldgen.biomes.impl;

import de.articdive.jnoise.JNoise;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.features.impl.TreeFeature;

public class Plains extends BiomeConfig {

	public Plains(WorldGen wg, int salt) {
		super(wg, Biome.builder().name(NamespaceID.from("test:plains")).build(), 1, salt, new TreeFeature(0.00000001f, Block.OAK_LEAVES, Block.OAK_LOG));
	}

	JNoise noise = JNoise.newBuilder().openSimplex().setFrequency(0.01).setSeed((int) (seed + 1)).build();
	JNoise noise2 = JNoise.newBuilder().openSimplex().setFrequency(0.05).setSeed((int) (seed + 2)).build();
	JNoise flowers = JNoise.newBuilder().openSimplex().setFrequency(0.1).setSeed((int) (seed + 3)).build();

	@Override
	public int getHeight(int x, int z, int biomeId) {
		return (int) (65 + noise.getNoise(x,z) * 4 + Math.abs(noise2.getNoise(x,z)) * 3);
	}

}
