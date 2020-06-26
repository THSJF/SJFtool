package com.meng.sjftool.libs;

import java.io.*;
import java.util.zip.*;

public class Gzip {

	public static byte[] compress(byte[] original)  {
		ByteArrayOutputStream out = null;
		GZIPOutputStream gzip = null;
		try {
			out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			gzip.write(original);
			gzip.finish();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (gzip != null) {
					gzip.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] unCompress(byte[] compressed) {
		ByteArrayOutputStream out = null;
		GZIPInputStream gunzip = null;
		try {
			out = new ByteArrayOutputStream();
			gunzip = new GZIPInputStream(new ByteArrayInputStream(compressed));
			byte[] buffer = new byte[1024];
			int n;
			while ((n = gunzip.read(buffer)) != -1) {
				out.write(buffer, 0, n);
			}
			out.flush();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (gunzip != null) {
					gunzip.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

