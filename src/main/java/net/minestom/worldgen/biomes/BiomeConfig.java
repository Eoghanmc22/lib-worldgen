package net.minestom.worldgen.biomes;

import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.features.PlaceableFeature;

public abstract class BiomeConfig {

	private final Biome minestomBiome;
	private final int variants;
	private final PlaceableFeature[] features;

	protected BiomeConfig(Biome minestomBiome, int variants, PlaceableFeature... features) {
		this.minestomBiome = minestomBiome;
		this.variants = variants;
		this.features = features;
	}

	public Biome getMinestomBiome() {
		return minestomBiome;
	}

	public abstract int getHeight(int x, int z, int biomeId);

	public abstract void generate(ChunkBatch batch, int x, int z, int height, int biomeId, ChunkRandom rng);

	public PlaceableFeature[] getFeatures() {
		return features;
	}

	public int getVariants() {
		return variants;
	}

}
