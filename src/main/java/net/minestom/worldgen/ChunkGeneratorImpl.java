package net.minestom.worldgen;

import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.world.biomes.Biome;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.features.PlaceableFeature;
import net.minestom.worldgen.layers.Layer;
import net.minestom.worldgen.layers.ThreadContext;
import net.minestom.worldgen.utils.MutLong;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChunkGeneratorImpl implements ChunkGenerator {

	private final WorldGen wg;
	private final ThreadLocal<ThreadContext> tl;
	public static final AtomicInteger counter = new AtomicInteger(0);

	public ChunkGeneratorImpl(WorldGen wg) {
		this.wg = wg;
		tl = ThreadLocal.withInitial(() -> new ThreadContext(wg.getLayers().size()));
	}

	@Override
	public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
		counter.getAndIncrement();
		ChunkRandom rng = new ChunkRandom(2200, 0);
		rng.initChunkSeed(chunkX, chunkZ);
		int[][] idArray = new int[16+2+2][16+2+2];
		int[][] heightArray = new int[16+2+2][16+2+2];
		BiomeConfig[][] biomeArray = new BiomeConfig[16+2+2][16+2+2];
		MutLong[][] dataArray = new MutLong[16+2+2][16+2+2];
		int offset;

		int realX = chunkX*16;
		int realZ = chunkZ*16;

		final ThreadContext threadContext = tl.get();
		final Layer biomes = wg.getLayers().getLast();

		// cache data
		offset = -2;
		for (int x = 0; x < 20; x++) {
			for (int z = 0; z < 20; z++) {
				int id = biomes.genBiomes(realX+x+offset, realZ+z+offset, threadContext);
				BiomeConfig biome = wg.getBiomeGroup(Layer.getClimate(id)).getBiome(Layer.getBiomeId(id));
				MutLong data = new MutLong();
				int height = biome.getHeight(realX+x+offset, realZ+z+offset, id, data);

				idArray[x][z] = id;
				heightArray[x][z] = height;
				biomeArray[x][z] = biome;
				dataArray[x][z] = data;
			}
		}

		// build chunk
		offset = 2;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int id = idArray[x+offset][z+offset];
				BiomeConfig biome = biomeArray[x+offset][z+offset];
				MutLong data = dataArray[x+offset][z+offset];

				// Bilinear interpolation https://en.wikipedia.org/wiki/Bilinear_interpolation
				int height11 = heightArray[x+offset-2][z+offset-2];
				int height12 = heightArray[x+offset-1][z+offset-2];
				int height13 = heightArray[x+offset+1][z+offset-2];
				int height14 = heightArray[x+offset+2][z+offset-2];

				int height21 = heightArray[x+offset-2][z+offset-1];
				int height22 = heightArray[x+offset-1][z+offset-1];
				int height23 = heightArray[x+offset+1][z+offset-1];
				int height24 = heightArray[x+offset+2][z+offset-1];

				int height31 = heightArray[x+offset-2][z+offset+1];
				int height32 = heightArray[x+offset-1][z+offset+1];
				int height33 = heightArray[x+offset+1][z+offset+1];
				int height34 = heightArray[x+offset+2][z+offset+1];

				int height41 = heightArray[x+offset-2][z+offset+2];
				int height42 = heightArray[x+offset-1][z+offset+2];
				int height43 = heightArray[x+offset+1][z+offset+2];
				int height44 = heightArray[x+offset+2][z+offset+2];

				int height = (int) ((height11/4d + height12/4d + height13/4d + height14/4d)/4d +
						(height21/4d + height22/4d + height23/4d + height24/4d)/4d +
						(height31/4d + height32/4d + height33/4d + height34/4d)/4d +
						(height41/4d + height42/4d + height43/4d + height44/4d)/4d);
				biome.generate(batch, x, z, height, chunkX, chunkZ, id, rng, data);

				for (final PlaceableFeature feature : biome.getFeatures()) {
					if (rng.nextFloat() < feature.getChance()) {
						int finalX = x;
						int finalZ = z;
						ChunkBatch.BLOCK_BATCH_POOL.execute(() -> feature.place(wg, finalX, height+1, finalZ, chunkX, chunkZ));
					}
				}
			}
		}
		
		// Todo add caves
	}

	@Override
	public void fillBiomes(@NotNull Biome[] biomes, int chunkX, int chunkZ) {
		Arrays.fill(biomes, Biome.PLAINS);
	}

	@Nullable
	@Override
	public List<ChunkPopulator> getPopulators() {
		return null;
	}

}
