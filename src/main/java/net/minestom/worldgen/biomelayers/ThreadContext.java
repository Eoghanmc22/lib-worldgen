package net.minestom.worldgen.biomelayers;

public final class ThreadContext {

	public final BiomeCache[] cache;

	public ThreadContext(final int layers) {
		cache = new BiomeCache[layers];
		for (int i = 0; i < layers; i++) {
			cache[i] = new BiomeCache();
		}
	}

	public final static class BiomeCache {
		final BiomeCacheEntry[][] cache = new BiomeCacheEntry[16][16];

		public BiomeCache() {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					cache[x][z] = new BiomeCacheEntry();
				}
			}
		}
	}

	public static final class BiomeCacheEntry {

		boolean uninitialized = true;
		int x, z;
		int biome;

		public final boolean isValid(final int x, final int z) {
			return !uninitialized && this.x == x && this.z == z;
		}

		public final int get() {
			return biome;
		}

		public final int set(final int x, final int z, final int biome) {
			uninitialized = false;
			this.x = x;
			this.z = z;
			this.biome = biome;
			return biome;
		}

	}
}
