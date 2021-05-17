package net.minestom.worldgen.layers.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.layers.MultiSamplingLayer;

public class SmoothLayer extends MultiSamplingLayer {

	public SmoothLayer(WorldGen wg, final long baseSeed) {
		super(wg, baseSeed);
	}

	@Override
	protected int adjustX(int x) {
		return x - 1;
	}

	@Override
	protected int adjustZ(int z) {
		return z - 1;
	}

	@Override
	public int genBiomes(int x, int z, int u, int d, int r, int l, int c, ChunkRandom rng) {
		int biome = c;
		rng.initChunkSeed(x, z);
		if (u == d && r == l) biome = rng.randomOf2(u, r);
		else if (u == d) biome = u;
		else if (r == l) biome = r;
		return biome;
	}

}
