package net.minestom.worldgen;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.instance.InstanceChunkLoadEvent;
import net.minestom.server.instance.ChunkGenerator;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WorldGen implements Context {

	private final LinkedList<Layer> layers = new LinkedList<>();
	private final List<BiomeGroup> biomeGroups = new ArrayList<>();
	private final BiomeGroup reservedGroup = new BiomeGroup();
	private final InstanceContainer instance;
	private final Random rng = new Random();
	private final FutureManager futureManager = new FutureManager(this);
	private final AtomicInteger layerIdCounter = new AtomicInteger(0);
	private final Map<Class<? extends BiomeConfig>, BiomeConfig> biomeMap = new ConcurrentHashMap<>();
	private final long seed;

	public WorldGen(InstanceContainer instance, WorldGenConfig config, long seed) {
		this.instance = instance;
		this.seed = ChunkRandom.scramble(seed);
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
			biomeMap.put(cfg.getClass(), cfg);
		}
		biomeGroups.add(biomeGroup);
	}

	public void addReservedBiome(BiomeConfig biome) {
		biome.setClimateId(Layer.reservedClimate);
		reservedGroup.addBiome(biome);
		biomeMap.put(biome.getClass(), biome);
	}

	public void addLayer(Layer layer) {
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

	public BiomeConfig getBiome(Class<? extends BiomeConfig> clazz) {
		return biomeMap.get(clazz);
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

	public AtomicInteger getLayerIdCounter() {
		return layerIdCounter;
	}

	public long getSeed() {
		return seed;
	}
}
