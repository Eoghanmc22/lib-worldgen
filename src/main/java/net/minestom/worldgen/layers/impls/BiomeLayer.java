package net.minestom.worldgen.layers.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.biomes.BiomeGroup;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.layers.Layer;
import net.minestom.worldgen.layers.ThreadContext;

import java.util.List;

public class BiomeLayer extends Layer {

	public BiomeLayer(final long baseSeed) {
		super(baseSeed);
	}

	@Override
	public int genBiomes(final int x, final int z, ChunkRandom r, ThreadContext threadContext) {
		final int biome = parent.genBiomes(x, z, threadContext);
		if (!isLand(biome))
			return biome;
		final List<BiomeGroup> biomeGroups = worldGen.getBiomeGroups();
		r.initChunkSeed(x, z);
		final List<BiomeConfig> biomes = biomeGroups.get(Layer.getClimate(biome)).getBiomes();
		return Layer.setBiomeId(biome, r.nextInt(biomes.size()));
	}

}
