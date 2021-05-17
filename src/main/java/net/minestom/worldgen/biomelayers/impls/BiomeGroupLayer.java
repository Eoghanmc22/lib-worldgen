package net.minestom.worldgen.biomelayers.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomelayers.Layer;
import net.minestom.worldgen.biomelayers.ThreadContext;

public class BiomeGroupLayer extends Layer {

	public BiomeGroupLayer(WorldGen wg, final long baseSeed) {
		super(wg, baseSeed);
	}

	@Override
	public int genBiomes(final int x, final int z, ChunkRandom r, ThreadContext threadContext) {
		final int biome = parent.genBiomes(x, z, threadContext);
		if (!isLand(biome))
			return biome;
		final int biomeCount = worldGen.getBiomeGroups().size();
		r.initChunkSeed(x, z);
		return setClimate(biome, r.nextInt(biomeCount));
	}

}
