package net.minestom.worldgen.terrain;

import net.minestom.worldgen.heightmap.HeightMapThreadContext;

public final class TerrainThreadContext {

	public final HeightMapThreadContext heightMapCtx;

	public TerrainThreadContext(final HeightMapThreadContext heightMapCtx) {
		this.heightMapCtx = heightMapCtx;
	}
}
