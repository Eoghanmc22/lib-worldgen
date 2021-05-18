package net.minestom.worldgen.biomegen.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomes.BiomeGroup;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.biomegen.BiomeLayer;
import net.minestom.worldgen.biomegen.BiomeThreadContext;

import java.util.List;

public class BiomesBiomeLayer extends BiomeLayer {

	public BiomesBiomeLayer(final WorldGen wg, final long baseSeed) {
		super(wg, baseSeed);
	}

	@Override
	public int genBiomes(final int x, final int z, final ChunkRandom r, final BiomeThreadContext threadContext) {
		final int biome = parent.genBiomes(x, z, threadContext);
		if (!isLand(biome))
			return biome;
		final List<BiomeGroup> biomeGroups = wg.getBiomeGroups();
		r.initChunkSeed(x, z);
		final List<BiomeConfig> biomes = biomeGroups.get(BiomeLayer.getClimate(biome)).getBiomes();
		return BiomeLayer.setBiomeId(biome, r.nextInt(biomes.size()));
	}

}
