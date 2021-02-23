package net.minestom.worldgen.features;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.batch.BlockBatch;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.futures.GenerationFuture;
import net.minestom.worldgen.utils.ChunkPosition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class PlaceableFeature {

	private final int xRadius, zRadius;
	private final float chance;

	public PlaceableFeature(int xRadius, int zRadius, float chance) {
		this.xRadius = xRadius;
		this.zRadius = zRadius;
		this.chance = chance;

	}

	public boolean fits(int rX, int rY, int rZ) {
		return !(rX + xRadius >= 16 || rX - xRadius < 0) &&
						!(rZ + zRadius >= 16 || rZ - zRadius < 0);
	}

	public boolean place(WorldGen wg, int rX, int rY, int rZ, int chunkX, int chunkZ) {
		boolean fits = fits(rX, rY, rZ);
		if (fits) {
			place0(wg, rX, rY, rZ, chunkX, chunkZ);
		} else {
			//this needs to be rewritten lol
			GenerationFuture future = new GenerationFuture(wg, getPersistentId());
			future.setAll(rX, rY, rZ, chunkX, chunkZ);

			BlockBatch temp =  generate(wg, rX, rY, rZ, chunkX, chunkZ);
			List<Chunk> chunks = null;
			try {
				final Field data = temp.getClass().getDeclaredField("data");
				data.setAccessible(true);
				final Map<Chunk, Object> o = (Map<Chunk, Object>) data.get(temp);
				chunks = new ArrayList<>(o.keySet());
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
			future.setNeededChunks(chunks.stream().map(ChunkPosition::fromChunk).collect(Collectors.toList()));
			if (!future.runFuture()) {
				wg.getFutureManager().putFuture(future);
			}
		}

		return fits;
	}

	public void place0(WorldGen wg, int rX, int rY, int rZ, int chunkX, int chunkZ) {
		BlockBatch batch = generate(wg, rX, rY, rZ, chunkX, chunkZ);
		batch.flush(null);
	}

	private BlockBatch generate(WorldGen wg, int rX, int rY, int rZ, int chunkX, int chunkZ) {
		ChunkRandom rng = new ChunkRandom(342354, 45452);
		rng.initChunkSeed(chunkX*16 + rX, chunkZ*16 + rZ);
		BlockBatch batch = wg.getInstance().createBlockBatch();
		build(batch, chunkX*16 + rX, rY, chunkZ*16 + rZ, rng);
		return batch;
	}

	public abstract void build(BlockBatch batch, int x, int y, int z, ChunkRandom rng);

	public abstract String getPersistentId();

	public float getChance() {
		return chance;
	}

}
