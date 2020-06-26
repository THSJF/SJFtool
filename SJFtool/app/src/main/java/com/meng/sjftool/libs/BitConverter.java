package com.meng.sjftool.libs;

import java.nio.charset.*;

public abstract class BitConverter {
	private static BitConverterLE leInstance = null;
	private static BitConverterBE beInstance = null;

	public static BitConverterLE getInstanceLittleEndian() {
		if (leInstance == null) {
			leInstance = new BitConverterLE();
		}
		return leInstance;
	}

	public static BitConverterBE getInstanceBigEndian() {
		if (beInstance == null) {
			beInstance = new BitConverterBE();
		}
		return beInstance;
	}

	public abstract byte[] getBytes(short s);

	public byte[] getBytes(char c) {
		return getBytes((short)c);	
	}

	public abstract byte[] getBytes(int i);

	public abstract byte[] getBytes(long l);

	public byte[] getBytes(float f) {
		return getBytes(Float.floatToIntBits(f));	
	}

	public byte[] getBytes(double d) {
		return getBytes(Double.doubleToLongBits(d));
	}

	public byte[] getBytes(String s) {
		return s.getBytes(StandardCharsets.UTF_8);
	}

	public abstract short toShort(byte[] data, int pos);

	public short toShort(byte[] data) {
		return toShort(data , 0);
	}

	public abstract int toInt(byte[] data, int pos);

	public int toInt(byte[] data) {
		return toInt(data, 0);
	}

	public abstract long toLong(byte[] data, int pos);

	public long toLong(byte[] data) {
		return toLong(data , 0);
	}

	public float toFloat(byte[] data, int pos) {
		return Float.intBitsToFloat(toInt(data, pos));
	}

	public float toFloat(byte[] data) {
		return toFloat(data , 0);
	}

	public double toDouble(byte[] data, int pos) {
		return Double.longBitsToDouble(toLong(data, pos));
	}

	public double toDouble(byte[] data) {
		return toDouble(data, 0);
	}

	public String toString(byte[] data, int pos, int byteCount) {
		return new String(data, pos, byteCount, StandardCharsets.UTF_8);
	}

	public String toString(byte[] data) {
		return toString(data, 0, data.length);
	}

	public static class BitConverterLE extends BitConverter {

		private BitConverterLE() {

		}

		public byte[] getBytes(short s) {
			byte[] bs=new byte[2];
			bs[0] = (byte) ((s >> 0) & 0xFF);
			bs[1] = (byte) ((s >> 8) & 0xFF) ;
			return bs;	
		}

		public byte[] getBytes(int i) {
			byte[] bs=new byte[4];
			bs[0] = (byte) ((i >> 0) & 0xFF);
			bs[1] = (byte) ((i >> 8) & 0xFF);
			bs[2] = (byte) ((i >> 16) & 0xFF);
			bs[3] = (byte) ((i >> 24) & 0xFF);
			return bs;	
		}

		public byte[] getBytes(long l) {
			byte[] bs=new byte[8];
			bs[0] = (byte) ((l >> 0) & 0xFF);
			bs[1] = (byte) ((l >> 8) & 0xFF);
			bs[2] = (byte) ((l >> 16) & 0xFF);
			bs[3] = (byte) ((l >> 24) & 0xFF);
			bs[4] = (byte) ((l >> 32) & 0xFF);
			bs[5] = (byte) ((l >> 40) & 0xFF);
			bs[6] = (byte) ((l >> 48) & 0xFF);
			bs[7] = (byte) ((l >> 56) & 0xFF);
			return bs;
		}

		public short toShort(byte[] data, int pos) {
			return (short) ((data[pos] & 0xFF) << 0 | (data[pos + 1] & 0xFF) << 8);
		}

		public int toInt(byte[] data, int pos) {
			return (data[pos] & 0xFF) << 0 | (data[pos + 1] & 0xFF) << 8 | (data[pos + 2] & 0xFF) << 16 | (data[pos + 3] & 0xFF) << 24;
		}

		public long toLong(byte[] data, int pos) {
			return ((data[pos] & 0xFFL) << 0) | (data[pos + 1] & 0xFFL) << 8 | (data[pos + 2] & 0xFFL) << 16 | (data[pos + 3] & 0xFFL) << 24 | (data[pos + 4] & 0xFFL) << 32 | (data[pos + 5] & 0xFFL) << 40 | (data[pos + 6] & 0xFFL) << 48 | (data[pos + 7] & 0xFFL) << 56;
		}
	}

	public static class BitConverterBE extends BitConverter {

		private BitConverterBE() {

		}

		public byte[] getBytes(short s) {
			byte[] bs=new byte[2];
			bs[0] = (byte) ((s >> 8) & 0xFF);
			bs[1] = (byte) ((s >> 0) & 0xFF);
			return bs;	
		}

		public byte[] getBytes(int i) {
			byte[] bs=new byte[4];
			bs[0] = (byte) ((i >> 24) & 0xFF);
			bs[1] = (byte) ((i >> 16) & 0xFF);
			bs[2] = (byte) ((i >> 8) & 0xFF);
			bs[3] = (byte) ((i >> 0) & 0xFF);
			return bs;	
		}

		public byte[] getBytes(long l) {
			byte[] bs=new byte[8];
			bs[0] = (byte) ((l >> 56) & 0xFF);
			bs[1] = (byte) ((l >> 48) & 0xFF);
			bs[2] = (byte) ((l >> 40) & 0xFF);
			bs[3] = (byte) ((l >> 32) & 0xFF);
			bs[4] = (byte) ((l >> 24) & 0xFF);
			bs[5] = (byte) ((l >> 16) & 0xFF);
			bs[6] = (byte) ((l >> 8) & 0xFF);
			bs[7] = (byte) ((l >> 0) & 0xFF);
			return bs;
		}

		public short toShort(byte[] data, int pos) {
			return (short) ((data[pos] & 0xFF) << 8 | (data[pos + 1] & 0xFF) << 0);
		}

		public int toInt(byte[] data, int pos) {
			return (data[pos] & 0xFF) << 24 | (data[pos + 1] & 0xFF) << 16 | (data[pos + 2] & 0xFF) << 8 | (data[pos + 3] & 0xFF) << 0;
		}

		public long toLong(byte[] data, int pos) {
			return ((data[pos] & 0xFFL) << 56) | (data[pos + 1] & 0xFFL) << 48 | (data[pos + 2] & 0xFFL) << 40 | (data[pos + 3] & 0xFFL) << 32 | (data[pos + 4] & 0xFFL) << 24 | (data[pos + 5] & 0xFFL) << 16 | (data[pos + 6] & 0xFFL) << 8 | (data[pos + 7] & 0xFFL) << 0;
		}
	}
}
