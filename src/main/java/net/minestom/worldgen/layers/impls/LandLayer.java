package net.minestom.worldgen.layers.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.layers.Layer;
import net.minestom.worldgen.layers.ThreadContext;

public class LandLayer extends Layer {

	final float landProbability;

	public LandLayer(WorldGen wg, long baseSeed, float landProbability) {
		super(wg, baseSeed);
		this.landProbability = landProbability;
	}

	@Override
	public int genBiomes(int x, int z, ChunkRandom r, ThreadContext threadContext) {
		r.initChunkSeed(x,z);
		return setIsLand(parent.genBiomes(x, z, threadContext), r.nextFloat() <= landProbability);
	}

}
