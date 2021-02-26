package net.minestom.worldgen.biomes;

import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.features.PlaceableFeature;

public abstract class BiomeConfig {

	private final Biome minestomBiome;
	private final int variants;
	private final PlaceableFeature[] features;
	private int biomeId = -1;
	private int climateId = -1;

	protected BiomeConfig(Biome minestomBiome, int variants, PlaceableFeature... features) {
		this.minestomBiome = minestomBiome;
		this.variants = variants;
		this.features = features;
	}

	public Biome getMinestomBiome() {
		return minestomBiome;
	}

	public abstract int getHeight(int x, int z, int biomeId);

	public abstract void generate(ChunkBatch batch, int x, int z, int height, int chunkX, int chunkZ, int biomeId, ChunkRandom rng);

	public PlaceableFeature[] getFeatures() {
		return features;
	}

	public int getVariants() {
		return variants;
	}

	public int getBiomeId() {
		return biomeId;
	}

	public void setBiomeId(int biomeId) {
		this.biomeId = biomeId;
	}

	public int getClimateId() {
		return climateId;
	}

	public void setClimateId(int climateId) {
		this.climateId = climateId;
	}

}
