package net.minestom.worldgen.futures;

import net.minestom.server.data.Data;
import net.minestom.server.data.SerializableData;
import net.minestom.server.data.SerializableDataImpl;
import net.minestom.server.utils.chunk.ChunkUtils;
import net.minestom.worldgen.WorldGen;
import net.minestom.worldgenUtils.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class GenerationFuture {

	private List<ChunkPos> neededChunks = new ArrayList<>();
	private final FutureManager futureManager;
	private final SerializableData data;
	private final String persistentRunnableId;
	private int state = -1;
	private int rX, rY, rZ, chunkX, chunkZ, biomeId;

	public GenerationFuture(WorldGen wg, SerializableData data, String persistentRunnableId) {
		this.futureManager = wg.getFutureManager();
		this.data = data;
		this.persistentRunnableId = persistentRunnableId;
	}

	public GenerationFuture(WorldGen wg, String persistentRunnableId) {
		this(wg, new SerializableDataImpl(), persistentRunnableId);
	}

	@SuppressWarnings("ConstantConditions")
	public GenerationFuture(WorldGen wg, Data serializedData) {
		this.futureManager = wg.getFutureManager();
		int length = serializedData.get("neededChunksLength");
		for (int i = 0; i < length; i++) {
			int x = serializedData.get("neededChunksX" + i);
			int z = serializedData.get("neededChunksZ" + i);
			neededChunks.add(new ChunkPos(x,z));
		}
		persistentRunnableId = serializedData.get("persistentRunnableId");
		data = serializedData.get("data");

		rX = serializedData.get("rX");
		rY = serializedData.get("rY");
		rZ = serializedData.get("rZ");
		chunkX = serializedData.get("chunkX");
		chunkZ = serializedData.get("chunkZ");
		biomeId = serializedData.get("biomeId");
	}

	public boolean runFuture() {
		boolean loaded = true;
		for (final ChunkPos cpos2 : getNeededChunks()) {
			if (!ChunkUtils.isLoaded(cpos2.toChunk(getFutureManager().getWg()))) {
				loaded = false;
				break;
			}
		}
		if (loaded) {
			run();
		}
		return loaded;
	}

	private void run() {
		final Generatable runnable = getFutureManager().getGeneratable(persistentRunnableId);
		if (runnable != null && state != 0) {
			runnable.generate(this, futureManager.getWg(), rX, rY, rZ, chunkX, chunkZ, biomeId);
			if (state == -1) {
				throw new RuntimeException("Runnable never updated the futures state. You need to call 'done' or 'incomplete' at the end of a future");
			}
		}
	}

	public void done() {
		state = 0;
		getFutureManager().removeFuture(this);
	}

	public void incomplete(List<ChunkPos> neededChunks) {
		state = 1;
		getFutureManager().removeFuture(this);
		this.neededChunks = neededChunks;
		getFutureManager().putFuture(this);
	}


	public FutureManager getFutureManager() {
		return futureManager;
	}

	public void setNeededChunks(List<ChunkPos> neededChunks) {
		this.neededChunks = neededChunks;
	}

	public List<ChunkPos> getNeededChunks() {
		return neededChunks;
	}

	public SerializableData getData() {
		return data;
	}

	public SerializableData serialize() {
		SerializableData serialized = new SerializableDataImpl();
		serialized.set("neededChunksLength", neededChunks.size());
		for (int i = 0; i < neededChunks.size(); i++) {
			serialized.set("neededChunksX" + i, neededChunks.get(i).getX());
			serialized.set("neededChunksZ" + i, neededChunks.get(i).getZ());
		}
		serialized.set("persistentRunnableId", persistentRunnableId);
		serialized.set("data", data);

		serialized.set("rX", rX);
		serialized.set("rY", rY);
		serialized.set("rZ", rZ);
		serialized.set("chunkX", chunkX);
		serialized.set("chunkZ", chunkZ);
		serialized.set("biomeId", biomeId);
		return serialized;
	}

	public void setAll(int rX, int rY, int rZ, int chunkX, int chunkZ, int biomeId) {
		this.rX = rX;
		this.rY = rY;
		this.rZ = rZ;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.biomeId = biomeId;
	}

	public void setrX(int rX) {
		this.rX = rX;
	}

	public void setrY(int rY) {
		this.rY = rY;
	}

	public void setrZ(int rZ) {
		this.rZ = rZ;
	}

	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}

	public void setChunkZ(int chunkZ) {
		this.chunkZ = chunkZ;
	}

	public void setBiomeId(int biomeId) {
		this.biomeId = biomeId;
	}
}
