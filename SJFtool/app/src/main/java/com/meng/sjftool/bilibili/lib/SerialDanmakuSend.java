package com.meng.sjftool.bilibili.lib;

import java.util.*;

public class SerialDanmakuSend implements Runnable {
	private long id;
	private HashMap<String,Long> sendHistory=new HashMap<>();
	private ArrayList<String> cookieList=new ArrayList<>();
	private ArrayList<String> msgList=new ArrayList<>();
	private int position=0;

	public void add(String msg, String cookie, long roomId) {
		cookieList.add(cookie);
		sendHistory.put(cookie, 0L);
		msgList.add(msg);
		id = roomId;
	}

	@Override
	public void run() {
		while (position < cookieList.size()) {
			String cookie=cookieList.get(position);
			if (System.currentTimeMillis() - sendHistory.get(cookie) > 2000) {
				Bilibili.sendLiveDanmaku(msgList.get(position), cookie, id);
				sendHistory.put(cookie, System.currentTimeMillis());
				++position;
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {

				}
			}
		}
	}

}
