package net.minestom.worldgen.heightmap.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.biomegen.BiomeLayer;
import net.minestom.worldgen.biomes.BiomeConfig;
import net.minestom.worldgen.heightmap.HeightMapLayer;
import net.minestom.worldgen.heightmap.HeightMapThreadContext;

public class BaseHeightMapLayer extends HeightMapLayer {
    public BaseHeightMapLayer(final WorldGen wg) {
        super(wg, 0);
    }

    @Override
    protected double genHeight(final int x, final int z, final ChunkRandom r, final HeightMapThreadContext threadContext) {
        int id = wg.getBiomeLayers().getLast().genBiomes(x, z, threadContext.biomeCtx);
        BiomeConfig biome = wg.getBiomeGroup(BiomeLayer.getClimate(id)).getBiome(BiomeLayer.getBiomeId(id));
        return biome.getHeight(x, z, id);
    }
}
