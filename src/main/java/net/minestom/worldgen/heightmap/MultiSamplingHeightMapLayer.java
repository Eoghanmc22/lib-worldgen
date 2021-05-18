package net.minestom.worldgen.heightmap;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;

public abstract class MultiSamplingHeightMapLayer extends HeightMapLayer {

	public MultiSamplingHeightMapLayer(final WorldGen wg, final long baseSeed) {
		super(wg, baseSeed);
	}

	protected int adjustX(final int x) {
		return x;
	}

	protected int adjustZ(final int y) {
		return y;
	}

	@Override
	public final double genHeight(final int x, final int z, final ChunkRandom r, final HeightMapThreadContext threadContext) {
		return genHeight(x,z, parent.genHeightAndCache(adjustX(x+1), adjustZ(z), threadContext), parent.genHeightAndCache(adjustX(x-1), adjustZ(z), threadContext),
				parent.genHeightAndCache(adjustX(x), adjustZ(z+1), threadContext), parent.genHeightAndCache(adjustX(x), adjustZ(z-1), threadContext), parent.genHeightAndCache(adjustX(x), adjustZ(z), threadContext), r);
	}

	protected abstract double genHeight(final int x, final int z, final double n, final double s, final double e, final double w, final double c, final ChunkRandom rng);
}
