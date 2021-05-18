package net.minestom.worldgen.heightmap;

import net.minestom.worldgen.biomegen.BiomeThreadContext;

public final class HeightMapThreadContext {

	public final BiomeThreadContext biomeCtx;
	public final HeightMapCache[] cache;

	public HeightMapThreadContext(final BiomeThreadContext biomeCtx, final int layers) {
		this.biomeCtx = biomeCtx;
		cache = new HeightMapCache[layers];
		for (int i = 0; i < layers; i++) {
			cache[i] = new HeightMapCache();
		}
	}

	public final static class HeightMapCache {
		final HeightMapCacheEntry[][] cache = new HeightMapCacheEntry[16][16];

		public HeightMapCache() {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					cache[x][z] = new HeightMapCacheEntry();
				}
			}
		}
	}

	public static final class HeightMapCacheEntry {

		boolean uninitialized = true;
		int x, z;
		double height;

		public final boolean isValid(final int x, final int z) {
			return !uninitialized && this.x == x && this.z == z;
		}

		public final double get() {
			return height;
		}

		public final double set(final int x, final int z, final double height) {
			uninitialized = false;
			this.x = x;
			this.z = z;
			this.height = height;
			return height;
		}

	}
}
