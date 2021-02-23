package net.minestom.worldgen;

import net.minestom.server.event.instance.InstanceChunkLoadEvent;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.Instance;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.biomes.BiomeGroup;
import net.minestom.worldgen.features.PlaceableFeature;
import net.minestom.worldgen.futures.FutureManager;
import net.minestom.worldgen.layers.Layer;
import net.minestom.worldgen.utils.ChunkPosition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WorldGen {

	private final LinkedList<Layer> layers = new LinkedList<>();
	private final List<BiomeGroup> biomeGroups = new ArrayList<>();
	private final Instance instance;
	private final FutureManager futureManager = new FutureManager(this);

	public WorldGen(Instance instance, WorldGenConfig config) {
		this.instance = instance;
		config.addLayers(this);
		config.addBiomes(this);
		for (final BiomeGroup bg : biomeGroups) {
			for (final BiomeConfig bc : bg.getBiomes()) {
				for (final PlaceableFeature feature : bc.getFeatures()) {
					getFutureManager().registerGeneratable(feature.getPersistentId(), (future, wg, rX, rY, rZ, chunkX, chunkZ) -> {
						feature.place0(wg, rX, rY, rZ, chunkX, chunkZ);
						future.done();
					});
				}
			}
		}
		instance.addEventCallback(InstanceChunkLoadEvent.class, event -> {
			getFutureManager().runFuturesForChunk(new ChunkPosition(event.getChunkX(), event.getChunkZ()));
		});
	}

	public ChunkGenerator getChunkGenerator() {
		return new ChunkGeneratorImpl(this);
	}

	public void addBiomeGroup(BiomeGroup biomeGroup) {
		biomeGroups.add(biomeGroup);
	}

	public void addLayer(Layer layer) {
		layer.setWorldGen(this);
		layers.add(layer);
	}

	public LinkedList<Layer> getLayers() {
		return layers;
	}

	public List<BiomeGroup> getBiomeGroups() {
		return biomeGroups;
	}

	public Instance getInstance() {
		return instance;
	}

	public FutureManager getFutureManager() {
		return futureManager;
	}

}
