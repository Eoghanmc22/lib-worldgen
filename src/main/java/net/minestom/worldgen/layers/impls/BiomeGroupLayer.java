package net.minestom.worldgen.layers.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.layers.Layer;

public class BiomeGroupLayer extends Layer {

	public BiomeGroupLayer(final long baseSeed) {
		super(baseSeed);
	}

	@Override
	public int genBiomes(final int x, final int z, ChunkRandom r) {
		final int biome = parent.genBiomes(x, z);
		if (!isLand(biome))
			return biome;
		final int biomeCount = worldGen.getBiomeGroups().size();
		r.initChunkSeed(x, z);
		return setClimate(biome, r.nextInt(biomeCount));
	}

}
