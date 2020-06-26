package com.meng.sjftool.libs;

public class CRC32A {
	/**
	 * @author 月溪
	 */
	private static CRC32A instance=null;

	public static CRC32A getInstance() {
		if (instance == null) {
			instance = new CRC32A();
		}
		return instance;
	}
	private int[] table=new int[256];

	private CRC32A() {
		int value;
		for (int i=0;i < 256;i++) {
			value = i << 24;
			for (int j=0;j < 8;j++) {
				if ((value & 0x80000000) != 0) {
					value = (value << 1) ^ 79764919;
				} else {
					value = value << 1;
				}
			}
			table[i] = value;
		}
	}

	public long calculate(byte[] data) {
		int start=0xffffffff;
		for (int i=0;i < data.length;i++) {
			start = (start << 8) ^ (table[((start >> 24) & 0xff) ^ data[i]]);
		}
		start = ~start;
		return (((start & 0xff) << 24) + ((start & 0xff00) << 8) + ((start & 0xff0000) >>> 8) + ((start & 0xff000000) >>> 24)) & 0xFFFFFFFFL;
	}
}
