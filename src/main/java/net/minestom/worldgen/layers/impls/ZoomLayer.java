package net.minestom.worldgen.layers.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.layers.Layer;
import net.minestom.worldgen.layers.ThreadContext;

public class ZoomLayer extends Layer {

	final boolean fuzzy;

	public ZoomLayer(final long baseSeed) {
		super(baseSeed);
		this.fuzzy = false;
	}

	public ZoomLayer(final long baseSeed, final boolean fuzzy) {
		super(baseSeed);
		this.fuzzy = fuzzy;
	}

	@Override
	public int genBiomes(final int x, final int z, ChunkRandom r, ThreadContext threadContext) {
		int biome = parent.genBiomesAndCache(x >> 1, z >> 1, threadContext);
		r.initChunkSeed(x >> 1 << 1, z >> 1 << 1);
		final int mode = ((x & 1)<<1) | z & 1;
		switch (mode) {
			case 0b00: {
				return biome;
			}
			case 0b10: {
				int biome1 = parent.genBiomesAndCache((x+1) >> 1, z >> 1, threadContext);
				return r.randomOf2(biome, biome1);
			}
			case 0b01: {
				int biome1 = parent.genBiomesAndCache(x >> 1, (z+1) >> 1, threadContext);
				return r.randomOf2(biome, biome1);
			}
			case 0b11: {
				int biome1 = parent.genBiomesAndCache((x+1) >> 1, z >> 1, threadContext);
				int biome2 = parent.genBiomesAndCache(x >> 1, (z+1) >> 1, threadContext);
				int biome3 = parent.genBiomesAndCache((x+1) >> 1, (z+1) >> 1, threadContext);
				return fuzzy ? r.getRandomInArray(biome, biome1, biome2, biome3) : r.getRandomOf4(biome, biome1, biome2, biome3);
			}
		}
		return biome;
	}

}
