package net.minestom.worldgen.terrain;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.utils.Column;

public abstract class TerrainLayer {

	protected final TerrainLayer parent;
	protected final WorldGen wg;

	//Seeds for randoms
	private final long baseSeed;
	private long worldSeed;

	public TerrainLayer(final WorldGen wg, final long baseSeed) {
		this.baseSeed = baseSeed;

		this.wg = wg;
		initWorldGenSeed(wg.getSeed());
		if (wg.getTerrainLayers().size() > 0)
			this.parent = wg.getTerrainLayers().getLast();
		else
			this.parent = null;
	}

	public final int genTerrain(final int x, final int z, final int genStructures, final Column column, final TerrainThreadContext threadContext) {
		return genTerrain(x, z, genStructures, column, new ChunkRandom(baseSeed, worldSeed), threadContext);
	}

	protected abstract int genTerrain(int x, int z, final int genStructures, final Column column, final ChunkRandom r, final TerrainThreadContext threadContext);

	private void initWorldGenSeed(final long worldSeed) {
		if (parent != null)
			parent.initWorldGenSeed(worldSeed);

		this.worldSeed = worldSeed;
	}

}
