package net.minestom.worldgen.layers;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract class for layer.
 * <p>
 * This class will have the randoms and contain the bit masks and bit shifts for data encoded into the "biome ids"
 * which really are ints that have a sector for for the biome id sent to the client
 * and also have bits that tell you if this is a land variant as well as optional extra data
 *
 * @author Eoghanmc22
 */

//Some code taken from OTG
public abstract class Layer {

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

	private static final AtomicInteger idCounter = new AtomicInteger(0);
	private final int id = idCounter.getAndIncrement();

	protected Layer parent;
	protected WorldGen worldGen;

	//Seeds for randoms
	private final long baseSeed;
	private long worldSeed;

	public Layer(final long baseSeed) {
		this.baseSeed = baseSeed;
	}

	// it is required to call this before this layer is used but it is called for you in WorldGen#addLayer();
	public void setWorldGen(WorldGen wg) {
		this.worldGen = wg;
		if (wg.getLayers().size() > 0)
			this.parent = wg.getLayers().getLast();
		else
			this.parent = null;
	}

	public static int mod(final int a, final int b) {
		return (a % b + b) % b;
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

	public final int genBiomesAndCache(final int x, final int z, ThreadContext threadContext) {
		ThreadContext.BiomeCache cache = threadContext.cache[id];
		final ThreadContext.BiomeCacheEntry entry = cache.cache[x & 0xF][z & 0xF];

		if (entry.isValid(x, z)) {
			return entry.get();
		}
		return entry.set(x, z, genBiomes(x, z, threadContext));
	}

	public final int genBiomes(final int x, final int z, ThreadContext threadContext) {
		return genBiomes(x, z, new ChunkRandom(baseSeed, worldSeed), threadContext);
	}

	protected abstract int genBiomes(int x, int z, ChunkRandom r, ThreadContext threadContext);

	//Start otg
	public void initWorldGenSeed(final long worldSeed) {
		if (parent != null)
			parent.initWorldGenSeed(worldSeed);

		this.worldSeed = worldSeed;
	}

}
