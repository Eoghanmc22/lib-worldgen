package net.minestom.worldgen.layers;

import net.minestom.worldgen.ChunkRandom;

public abstract class MultiSamplingLayer extends Layer {

	public MultiSamplingLayer(long baseSeed) {
		super(baseSeed);
	}

	protected int adjustX(int x) {
		return x;
	}

	protected int adjustZ(int y) {
		return y;
	}

	@Override
	public final int genBiomes(int x, int z, ChunkRandom r) {
		return genBiomes(x,z, parent.genBiomesAndCache(adjustX(x+1), adjustZ(z)), parent.genBiomesAndCache(adjustX(x-1), adjustZ(z)),
				parent.genBiomesAndCache(adjustX(x), adjustZ(z+1)), parent.genBiomesAndCache(adjustX(x), adjustZ(z-1)), parent.genBiomesAndCache(adjustX(x), adjustZ(z)), r);
	}

	protected abstract int genBiomes(int x, int z, int u, int d, int r, int l, int c, ChunkRandom rng);
}
