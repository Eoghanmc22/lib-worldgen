package net.minestom.worldgen.biomegen.impls;


import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomegen.BiomeLayer;
import net.minestom.worldgen.biomegen.BiomeThreadContext;

public class BaseBiomeLayer extends BiomeLayer {

	public BaseBiomeLayer(final WorldGen wg) {
		super(wg, 0);
	}

	@Override
	public int genBiomes(final int x, final int z, final ChunkRandom r, final BiomeThreadContext threadContext) {
		return 0;
	}

}
