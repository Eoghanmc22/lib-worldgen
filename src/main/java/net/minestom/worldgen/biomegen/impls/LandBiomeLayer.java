package net.minestom.worldgen.biomegen.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomegen.BiomeLayer;
import net.minestom.worldgen.biomegen.BiomeThreadContext;

public class LandBiomeLayer extends BiomeLayer {

	final float landProbability;

	public LandBiomeLayer(final WorldGen wg, final long baseSeed, final float landProbability) {
		super(wg, baseSeed);
		this.landProbability = landProbability;
	}

	@Override
	public int genBiomes(final int x, final int z, final ChunkRandom r, final BiomeThreadContext threadContext) {
		r.initChunkSeed(x,z);
		return setIsLand(parent.genBiomes(x, z, threadContext), r.nextFloat() <= landProbability);
	}

}
