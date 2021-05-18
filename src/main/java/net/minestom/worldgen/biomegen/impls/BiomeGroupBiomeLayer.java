package net.minestom.worldgen.biomegen.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomegen.BiomeLayer;
import net.minestom.worldgen.biomegen.BiomeThreadContext;

public class BiomeGroupBiomeLayer extends BiomeLayer {

	public BiomeGroupBiomeLayer(final WorldGen wg, final long baseSeed) {
		super(wg, baseSeed);
	}

	@Override
	public int genBiomes(final int x, final int z, final ChunkRandom r, final BiomeThreadContext threadContext) {
		final int biome = parent.genBiomes(x, z, threadContext);
		if (!isLand(biome))
			return biome;
		final int biomeCount = wg.getBiomeGroups().size();
		r.initChunkSeed(x, z);
		return setClimate(biome, r.nextInt(biomeCount));
	}

}
