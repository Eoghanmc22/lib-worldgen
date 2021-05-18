package net.minestom.worldgen.heightmap;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;

public abstract class HeightMapLayer {

	private final int id;

	protected final HeightMapLayer parent;
	protected final WorldGen wg;

	//Seeds for randoms
	private final long baseSeed;
	private long worldSeed;

	public HeightMapLayer(final WorldGen wg, final long baseSeed) {
		this.baseSeed = baseSeed;

		this.wg = wg;
		initWorldGenSeed(wg.getSeed());
		id = wg.getHeightMapLayerIdCounter().getAndIncrement();
		if (wg.getHeightMapLayers().size() > 0)
			this.parent = wg.getHeightMapLayers().getLast();
		else
			this.parent = null;
	}

	public final double genHeightAndCache(final int x, final int z, final HeightMapThreadContext threadContext) {
		final HeightMapThreadContext.HeightMapCache cache = threadContext.cache[id];
		final HeightMapThreadContext.HeightMapCacheEntry entry = cache.cache[x & 0xF][z & 0xF];

		if (entry.isValid(x, z)) {
			return entry.get();
		}
		return entry.set(x, z, genHeight(x, z, threadContext));
	}

	public final double genHeight(final int x, final int z, final HeightMapThreadContext threadContext) {
		return genHeight(x, z, new ChunkRandom(baseSeed, worldSeed), threadContext);
	}

	protected abstract double genHeight(final int x, final int z, final ChunkRandom r, final HeightMapThreadContext threadContext);

	private void initWorldGenSeed(final long worldSeed) {
		if (parent != null)
			parent.initWorldGenSeed(worldSeed);

		this.worldSeed = worldSeed;
	}

}
