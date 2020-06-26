package com.meng.sjftool.libs;

import java.io.*;
import java.nio.charset.*;

public class FileTool {
	public static void deleteFiles(File folder) {
		File[] fs = folder.listFiles();
		if (fs != null && fs.length > 0) {
			for (File f : fs) {
				if (f.isDirectory()) {
					deleteFiles(f);
					f.delete();
				} else {
					f.delete();
				}
			}
		}
	}

	public static void fileCopy(String src, String des) {
		try {
			BufferedInputStream bis = null;
			bis = new BufferedInputStream(new FileInputStream(src));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
			int i = -1;
			byte[] bt = new byte[2014];
			while ((i = bis.read(bt)) != -1) {
				bos.write(bt, 0, i);
			}
			bis.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readString(String fileName) {
		return readString(new File(fileName));
	}

	public static String readString(File f) {
		try {      
			long filelength = f.length();
			byte[] filecontent = new byte[(int) filelength];
			FileInputStream in = new FileInputStream(f);
			in.read(filecontent);
			in.close();
			return new String(filecontent, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readBytes(File f) {
		byte[] filecontent=null;
		try {
			long filelength = f.length();
			filecontent = new byte[(int) filelength];
			FileInputStream in = new FileInputStream(f);
			in.read(filecontent);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filecontent;
	}

	public static byte[] readBytes(String path) {
		return readBytes(new File(path));
	}
}
