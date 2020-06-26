package com.meng.sjftool.bilibili.lib;

import com.meng.sjftool.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.nio.charset.*;
import org.jsoup.*;

public class NetworkCacher {

	public static enum Mode {
		CacheOnly,CachePrefer,CacheRefresh,NoCache
		}

	private static Mode picMode = Mode.CachePrefer;
	private static Mode jsonCacheMode = Mode.NoCache;

	public static void setJsonCacheMode(Mode jsonCacheMode) {
		NetworkCacher.jsonCacheMode = jsonCacheMode;
	}

	public static Mode getJsonCacheMode() {
		return jsonCacheMode;
	}

	public static void setPicCacheMode(Mode picCacheMode) {
		NetworkCacher.picMode = picCacheMode;
	}

	public static Mode getPicMode() {
		return picMode;
	}

	public static byte[] getNetPicture(final String url) {
		final File file=new File(MainActivity.instance.mainDic + "cache/" + new String(Hash.getMd5Instance().calculate(url.getBytes(StandardCharsets.UTF_8))).toUpperCase());
		switch (picMode) {
			case CacheOnly:
				if (!file.exists()) {
					return null;
				}
				return FileTool.readBytes(file);
			case CachePrefer:
				if (file.exists()) {
					return FileTool.readBytes(file);
				} else {
					try {
						byte[] img = Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
						FileOutputStream fos=new FileOutputStream(file);
						fos.write(img);
						fos.flush();
						fos.close();
						return img;
					} catch (Exception e) {
						return null;
					}
				}
			case CacheRefresh:
				if (file.exists()) {
					byte[] s = FileTool.readBytes(file);
					MainActivity.instance.threadPool.execute(new Runnable(){

							@Override
							public void run() {
								try {
									byte[] img = Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
									FileOutputStream fos=new FileOutputStream(file);
									fos.write(img);
									fos.flush();
									fos.close();
								} catch (Exception e) {
								}
							}
						});
					return s;
				} else {
					try {
						byte[] img = Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
						FileOutputStream fos=new FileOutputStream(file);
						fos.write(img);
						fos.flush();
						fos.close();
						return img;
					} catch (Exception e) {
						return null;
					}
				}
			case NoCache:
				try {
					return Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
				} catch (Exception e) {
					return null;
				}
		}
		return null;
	}

	public static String getNetJson(String url) {
		return getNetJson(url, null, null, jsonCacheMode);
	}

	public static String getNetJson(String url, Mode cacheMode) {
		return getNetJson(url, null, null, cacheMode);
	}

	public static String getNetJson(String url, String cookie) {
		return getNetJson(url, cookie, null, jsonCacheMode);
	}

	public static String getNetJson(String url, String cookie, Mode cacheMode) {
		return getNetJson(url, cookie, null, cacheMode);
	}

	public static String getNetJson(String url, String cookie, String referer) {
		return getNetJson(url, cookie, referer, jsonCacheMode);
	}

	public static String getNetJson(final String url, final String cookie, final String referer, Mode cacheMode) {
		final File file=new File(MainActivity.instance.mainDic + "cache/" + new String(Hash.getMd5Instance().calculate(url.getBytes(StandardCharsets.UTF_8))).toUpperCase());
		switch (cacheMode) {
			case CacheOnly:
				if (!file.exists()) {
					return null;
				}
				return FileTool.readString(file);
			case CachePrefer:
				if (file.exists()) {
					return FileTool.readString(file);
				} else {
					try {
						String json=Network.httpGet(url, cookie, referer);
						FileOutputStream fos=new FileOutputStream(file);
						fos.write(json.getBytes(StandardCharsets.UTF_8));
						fos.flush();
						fos.close();
						return json;
					} catch (Exception e) {

					}
					return null;
				}
			case CacheRefresh:
				if (file.exists()) {
					String s = FileTool.readString(file);
					MainActivity.instance.threadPool.execute(new Runnable(){

							@Override
							public void run() {
								try {
									String json=Network.httpGet(url, cookie, referer);
									FileOutputStream fos=new FileOutputStream(file);
									fos.write(json.getBytes(StandardCharsets.UTF_8));
									fos.flush();
									fos.close();
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							}
						});
					return s;
				} else {
					try {
						String json=Network.httpGet(url, cookie, referer);
						FileOutputStream fos=new FileOutputStream(file);
						fos.write(json.getBytes(StandardCharsets.UTF_8));
						fos.flush();
						fos.close();
						return json;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			case NoCache:
				return Network.httpGet(url, cookie, referer);
		}
		return null;
	}
	/*
	 switch (picMode) {
	 case CacheOnly:
	 break;
	 case CachePrefer:
	 break;
	 case CacheRefresh:
	 break;
	 case NoCache:
	 break;
	 }
	 */

}
