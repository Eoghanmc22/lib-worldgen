package net.minestom.worldgen.utils;

import net.minestom.server.data.Data;
import net.minestom.server.instance.Chunk;
import net.minestom.server.utils.block.CustomBlockUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenBatch {

	private final Map<ChunkPosition, ArrayList<BlockData>> blocks = new HashMap<>();



	public Map<ChunkPosition, ArrayList<BlockData>> getBlocks() {
		return blocks;
	}

	private static class BlockData {

		private int x, y, z;
		private short blockStateId;
		private short customBlockId;
		private Data data;

		public void apply(Chunk chunk) {
			chunk.UNSAFE_setBlock(x, y, z, blockStateId, customBlockId, data, CustomBlockUtils.hasUpdate(customBlockId));
		}

	}
}
