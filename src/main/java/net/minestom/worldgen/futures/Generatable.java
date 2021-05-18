package net.minestom.worldgen.futures;

import net.minestom.worldgen.WorldGen;

public interface Generatable {

	void generate(GenerationFuture future, WorldGen wg, int rX, int rY, int rZ, int chunkX, int chunkZ, int biomeId);
}
