package net.minestom.worldgen.utils;

import net.minestom.server.instance.block.Block;
import net.minestom.worldgen.biomes.BiomeConfig;

public class Column {

    private final short[] blocks = new short[256];
    private int maxHeight = 0;

    private boolean metaSet = false;
    private BiomeConfig biome = null;
    private int biomeId = 0;
    private double heightMap = 0;

    public final void setBlock(final Block block, final int y) {
        setBlock(block.getBlockId(), y);
    }

    public final void setBlock(final short block, final int y) {
        if (y > maxHeight) {
            maxHeight = y;
        }
        blocks[y] = block;
    }

    public final short getBlock(final int y) {
        return blocks[y];
    }

    public final int getMaxHeight() {
        return maxHeight;
    }

    public void setMeta(final BiomeConfig biome, final int biomeId, final double heightMap) {
        if (!metaSet) {
            metaSet = true;
            this.biome = biome;
            this.biomeId = biomeId;
            this.heightMap = heightMap;
        }
    }

    public BiomeConfig getBiome() {
        return biome;
    }

    public double getHeightMap() {
        return heightMap;
    }

    public int getBiomeId() {
        return biomeId;
    }
}
