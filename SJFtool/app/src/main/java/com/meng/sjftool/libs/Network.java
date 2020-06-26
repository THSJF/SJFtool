package com.meng.sjftool.libs;

import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.lib.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import org.jsoup.*;

public class Network {
	
	private static String bilibiliPost(String url, String cookie, Map<String,String> headers, Object... params) {
		Connection connection = Jsoup.connect(url);
		connection.userAgent(MainActivity.instance.userAgent);
		if (headers != null) {
			connection.headers(headers);
		}
		if (cookie != null) {
			connection.cookies(cookieToMap(cookie));
		}
		connection.ignoreContentType(true)
			.method(Connection.Method.POST)
			.data(params);
		Connection.Response response=null;
		try {
			response = connection.execute();
		} catch (IOException e) {
			MainActivity.instance.showToast("连接出错");
			return null;
		}
		if (response.statusCode() != 200) {
			MainActivity.instance.showToast(String.valueOf(response.statusCode()));
		}
		Log.network(Connection.Method.POST, url, formatJson(response.body()), params);
		return response.body();
	}

	public static String bilibiliPost(String url, String cookie, Object... params) {
		return bilibiliPost(url, cookie, null, params);
	}

	public static String bilibiliMainPost(String url, String cookie, Object... params) {
		return bilibiliPost(url, cookie, Bilibili.mainHead, params);
	}

	public static String bilibiliLivePost(String url, String cookie, Object... params) {
		return bilibiliPost(url, cookie, Bilibili.liveHead, params);
	}

	public static Map<String, String> cookieToMap(String value) {
		Map<String, String> map = new HashMap<>();
		String[] values = value.split("; ");
		for (String val : values) {
			String[] vals = val.split("=");
			if (vals.length == 2) {
				map.put(vals[0], vals[1]);
			} else if (vals.length == 1) {
				map.put(vals[0], "");
			}
		}
		return map;
	}

	public static String getRealUrl(String surl) throws Exception {
		URL url = new URL(surl);
		URLConnection conn = url.openConnection();
		conn.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
		String nurl = conn.getURL().toString();
		in.close();
		return nurl;
	}

	public static String httpGet(String url) {
		return httpGet(url, null, null);
	}

	public static String httpGet(String url, String cookie) {
		return httpGet(url, cookie, null);
	}

	public static String httpGet(String url, String cookie, String refer) {
		Connection.Response response = null;
		Connection connection;
		try {
			connection = Jsoup.connect(url);
			if (cookie != null) {
				connection.cookies(cookieToMap(cookie));
			}
			if (refer != null) {
				connection.referrer(refer);
			}
			connection.userAgent(MainActivity.instance.userAgent);
			connection.ignoreContentType(true).method(Connection.Method.GET);
			response = connection.execute();
			if (response.statusCode() != 200) {
				MainActivity.instance.showToast(String.valueOf(response.statusCode()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//	if (!url.contains("/room/v1/Area/getList")) {
		Log.network(Connection.Method.GET, url, formatJson(response.body()));
		//	}
		return response.body();
	}

	public static String formatJson(String content) {
		if (content == null) {
			return "{}";
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		int count = 0;
		while (index < content.length()) {
			char ch = content.charAt(index);
			if (ch == '{' || ch == '[') {
				sb.append(ch);
				sb.append('\n');
				count++;
				for (int i = 0; i < count; i++) {                   
					sb.append('\t');
				}
			} else if (ch == '}' || ch == ']') {
				sb.append('\n');
				count--;
				for (int i = 0; i < count; i++) {                   
					sb.append('\t');
				}
				sb.append(ch);
			} else if (ch == ',') {
				sb.append(ch);
				sb.append('\n');
				for (int i = 0; i < count; i++) {                   
					sb.append('\t');
				}
			} else {
				sb.append(ch);              
			}
			index++;
		}
		return sb.toString();
	}
	/**
	 * 把格式化的json紧凑
	 * @param content
	 * @return
	 */
	public static String compactJson(String content) {
		String regEx="[\t\n]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(content);
		return m.replaceAll("").trim();
	}
}
