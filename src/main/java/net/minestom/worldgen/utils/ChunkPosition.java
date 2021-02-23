package net.minestom.worldgen.utils;

import net.minestom.server.instance.Chunk;
import net.minestom.worldgen.WorldGen;

public class ChunkPosition {

	int x, z;

	public ChunkPosition() {

	}

	public ChunkPosition(final int x, final int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setZ(final int z) {
		this.z = z;
	}
	public void setXZ(final int x, final int z) {
		this.x = x;
		this.z = z;
	}

	public Chunk toChunk(WorldGen wg) {
		return wg.getInstance().getChunk(x, z);
	}

	public static ChunkPosition fromChunk(Chunk c) {
		return new ChunkPosition(c.getChunkX(), c.getChunkZ());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ChunkPosition that = (ChunkPosition) o;

		if (getX() != that.getX()) return false;
		return getZ() == that.getZ();
	}

	@Override
	public int hashCode() {
		int result = getX();
		result = 31 * result + getZ();
		return result;
	}

}
