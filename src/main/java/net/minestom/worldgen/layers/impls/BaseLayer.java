package net.minestom.worldgen.layers.impls;


import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.layers.Layer;
import net.minestom.worldgen.layers.ThreadContext;

public class BaseLayer extends Layer {

	public BaseLayer(WorldGen wg) {
		super(wg, 0);
	}

	@Override
	public int genBiomes(final int x, final int z, ChunkRandom r, ThreadContext threadContext) {
		return 0;
	}

}
