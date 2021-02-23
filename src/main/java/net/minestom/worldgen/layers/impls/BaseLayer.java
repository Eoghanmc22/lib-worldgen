package net.minestom.worldgen.layers.impls;


import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.layers.Layer;

public class BaseLayer extends Layer {

	public BaseLayer() {
		super(0);
	}

	@Override
	public int genBiomes(final int x, final int z, ChunkRandom r) {
		return 0;
	}

}
