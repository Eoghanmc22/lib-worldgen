package net.minestom.worldgen.heightmap.impls;

import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgen.heightmap.HeightMapLayer;
import net.minestom.worldgen.heightmap.HeightMapThreadContext;

public class InterpolationHeightMapLayer extends HeightMapLayer {
    public InterpolationHeightMapLayer(final WorldGen wg) {
        super(wg, 0);
    }

    @Override
    protected double genHeight(final int x, final int z, final ChunkRandom r, final HeightMapThreadContext threadContext) {
        double height11 = parent.genHeightAndCache(x-2, z-2, threadContext);
        double height12 = parent.genHeightAndCache(x-1, z-2, threadContext);
        double height13 = parent.genHeightAndCache(x, z-2, threadContext);
        double height14 = parent.genHeightAndCache(x+1, z-2, threadContext);
        double height15 = parent.genHeightAndCache(x+2, z-2, threadContext);

        double height21 = parent.genHeightAndCache(x-2, z-1, threadContext);
        double height22 = parent.genHeightAndCache(x-1, z-1, threadContext);
        double height23 = parent.genHeightAndCache(x, z-1, threadContext);
        double height24 = parent.genHeightAndCache(x+1, z-1, threadContext);
        double height25 = parent.genHeightAndCache(x+2, z-1, threadContext);

        double height31 = parent.genHeightAndCache(x-2, z, threadContext);
        double height32 = parent.genHeightAndCache(x-1, z, threadContext);
        double height33 = parent.genHeightAndCache(x, z, threadContext);
        double height34 = parent.genHeightAndCache(x+1, z, threadContext);
        double height35 = parent.genHeightAndCache(x+2, z, threadContext);

        double height41 = parent.genHeightAndCache(x-2, z+1, threadContext);
        double height42 = parent.genHeightAndCache(x-1, z+1, threadContext);
        double height43 = parent.genHeightAndCache(x, z+1, threadContext);
        double height44 = parent.genHeightAndCache(x+1, z+1, threadContext);
        double height45 = parent.genHeightAndCache(x+2, z+1, threadContext);

        double height51 = parent.genHeightAndCache(x-2, z+2, threadContext);
        double height52 = parent.genHeightAndCache(x-1, z+2, threadContext);
        double height53 = parent.genHeightAndCache(x, z+2, threadContext);
        double height54 = parent.genHeightAndCache(x+1, z+2, threadContext);
        double height55 = parent.genHeightAndCache(x+2, z+2, threadContext);

        int height = (int) (
                (height11/5d + height12/5d + height13/5d + height14/5d + height15/5d)/5d +
                (height21/5d + height22/5d + height23/5d + height24/5d + height25/5d)/5d +
                (height31/5d + height32/5d + height33/5d + height34/5d + height35/5d)/5d +
                (height41/5d + height42/5d + height43/5d + height44/5d + height45/5d)/5d +
                (height51/5d + height52/5d + height53/5d + height54/5d + height55/5d)/5d
                );
        return height;
    }
}
