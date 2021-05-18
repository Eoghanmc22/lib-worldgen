package net.minestom.worldgen.terrain.impls;

import net.minestom.server.instance.block.Block;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.terrain.TerrainLayer;
import net.minestom.worldgen.terrain.TerrainThreadContext;
import net.minestom.worldgen.utils.Column;

public class DemoTerrainLayer extends TerrainLayer {
    public DemoTerrainLayer(WorldGen wg, long baseSeed) {
        super(wg, baseSeed);
    }

    @Override
    protected int genTerrain(int x, int z, int genStructures, Column column, ChunkRandom r, TerrainThreadContext threadContext) {
        genStructures = parent.genTerrain(x, z, genStructures, column, threadContext);
        for (int y = 0; y <= (int) column.getHeightMap(); y++) {
            column.setBlock(Block.STONE, y);
        }
        for (int y = 0; y <= r.nextInt(4); y++) {
            column.setBlock(Block.BEDROCK, y);
        }
        return genStructures;
    }
}
