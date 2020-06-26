package com.meng.sjftool.update;

import com.meng.sjftool.libs.*;
import java.io.*;
import java.util.*;

public class BotDataPack {

	public ArrayList<Byte> data = new ArrayList<>();
	public byte[] dataArray;
	private BitConverter bcle = BitConverter.getInstanceLittleEndian();
	public static final short headLength=10;
	public int dataPointer=0;

	public static final byte typeByte=0;
	public static final byte typeShort=1;
	public static final byte typeInt=2;
	public static final byte typeLong=3;
	public static final byte typeFloat=4;
	public static final byte typeDouble=5;
	public static final byte typeString=6;
	public static final byte typeBoolean=7;
	public static final byte typeFile=8;

	public static final int opTextNotify = 32;

	public static final int opGetApp = 53;
	public static final int opCrashLog = 54;

	public static final int sendToMaster = 56;

	public static final int cookie = 58;
	public static final int opGetGzApp = 59;
	

	public static BotDataPack encode(int opCode) {
		return new BotDataPack(opCode);
	}

	public static BotDataPack decode(byte[] bytes) {
		return new BotDataPack(bytes);
	}

	private BotDataPack(int opCode) {
		//length(4) version(2) opCode(4)
		writeByteDataIntoArray(bcle.getBytes(0));
		writeByteDataIntoArray(bcle.getBytes((short)1));
		writeByteDataIntoArray(bcle.getBytes(opCode));
	}   

	private BotDataPack(byte[] pack) {
		dataArray = pack;
		dataPointer = headLength;
	} 

	public byte[] getData() {
		byte[] retData=new byte[data.size()];
		for (int i=0;i < data.size();++i) {
			retData[i] = data.get(i);
		}
		byte[] len=bcle.getBytes(retData.length);
		retData[0] = len[0];
		retData[1] = len[1];
		retData[2] = len[2];
		retData[3] = len[3];
		dataArray = retData;
		return retData;
	}

	public int getLength() {
		return bcle.toInt(dataArray, 0);
	}  

	public short getVersion() {
		return bcle.toShort(dataArray, 4);
	}

	public int getOpCode() {
		return bcle.toShort(dataArray, 6);
	}

	private BotDataPack writeByteDataIntoArray(byte... bs) {
		for (byte b:bs) {
			data.add(b);
			++dataPointer;
		}
		return this;
	}

	public BotDataPack write(byte b) {
		writeByteDataIntoArray(typeByte);
		writeByteDataIntoArray(b);
		return this;
	}

	public BotDataPack write(short s) {
		writeByteDataIntoArray(typeShort);
		writeByteDataIntoArray(bcle.getBytes(s));
		return this;
	}

	public BotDataPack write(int i) {
		writeByteDataIntoArray(typeInt);
		writeByteDataIntoArray(bcle.getBytes(i));
		return this;
	}

	public BotDataPack write(long l) {
		writeByteDataIntoArray(typeLong);
		writeByteDataIntoArray(bcle.getBytes(l));
		return this;
	}

	public BotDataPack write(float f) {
		writeByteDataIntoArray(typeFloat);
		writeByteDataIntoArray(bcle.getBytes(f));
		return this;
	}

	public BotDataPack write(double d) {
		writeByteDataIntoArray(typeDouble);
		writeByteDataIntoArray(bcle.getBytes(d));
		return this;
	}

	public BotDataPack write(String s) {
		writeByteDataIntoArray(typeString);
		byte[] stringBytes = bcle.getBytes(s);
		write(stringBytes.length);
		writeByteDataIntoArray(stringBytes);
		return this;
	}

	public BotDataPack write(boolean b) {
		writeByteDataIntoArray(typeBoolean);
		writeByteDataIntoArray(b ?(byte)1: (byte)0);
		return this;
	}

	public BotDataPack write(File file) {
		try {
			FileInputStream fin=new FileInputStream(file);
			byte[] bs=new byte[(int)file.length()];
			fin.read(bs, 0, bs.length);
			writeByteDataIntoArray(typeFile);
			write((int)file.length());
			writeByteDataIntoArray(bs);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		return this;
	}

	public File readFile(File file) {
		if (dataArray[dataPointer++] == typeFile) {
			int fileLen=readInt();
			try {
				FileOutputStream fos=new FileOutputStream(file);
				fos.write(dataArray, dataPointer, fileLen);
			} catch (Exception e) {
				file.delete();
				file = null;
			}
			dataPointer += fileLen;
			return file;
		}
		throw new RuntimeException("not a file");
	}

	public byte readByte() {
		if (dataArray[dataPointer++] == typeByte) {
			return dataArray[dataPointer++];
		}
		throw new RuntimeException("not a byte number");
	}

	public short readShort() {
		if (dataArray[dataPointer++] == typeShort) {
			short s = bcle.toShort(dataArray, dataPointer);
			dataPointer += 2;
			return s;
		}
		throw new RuntimeException("not a short number");
	}

	public int readInt() {
		if (dataArray[dataPointer++] == typeInt) {
			int i= bcle.toInt(dataArray, dataPointer);
			dataPointer += 4;
			return i;
		}
		throw new RuntimeException("not a int number");
	}

	public long readLong() {
		if (dataArray[dataPointer++] == typeLong) {
			long l= bcle.toLong(dataArray, dataPointer);
			dataPointer += 8;
			return l;
		}
		throw new RuntimeException("not a long number");
	}

	public float readFloat() {
		if (dataArray[dataPointer++] == typeFloat) {
			float f = bcle.toFloat(dataArray, dataPointer);
			dataPointer += 4;
			return f;
		}
		throw new RuntimeException("not a float number");
	}

	public double readDouble() {
		if (dataArray[dataPointer++] == typeDouble) {
			double d = bcle.toDouble(dataArray, dataPointer);
			dataPointer += 8;
			return d;
		}
		throw new RuntimeException("not a double number");
	}

	public String readString() {
		try {
			if (dataArray[dataPointer++] == typeString) {
				int len = readInt();
				String s = bcle.toString(dataArray, dataPointer, len);
				dataPointer += len;
				return s;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		return null;
	}

	public boolean readBoolean() {
		if (dataArray[dataPointer++] == typeBoolean) {
			return dataArray[dataPointer++] == 1;
		}
		throw new RuntimeException("not a boolean value");
	}

	public boolean hasNext() {
		return dataPointer != dataArray.length;
	}
}


