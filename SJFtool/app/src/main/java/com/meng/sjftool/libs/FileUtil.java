package com.meng.sjftool.libs;

import java.io.*;

public class FileUtil {
	public static String fileReader(File file) throws IOException {
		BufferedReader bReader = new BufferedReader(
			new InputStreamReader(new FileInputStream(file)));
		StringBuilder temp = new StringBuilder();
		String temp1 = "";
		while ((temp1 = bReader.readLine()) != null) temp.append(temp1).append("\n");
		bReader.close();
		return temp.toString();
	}

	public static String fileReader(String path) throws IOException {
		BufferedReader bReader = new BufferedReader(
			new InputStreamReader(new FileInputStream(path)));
		StringBuilder temp = new StringBuilder();
		String temp1 = "";
		while ((temp1 = bReader.readLine()) != null) temp.append(temp1).append("\n");
		bReader.close();
		return temp.toString();
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (String aChildren : children) {
				boolean success = deleteDir(new File(dir, aChildren));
				if (!success)
					return false;
			}
		}
		return dir.delete();
	}
}
