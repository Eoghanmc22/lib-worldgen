package net.minestom.worldgen;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.instance.InstanceChunkLoadEvent;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.biomes.BiomeGroup;
import net.minestom.worldgen.features.PlaceableFeature;
import net.minestom.worldgen.futures.FutureManager;
import net.minestom.worldgen.layers.Layer;
import net.minestom.worldgenUtils.ChunkPos;
import net.minestom.worldgenUtils.Context;

import java.util.*;

public class WorldGen implements Context {

	private final LinkedList<Layer> layers = new LinkedList<>();
	private final List<BiomeGroup> biomeGroups = new ArrayList<>();
	private final BiomeGroup reservedGroup = new BiomeGroup();
	private final InstanceContainer instance;
	private final Random rng = new Random();
	private final FutureManager futureManager = new FutureManager(this);

	public WorldGen(InstanceContainer instance, WorldGenConfig config) {
		this.instance = instance;
		config.addBiomes(this);
		config.addLayers(this);
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
			getFutureManager().runFuturesForChunk(new ChunkPos(event.getChunkX(), event.getChunkZ()));
		});
		MinecraftServer.getSchedulerManager().buildTask(() -> {
			final int i = ChunkGeneratorImpl.counter.getAndSet(0);
			System.out.println(i + " chunks per second");
		}).repeat(1, TimeUnit.SECOND).schedule();
	}

	public ChunkGenerator getChunkGenerator() {
		return new ChunkGeneratorImpl(this);
	}

	public void addBiomeGroup(BiomeGroup biomeGroup) {
		int id = biomeGroups.size();
		for (final BiomeConfig cfg : biomeGroup.getBiomes()) {
			cfg.setClimateId(id);
		}
		biomeGroups.add(biomeGroup);
	}

	public void addReservedBiome(BiomeConfig biome) {
		biome.setClimateId(Layer.reservedClimate);
		reservedGroup.addBiome(biome);
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

	public BiomeGroup getBiomeGroup(int climate) {
		return climate != Layer.reservedClimate ? biomeGroups.get(climate) : reservedGroup;
	}

	public InstanceContainer getInstance() {
		return instance;
	}

	@Override
	public Random getRNG() {
		return rng;
	}

	public FutureManager getFutureManager() {
		return futureManager;
	}

	public BiomeGroup getReservedGroup() {
		return reservedGroup;
	}

}
