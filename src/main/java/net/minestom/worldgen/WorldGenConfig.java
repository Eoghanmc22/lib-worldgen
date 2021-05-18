package net.minestom.worldgen;

public interface WorldGenConfig {

	void addBiomeLayers(WorldGen wg);
	void addHeightMapLayers(WorldGen wg);
	void addTerrainLayers(WorldGen wg);

	void addBiomes(WorldGen wg);
}
