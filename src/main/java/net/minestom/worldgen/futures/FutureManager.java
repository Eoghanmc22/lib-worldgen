package net.minestom.worldgen.futures;

import net.minestom.worldgen.WorldGen;
import net.minestom.worldgenUtils.ChunkPos;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FutureManager {

	private final WorldGen wg;
	private final Map<String, Generatable> generatableHashMap = new ConcurrentHashMap<>();
	private final Map<ChunkPos, List<GenerationFuture>> futuresHashMap = new ConcurrentHashMap<>();

	public FutureManager(WorldGen wg) {
		this.wg = wg;
	}

	public void putFuture(GenerationFuture future) {
		for (final ChunkPos cpos : future.getNeededChunks()) {
			if (futuresHashMap.containsKey(cpos)) {
				futuresHashMap.get(cpos).add(future);
			} else {
				List<GenerationFuture> list = new CopyOnWriteArrayList<>();
				futuresHashMap.put(cpos, list);
			}
		}
	}

	public void removeFuture(GenerationFuture future) {
		for (final ChunkPos cpos : future.getNeededChunks()) {
			if (futuresHashMap.containsKey(cpos)) {
				futuresHashMap.get(cpos).remove(future);
			}
		}
	}

	public void runFuturesForChunk(ChunkPos cpos) {
		if (futuresHashMap.containsKey(cpos)) {
			for (final GenerationFuture future : futuresHashMap.get(cpos)) {
				future.runFuture();
			}
		}
	}

	public void registerGeneratable(String persistentId, Generatable runnable) {
		generatableHashMap.put(persistentId, runnable);
	}

	public Generatable getGeneratable(String persistentId) {
		return generatableHashMap.get(persistentId);
	}

	public WorldGen getWg() {
		return wg;
	}

}
