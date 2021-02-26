package net.minestom.worldgen.layers;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;

/**
 * Abstract class for layer.
 * <p>
 * This class will have the randoms and contain the bit masks and bit shifts for data encoded into the "biome ids"
 * which really are ints that have a sector for for the biome id sent to the client
 * and also have bits that tell you if this is a river/land variant
 * or if it should be modified in a certain way. Ex Hills variant
 *
 * @author Eoghanmc22
 */

//Some code taken from OTG
public abstract class Layer {

	/*
	Biome ints don't just contain the biome id.
	They also contain info about that biome
	 */

	//Biome id -- actual biome sent to the client
	//16,384 possible biomes
	public final static int biomeMask = 0x3FFF;
	public final static int biomeShift = 0;

	//Biome variation -- A server sided randomly generated number given to generator that allows for more unique generation.
	//64 variations
	public final static int biomeVariationMask = 0xFC000;
	public final static int biomeVariationShift = 14;

	//has rivers
	public final static int riverBit = 0x100000;
	public final static int riverShift = 20;

	//has wide rivers
	public final static int wideRiverBit = 0x200000;
	public final static int wideRiverShift = 21;

	//is island
	public final static int landBit = 0x400000;
	public final static int landShift = 22;

	//is ice variation / in cold climate
	public final static int iceBit = 0x800000;
	public final static int iceShift = 23;

	//Controls what biomes can be placed in a certain area
	//256 climates
	public final static int climateMask = 0xFF000000;
	public final static int climateShift = 24;

	public final static int reservedClimate = 0xFF;

	//Cache
	private Int2IntOpenHashMap cache = null;
	private Int2LongOpenHashMap cache2 = null;
	private final Object lock = new Object();

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

	public static int getBiomeVariation(final int id) {
		return (id & biomeVariationMask) >>> biomeVariationShift;
	}

	public static boolean hasRivers(final int id) {
		return ((id & riverBit) >>> riverShift) == 1;
	}

	public static boolean hasWideRivers(final int id) {
		return ((id & wideRiverBit) >>> wideRiverShift) == 1;
	}

	public static boolean isLand(final int id) {
		return ((id & landBit) >>> landShift) == 1;
	}

	public static boolean isIceVariant(final int id) {
		return ((id & iceBit) >>> iceShift) == 1;
	}

	public static int getClimate(final int id) {
		return (id & climateMask) >>> climateShift;
	}

	public static int setBiomeId(final int id, final int biomeId) {
		return (biomeId << biomeShift) | (id & ~biomeMask);
	}

	public static int setBiomeVariation(final int id, final int biomeVariation) {
		return (biomeVariation << biomeVariationShift) | (id & ~biomeVariationMask);
	}

	public static int setHasRivers(final int id, final boolean hasRivers) {
		return ((hasRivers ? 1 : 0) << riverShift) | (id & ~riverBit);
	}

	public static int setHasWideRivers(final int id, final boolean hasWideRivers) {
		return ((hasWideRivers ? 1 : 0) << wideRiverShift) | (id & wideRiverBit);
	}

	public static int setIsLand(final int id, final boolean isLand) {
		return ((isLand ? 1 : 0) << landShift) | (id & ~landBit);
	}

	public static int setIsIceVariant(final int id, final boolean isIceVariant) {
		return ((isIceVariant ? 1 : 0) << iceShift) | (id & ~iceBit);
	}

	public static int setClimate(final int id, final int climate) {
		return (climate << climateShift) | (id & ~climateMask);
	}

	public final int genBiomesAndCache(final int x, final int z) {
		if (cache == null) {
			synchronized (lock) {
				cache = new Int2IntOpenHashMap(256);
				cache2 = new Int2LongOpenHashMap(256);
			}
		}
		long pos = (((long) Math.abs(z)) << 32) | Math.abs(x);

		// Is this needed?
		if (z < 0)
			pos |= 1L << 63;
		if (x < 0)
			pos |= 1L << 31;

		final int pos2 = Math.abs(z % 16) * 16 + Math.abs(x % 16);
		synchronized (lock) {
			if (cache2.containsValue(pos))
				return cache.get(pos2);
		}
		final int biome = genBiomes(x, z);
		synchronized (lock) {
			cache.put(pos2, biome);
			cache2.put(pos2, pos);
		}
		return biome;
	}

	public final int genBiomes(final int x, final int z) {
		return genBiomes(x, z, new ChunkRandom(baseSeed, worldSeed));
	}

	protected abstract int genBiomes(int x, int z, ChunkRandom r);

	//Start otg
	public void initWorldGenSeed(final long worldSeed) {
		if (parent != null)
			parent.initWorldGenSeed(worldSeed);

		this.worldSeed = worldSeed;
	}

}
