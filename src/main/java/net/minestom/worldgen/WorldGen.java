package net.minestom.worldgen;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.instance.InstanceChunkLoadEvent;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.worldgen.biomegen.impls.BaseBiomeLayer;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.biomes.BiomeGroup;
import net.minestom.worldgen.features.PlaceableFeature;
import net.minestom.worldgen.futures.FutureManager;
import net.minestom.worldgen.biomegen.BiomeLayer;
import net.minestom.worldgen.heightmap.HeightMapLayer;
import net.minestom.worldgen.heightmap.impls.BaseHeightMapLayer;
import net.minestom.worldgen.terrain.TerrainLayer;
import net.minestom.worldgen.terrain.impls.BaseTerrainLayer;
import net.minestom.worldgenUtils.ChunkPos;
import net.minestom.worldgenUtils.Context;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// biome gen -> height map gen -> terrain gen -> lazy population
public class WorldGen implements Context {
	private final LinkedList<BiomeLayer> biomeLayers = new LinkedList<>();
	private final LinkedList<HeightMapLayer> heightMapLayers = new LinkedList<>();
	private final LinkedList<TerrainLayer> terrainLayers = new LinkedList<>();
	private final List<BiomeGroup> biomeGroups = new ArrayList<>();
	private final BiomeGroup reservedGroup = new BiomeGroup();
	private final InstanceContainer instance;
	private final Random rng = new Random();
	private final FutureManager futureManager = new FutureManager(this);
	private final AtomicInteger biomeLayerIdCounter = new AtomicInteger(0);
	private final AtomicInteger heightMapLayerIdCounter = new AtomicInteger(0);
	private final Map<Class<? extends BiomeConfig>, BiomeConfig> biomeMap = new ConcurrentHashMap<>();
	private final long seed;

	public WorldGen(InstanceContainer instance, WorldGenConfig config, long seed) {
		this.instance = instance;
		this.seed = ChunkRandom.scramble(seed);

		biomeLayers.add(new BaseBiomeLayer(this));
		heightMapLayers.add(new BaseHeightMapLayer(this));
		terrainLayers.add(new BaseTerrainLayer(this));

		config.addBiomes(this);
		config.addBiomeLayers(this);
		config.addHeightMapLayers(this);
		config.addTerrainLayers(this);
		for (final BiomeGroup bg : biomeGroups) {
			for (final BiomeConfig bc : bg.getBiomes()) {
				for (final PlaceableFeature feature : bc.getFeatures()) {
					getFutureManager().registerGeneratable(feature.getPersistentId(), (future, wg, rX, rY, rZ, chunkX, chunkZ, biomeId) -> {
						feature.place0(wg, rX, rY, rZ, chunkX, chunkZ, biomeId);
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
		biome.setClimateId(BiomeLayer.reservedClimate);
		reservedGroup.addBiome(biome);
		biomeMap.put(biome.getClass(), biome);
	}

	public void addBiomeLayer(BiomeLayer biomeLayer) {
		biomeLayers.add(biomeLayer);
	}

	public void addHeightLayer(HeightMapLayer heightMapLayer) {
		heightMapLayers.add(heightMapLayer);
	}

	public void addTerrainLayer(TerrainLayer terrainLayer) {
		terrainLayers.add(terrainLayer);
	}

	public LinkedList<BiomeLayer> getBiomeLayers() {
		return biomeLayers;
	}

	public LinkedList<HeightMapLayer> getHeightMapLayers() {
		return heightMapLayers;
	}

	public LinkedList<TerrainLayer> getTerrainLayers() {
		return terrainLayers;
	}

	public List<BiomeGroup> getBiomeGroups() {
		return biomeGroups;
	}

	public BiomeGroup getBiomeGroup(int climate) {
		return climate != BiomeLayer.reservedClimate ? biomeGroups.get(climate) : reservedGroup;
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

	public AtomicInteger getBiomeLayerIdCounter() {
		return biomeLayerIdCounter;
	}
	public AtomicInteger getHeightMapLayerIdCounter() {
		return heightMapLayerIdCounter;
	}

	public long getSeed() {
		return seed;
	}
}
