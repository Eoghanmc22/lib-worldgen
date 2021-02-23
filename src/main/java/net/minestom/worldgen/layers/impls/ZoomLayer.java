package net.minestom.worldgen.layers.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.layers.Layer;

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
	public int genBiomes(final int x, final int z, ChunkRandom r) {
		int biome = parent.genBiomesAndCache(x >> 1, z >> 1);
		r.initChunkSeed(x >> 1 << 1, z >> 1 << 1);
		final int mode = ((x & 1)<<1) | z & 1;
		switch (mode) {
			case 0b00: {
				return biome;
			}
			case 0b10: {
				int biome1 = parent.genBiomesAndCache((x+1) >> 1, z >> 1);
				return r.randomOf2(biome, biome1);
			}
			case 0b01: {
				int biome1 = parent.genBiomesAndCache(x >> 1, (z+1) >> 1);
				return r.randomOf2(biome, biome1);
			}
			case 0b11: {
				int biome1 = parent.genBiomesAndCache((x+1) >> 1, z >> 1);
				int biome2 = parent.genBiomesAndCache(x >> 1, (z+1) >> 1);
				int biome3 = parent.genBiomesAndCache((x+1) >> 1, (z+1) >> 1);
				return fuzzy ? r.getRandomInArray(biome, biome1, biome2, biome3) : r.getRandomOf4(biome, biome1, biome2, biome3);
			}
		}
		return biome;
	}

}
