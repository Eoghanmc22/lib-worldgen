package net.minestom.worldgen.features.impl;

import net.minestom.server.instance.block.Block;
import net.minestom.worldgen.ChunkRandom;
import net.minestom.worldgen.features.PlaceableFeature;
import net.minestom.worldgenUtils.Batch;

public class TreeFeature extends PlaceableFeature {

	private final Block leaves, logs;
	
	public TreeFeature(float chance, Block leaves, Block logs) {
		super(2, 2, chance);
		this.leaves = leaves;
		this.logs = logs;
	}

	@Override
	public void build(Batch batch, ChunkRandom rng) {
		int height = rng.nextInt(3) + 1;
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				batch.setBlock(i, height, j, leaves);
			}
		}
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				batch.setBlock(i, height+1, j, leaves);
			}
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				batch.setBlock(i, height+2, j, leaves);
			}
		}
		batch.setBlock(0, height+3, 0, leaves);
		batch.setBlock(1, height+3, 0, leaves);
		batch.setBlock(-1, height+3, 0, leaves);
		batch.setBlock(0, height+3, 1, leaves);
		batch.setBlock(0, height+3, -1, leaves);
		switch (rng.nextInt(4)) {
			case 0: {
				batch.setBlock(1, height+3, 1, leaves);
				break;
			}
			case 1: {
				batch.setBlock(1, height+3, -1, leaves);
				break;
			}
			case 2: {
				batch.setBlock(-1, height+3, 1, leaves);
				break;
			}
			case 3: {
				batch.setBlock(-1, height+3, -1, leaves);
				break;
			}
		}

		for (int i = 0; i < height + 2; i++) {
			batch.setBlock(0, i, 0, logs);
		}
	}

	@Override
	public String getPersistentId() {
		return "BasicTreeFeature";
	}

}
