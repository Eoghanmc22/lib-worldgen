package net.minestom.worldgen.biomegen;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;

/**
 * Abstract class for biome layers.
 * <p>
 * This class will have the randoms and contain the bit masks and bit shifts for data encoded into the "biome ids"
 * which really are ints that have a sector for for the biome id sent to the client
 * and also have bits that tell you if this is a land variant as well as optional extra data
 *
 * @author Eoghanmc22
 */

public abstract class BiomeLayer {

	/*
	Biome ints don't just contain the biome id.
	They also contain info about that biome
	 */

	// Biome id -- actual biome sent to the client
	// 32,768 possible biomes
	public final static int biomeMask = 0x7FFF;
	public final static int biomeShift = 0;

	// Controls what biomes can be placed in a certain area
	// 256 climates
	public final static int climateMask = 0x7F_8000;
	public final static int climateShift = 15;

	public final static int reservedClimate = 0xFF;

	// is land
	public final static int landBit = 0x80_0000;
	public final static int landShift = 23;

	// extra data
	public final static int dataMask = 0xFF00_0000;
	public final static int dataShift = 24;

	private final int id;

	protected final BiomeLayer parent;
	protected final WorldGen wg;

	//Seeds for randoms
	private final long baseSeed;
	private long worldSeed;

	public BiomeLayer(final WorldGen wg, final long baseSeed) {
		this.baseSeed = baseSeed;

		this.wg = wg;
		initWorldGenSeed(wg.getSeed());
		id = wg.getBiomeLayerIdCounter().getAndIncrement();
		if (wg.getBiomeLayers().size() > 0)
			this.parent = wg.getBiomeLayers().getLast();
		else
			this.parent = null;
	}

	public static int getBiomeId(final int id) {
		return (id & biomeMask) >>> biomeShift;
	}

	public static int getBiomeData(final int id) {
		return (id & dataMask) >>> dataShift;
	}

	public static boolean isLand(final int id) {
		return ((id & landBit) >>> landShift) == 1;
	}

	public static int getClimate(final int id) {
		return (id & climateMask) >>> climateShift;
	}

	public static int setBiomeId(final int id, final int biomeId) {
		return (biomeId << biomeShift) | (id & ~biomeMask);
	}

	public static int setBiomeData(final int id, final int biomeData) {
		return (biomeData << dataShift) | (id & ~dataMask);
	}

	public static int setIsLand(final int id, final boolean isLand) {
		return ((isLand ? 1 : 0) << landShift) | (id & ~landBit);
	}

	public static int setClimate(final int id, final int climate) {
		return (climate << climateShift) | (id & ~climateMask);
	}

	public static boolean cmpBiomeClimate(final int id, final int climate, final int biome) {
		return getClimate(id) == climate && getBiomeId(id) == biome;
	}

	public final int genBiomesAndCache(final int x, final int z, final BiomeThreadContext threadContext) {
		BiomeThreadContext.BiomeCache cache = threadContext.cache[id];
		final BiomeThreadContext.BiomeCacheEntry entry = cache.cache[x & 0xF][z & 0xF];

		if (entry.isValid(x, z)) {
			return entry.get();
		}
		return entry.set(x, z, genBiomes(x, z, threadContext));
	}

	public final int genBiomes(final int x, final int z, final BiomeThreadContext threadContext) {
		return genBiomes(x, z, new ChunkRandom(baseSeed, worldSeed), threadContext);
	}

	protected abstract int genBiomes(final int x, final int z, final ChunkRandom r, final BiomeThreadContext threadContext);

	private void initWorldGenSeed(final long worldSeed) {
		if (parent != null)
			parent.initWorldGenSeed(worldSeed);

		this.worldSeed = worldSeed;
	}

}
