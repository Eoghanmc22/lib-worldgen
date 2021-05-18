package net.minestom.worldgen.terrain.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.terrain.TerrainLayer;
import net.minestom.worldgen.terrain.TerrainThreadContext;
import net.minestom.worldgen.utils.Column;

public class SurfaceTerrainLayer extends TerrainLayer {

    public SurfaceTerrainLayer(WorldGen wg) {
        super(wg, 0);
    }

    @Override
    protected int genTerrain(int x, int z, int genStructures, Column column, ChunkRandom r, TerrainThreadContext threadContext) {
        genStructures = parent.genTerrain(x, z, genStructures, column, threadContext);
        ChunkRandom rng = new ChunkRandom(r.nextInt(Integer.MAX_VALUE), wg.getSeed());
        rng.initChunkSeed(x/16, z/16);
        return column.getBiome().buildSurface(x, z, genStructures, column.getBiomeId(), column, rng, threadContext);
    }
}
