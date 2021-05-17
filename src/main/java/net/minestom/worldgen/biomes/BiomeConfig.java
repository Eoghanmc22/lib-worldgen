package net.minestom.worldgen.biomes;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.features.PlaceableFeature;
import net.minestom.worldgen.utils.MutLong;

public abstract class BiomeConfig {

	protected final WorldGen wg;
	private final Biome minestomBiome;
	private final int variants;
	private final PlaceableFeature[] features;
	private int biomeId = -1;
	private int climateId = -1;
	protected long seed;

	protected BiomeConfig(WorldGen wg, Biome minestomBiome, int variants, int salt, PlaceableFeature... features) {
		this.wg = wg;
		this.seed = ChunkRandom.scramble(salt, wg.getSeed());
		this.minestomBiome = minestomBiome;
		this.variants = variants;
		this.features = features;
		MinecraftServer.getBiomeManager().addBiome(minestomBiome);
	}

	public Biome getMinestomBiome() {
		return minestomBiome;
	}

	public abstract int getHeight(int x, int z, int biomeId, MutLong data);

	public static final int GENERATE_STRUCTURES = 0b0000_0000_0000_0000_0000_0000_0000_0001;

	public abstract int generate(ChunkBatch batch, int x, int z, int height, int chunkX, int chunkZ, int biomeId, ChunkRandom rng, MutLong data, int genStructures);

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

	public WorldGen getWg() {
		return wg;
	}
}
