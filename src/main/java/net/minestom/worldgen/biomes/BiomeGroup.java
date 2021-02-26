package net.minestom.worldgen.biomes;

import java.util.ArrayList;
import java.util.List;

public class BiomeGroup {
	private final List<BiomeConfig> biomes = new ArrayList<>();

	public void addBiome(BiomeConfig biome) {
		biome.setBiomeId(biomes.size());
		biomes.add(biome);
	}

	public List<BiomeConfig> getBiomes() {
		return biomes;
	}

	public BiomeConfig getBiome(int biome) {
		return biomes.get(biome);
	}

}
