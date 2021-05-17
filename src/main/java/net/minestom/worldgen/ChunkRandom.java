package net.minestom.worldgen;

//code taken from OTG
public class ChunkRandom {

	long baseSeed;
	long worldSeed;
	long chunkSeed;

	public ChunkRandom(final long baseSeed) {
		this.baseSeed = baseSeed;
	}

	public ChunkRandom(final long baseSeed, final long worldSeed) {
		this.baseSeed = baseSeed;
		setWorldSeed(worldSeed);
	}


	private ChunkRandom(final long baseSeed, final long worldSeed, final long chunkSeed) {
		this(baseSeed, worldSeed);
		this.chunkSeed = chunkSeed;
	}

	public ChunkRandom copy() {
		return new ChunkRandom(baseSeed, worldSeed, chunkSeed);
	}

	public static long scramble(final long baseSeed) {
		long scrambledBaseSeed = baseSeed;
		scrambledBaseSeed *= (scrambledBaseSeed * 6364136223846793005L + 1442695040888963407L);
		scrambledBaseSeed += baseSeed;
		scrambledBaseSeed *= (scrambledBaseSeed * 6364136223846793005L + 1442695040888963407L);
		scrambledBaseSeed += baseSeed;
		scrambledBaseSeed *= (scrambledBaseSeed * 6364136223846793005L + 1442695040888963407L);
		scrambledBaseSeed += baseSeed;
		return scrambledBaseSeed;
	}

	public static long scramble(final long baseSeed, final long worldSeed) {
		final long scrambledBaseSeed = scramble(baseSeed);
		long scrambledWorldSeed = worldSeed;
		scrambledWorldSeed *= (scrambledWorldSeed * 6364136223846793005L + 1442695040888963407L);
		scrambledWorldSeed += scrambledBaseSeed;
		scrambledWorldSeed *= (scrambledWorldSeed * 6364136223846793005L + 1442695040888963407L);
		scrambledWorldSeed += scrambledBaseSeed;
		scrambledWorldSeed *= (scrambledWorldSeed * 6364136223846793005L + 1442695040888963407L);
		scrambledWorldSeed += scrambledBaseSeed;
		return scrambledWorldSeed;
	}


	public void setWorldSeed(final long worldSeed) {
		this.worldSeed = scramble(baseSeed, worldSeed);
	}

	public void initChunkSeed(final long x, final long z) {
		chunkSeed = worldSeed;
		chunkSeed *= (chunkSeed * 6364136223846793005L + 1442695040888963407L);
		chunkSeed += x;
		chunkSeed *= (chunkSeed * 6364136223846793005L + 1442695040888963407L);
		chunkSeed += z;
		chunkSeed *= (chunkSeed * 6364136223846793005L + 1442695040888963407L);
		chunkSeed += x;
		chunkSeed *= (chunkSeed * 6364136223846793005L + 1442695040888963407L);
		chunkSeed += z;
	}

	public int nextInt(final int x) {
		int i = (int) ((chunkSeed >> 24) % x);
		if (i < 0) i += x;
		chunkSeed *= (chunkSeed * 6364136223846793005L + 1442695040888963407L);
		chunkSeed += worldSeed;
		return i;
	}

	public float nextFloat() {
		final int step = 1024;
		int i = (int) ((chunkSeed >> 24) % step);
		if (i < 0) i += step;
		chunkSeed *= (chunkSeed * 6364136223846793005L + 1442695040888963407L);
		chunkSeed += worldSeed;
		return i / 1024f;
	}

	@SafeVarargs
	public final <T> T getRandomInArray(final T... array) {
		return array[nextInt(array.length)];
	}

	public int randomOf2(final int i1, final int i2) {
		return nextInt(2) == 0 ? i1 : i2;
	}

	public int getRandomOf4(final int a, final int b, final int c, final int d) {
		return b == c && c == d
				? b
				: (a == b && a == c
				? a
				: (a == b && a == d
				? a
				: (a == c && a == d
				? a
				: (a == b && c != d
				? a
				: (a == c && b != d
				? a
				: (a == d && b != c
				? a
				: (b == c && a != d
				? b
				: (b == d && a != c
				? b
				: (c == d && a != b
				? c
				: getRandomInArray(a, b, c, d
		))))))))));
	}

}
