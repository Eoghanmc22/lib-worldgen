package net.minestom.worldgen.biomegen;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;

public abstract class MultiSamplingBiomeLayer extends BiomeLayer {

	public MultiSamplingBiomeLayer(final WorldGen wg, final long baseSeed) {
		super(wg, baseSeed);
	}

	protected int adjustX(final int x) {
		return x;
	}

	protected int adjustZ(final int y) {
		return y;
	}

	@Override
	public final int genBiomes(final int x, final int z, final ChunkRandom r, final BiomeThreadContext threadContext) {
		return genBiomes(x,z, parent.genBiomesAndCache(adjustX(x+1), adjustZ(z), threadContext), parent.genBiomesAndCache(adjustX(x-1), adjustZ(z), threadContext),
				parent.genBiomesAndCache(adjustX(x), adjustZ(z+1), threadContext), parent.genBiomesAndCache(adjustX(x), adjustZ(z-1), threadContext), parent.genBiomesAndCache(adjustX(x), adjustZ(z), threadContext), r);
	}

	protected abstract int genBiomes(final int x, final int z, final int n, final int s, final int e, final int w, final int c, final ChunkRandom rng);
}
