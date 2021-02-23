package net.minestom.worldgen.biomes;

import java.util.ArrayList;
import java.util.List;

public class BiomeGroup {
	private final List<BiomeConfig> biomes = new ArrayList<>();

	public void addBiome(BiomeConfig biome) {
		biomes.add(biome);
	}

	public List<BiomeConfig> getBiomes() {
		return biomes;
	}

}
