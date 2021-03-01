package net.minestom.worldgen.utils;

public class MutLong {

	public long val;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MutLong mutLong = (MutLong) o;

		return val == mutLong.val;
	}

	@Override
	public int hashCode() {
		return (int) (val ^ (val >>> 32));
	}

}
