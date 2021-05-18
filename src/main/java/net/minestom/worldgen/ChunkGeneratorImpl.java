package net.minestom.worldgen;

import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.features.PlaceableFeature;
import net.minestom.worldgen.biomegen.BiomeLayer;
import net.minestom.worldgen.biomegen.BiomeThreadContext;
import net.minestom.worldgen.heightmap.HeightMapThreadContext;
import net.minestom.worldgen.terrain.TerrainLayer;
import net.minestom.worldgen.terrain.TerrainThreadContext;
import net.minestom.worldgen.utils.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChunkGeneratorImpl implements ChunkGenerator {

	private final WorldGen wg;
	private final ThreadLocal<TerrainThreadContext> tl;
	public static final AtomicInteger counter = new AtomicInteger(0);

	public ChunkGeneratorImpl(WorldGen wg) {
		this.wg = wg;
		tl = ThreadLocal.withInitial(() -> new TerrainThreadContext(new HeightMapThreadContext(new BiomeThreadContext(wg.getBiomeLayers().size()), wg.getHeightMapLayers().size())));
	}

	@Override
	public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
		counter.getAndIncrement();
		final ChunkRandom rng = new ChunkRandom(2200, wg.getSeed());
		rng.initChunkSeed(chunkX, chunkZ);

		final int realX = chunkX*16;
		final int realZ = chunkZ*16;

		final TerrainThreadContext threadContext = tl.get();
		final TerrainLayer terrainLayer = wg.getTerrainLayers().getLast();

		// build chunk
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				final Column column = new Column();
				final int genStructures = terrainLayer.genTerrain(realX+x, realZ+z, -1, column, threadContext);
				for (int y = 0; y <= column.getMaxHeight(); y++) {
					batch.setBlockStateId(x, y, z, column.getBlock(y));
				}
				if ((genStructures & BiomeConfig.GENERATE_STRUCTURES) == BiomeConfig.GENERATE_STRUCTURES) {
					for (final PlaceableFeature feature : column.getBiome().getFeatures()) {
						if (rng.nextFloat() < feature.getChance()) {
							final int finalX = x;
							final int finalZ = z;
							ChunkBatch.BLOCK_BATCH_POOL.execute(() -> feature.place(wg, finalX, column.getMaxHeight() + 1, finalZ, chunkX, chunkZ, column.getBiomeId()));
						}
					}
				}
			}
		}
	}

	@Override
	public void fillBiomes(@NotNull Biome[] biomes, int chunkX, int chunkZ) {
		int realX = chunkX*16;
		int realZ = chunkZ*16;
		final BiomeThreadContext threadContext = tl.get().heightMapCtx.biomeCtx;
		final BiomeLayer biomeSource = wg.getBiomeLayers().getLast();
		Biome[][] biomeArray = new Biome[4][4];
		for (int x = 0; x < 4; x++) {
			for (int z = 0; z < 4; z++) {
				int id = biomeSource.genBiomes(realX+x*4, realZ+x*4, threadContext);
				BiomeConfig biome = wg.getBiomeGroup(BiomeLayer.getClimate(id)).getBiome(BiomeLayer.getBiomeId(id));
				biomeArray[x][z] = biome.getMinestomBiome();
			}
		}
		for (int y = 0; y < 64; y++) {
			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 4; x++) {
					biomes[y*16+z*4+x] = biomeArray[x][z];
				}
			}
		}
	}

	@Nullable
	@Override
	public List<ChunkPopulator> getPopulators() {
		return null;
	}

}
