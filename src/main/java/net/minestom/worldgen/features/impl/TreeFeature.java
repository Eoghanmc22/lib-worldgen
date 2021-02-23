package net.minestom.worldgen.features.impl;

import net.minestom.server.instance.batch.BlockBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.features.PlaceableFeature;

public class TreeFeature extends PlaceableFeature {

	private final Block leaves, logs;
	
	public TreeFeature(float chance, Block leaves, Block logs) {
		super(2, 2, chance);
		this.leaves = leaves;
		this.logs = logs;
	}

	@Override
	public void build(BlockBatch batch, int x, int y, int z, ChunkRandom rng) {
		int height = rng.nextInt(3) + 1;
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				batch.setBlock(x+i, y+height, z+j, leaves);
			}
		}
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				batch.setBlock(x+i, y+height+1, z+j, leaves);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				batch.setBlock(x+i, y+height+2, z+j, leaves);
			}
		}
		batch.setBlock(x, y+height+3, z, leaves);
		batch.setBlock(x+1, y+height+3, z, leaves);
		batch.setBlock(x-1, y+height+3, z, leaves);
		batch.setBlock(x, y+height+3, z+1, leaves);
		batch.setBlock(x, y+height+3, z-1, leaves);
		switch (rng.nextInt(4)) {
			case 0: {
				batch.setBlock(x+1, y+height+3, z+1, leaves);
				break;
			}
			case 1: {
				batch.setBlock(x+1, y+height+3, z-1, leaves);
				break;
			}
			case 2: {
				batch.setBlock(x-1, y+height+3, z+1, leaves);
				break;
			}
			case 3: {
				batch.setBlock(x-1, y+height+3, z-1, leaves);
				break;
			}
		}

		for (int i = 0; i < height + 2; i++) {
			batch.setBlock(x, y+i, z, logs);
		}
	}

	@Override
	public String getPersistentId() {
		return "BasicTreeFeature";
	}

}
