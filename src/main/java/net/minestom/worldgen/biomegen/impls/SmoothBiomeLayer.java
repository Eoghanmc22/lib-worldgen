package net.minestom.worldgen.biomegen.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomegen.MultiSamplingBiomeLayer;

public class SmoothBiomeLayer extends MultiSamplingBiomeLayer {

	public SmoothBiomeLayer(final WorldGen wg, final long baseSeed) {
		super(wg, baseSeed);
	}

	@Override
	protected final int adjustX(final int x) {
		return x - 1;
	}

	@Override
	protected final int adjustZ(final int z) {
		return z - 1;
	}

	@Override
	public int genBiomes(final int x, final int z, final int n, final int s, final int e, final int w, final int c, final ChunkRandom rng) {
		int biome = c;
		rng.initChunkSeed(x, z);
		if (n == s && e == w) biome = rng.randomOf2(n, e);
		else if (n == s) biome = n;
		else if (e == w) biome = e;
		return biome;
	}

}
