package net.minestom.worldgen.features;

import net.minestom.server.utils.BlockPosition;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.futures.GenerationFuture;
import net.minestom.worldgenUtils.Batch;
import net.minestom.worldgenUtils.ChunkPos;
import net.minestom.worldgenUtils.SimpleBlockData;
import net.minestom.worldgenUtils.SimpleBlockPosition;

import java.util.ArrayList;
import java.util.HashMap;

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

	public boolean place(WorldGen wg, int x, int y, int z, int chunkX, int chunkZ) {
		boolean fits = fits(x, y, z);
		if (fits) {
			place0(wg, x, y, z, chunkX, chunkZ);
		} else {
			GenerationFuture future = new GenerationFuture(wg, getPersistentId());
			future.setAll(x, y, z, chunkX, chunkZ);

			final HashMap<ChunkPos, HashMap<SimpleBlockPosition, SimpleBlockData>> data = generate(x, z, chunkX, chunkZ).getData();
			future.setNeededChunks(new ArrayList<>(data.keySet()));
			if (!future.runFuture()) {
				wg.getFutureManager().putFuture(future);
			}
		}

		return fits;
	}

	public void place0(WorldGen wg, int x, int y, int z, int chunkX, int chunkZ) {
		Batch batch = generate(x, z, chunkX, chunkZ);
		batch.apply(wg, new BlockPosition(x + chunkX * 16, y, z + chunkZ * 16));
	}

	private Batch generate(int rX, int rZ, int chunkX, int chunkZ) {
		ChunkRandom rng = new ChunkRandom(342354, 45452);
		rng.initChunkSeed(chunkX* 16L + rX, chunkZ* 16L + rZ);
		Batch batch = new Batch();
		build(batch, rng);
		return batch;
	}

	public abstract void build(Batch batch, ChunkRandom rng);

	public abstract String getPersistentId();

	public float getChance() {
		return chance;
	}

}
