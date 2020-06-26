package com.meng.sjftool.libs;

import java.io.*;
import java.security.*;
import java.nio.charset.*;
import java.util.*;

public abstract class Hash {

	private static MD5 md5Instance = null;
	
	public static MD5 getMd5Instance() {
		if(md5Instance == null){
			md5Instance = new MD5();
		}
		return md5Instance;
	}
	
	public static CRC32A getCRC32aInstance() {
		return CRC32A.getInstance();
	}
	
	public String calculate(String str) {
		return calculate(str.getBytes(StandardCharsets.UTF_8));
	}

	public abstract String calculate(byte[] bs);

	public String calculate(File file) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return calculate(inputStream);
		} catch (Exception e) {
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public abstract String calculate(InputStream inputStream);

	public static class MD5 extends Hash {

		private MD5(){
			
		}
		
		public String calculate(byte[] bs) {
			try {
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				mdTemp.update(bs);
				return toHexString(mdTemp.digest());
			} catch (Exception e) {
				return null;
			}
		}

		public String calculate(InputStream inputStream) {
			try {
				MessageDigest mdTemp = MessageDigest.getInstance("MD5");
				byte[] buffer = new byte[1024];
				int numRead = 0;
				while ((numRead = inputStream.read(buffer)) > 0) {
					mdTemp.update(buffer, 0, numRead);
				}
				return toHexString(mdTemp.digest());
			} catch (Exception e) {
				return null;
			}
		}
		
		private String toHexString(byte[] md) {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
			int j = md.length;
			char str[] = new char[j * 2];
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
				str[i * 2 + 1] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		}
	}
}
