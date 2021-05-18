package net.minestom.worldgen.utils;

import net.minestom.server.instance.block.Block;

public class Column {

    private final short[] blocks = new short[256];
    private int maxHeight = 0;

    public final void setBlock(final Block block, final int y) {
        blocks[y] = block.getBlockId();
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
}
