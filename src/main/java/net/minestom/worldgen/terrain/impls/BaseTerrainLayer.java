package net.minestom.worldgen.terrain.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.terrain.TerrainLayer;
import net.minestom.worldgen.terrain.TerrainThreadContext;
import net.minestom.worldgen.utils.Column;

public class BaseTerrainLayer extends TerrainLayer {
    public BaseTerrainLayer(WorldGen wg) {
        super(wg, 0);
    }

    @Override
    protected int genTerrain(int x, int z, int genStructures, Column column, ChunkRandom r, TerrainThreadContext threadContext) {
        double height = wg.getHeightMapLayers().getLast().genHeight(x, z, threadContext.heightMapCtx);
        column.setMeta(threadContext.heightMapCtx.biome, threadContext.heightMapCtx.biomeId, height);
        return 0;
    }
}
