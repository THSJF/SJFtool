package com.meng.sjftool.bilibili.result;

import java.util.*;

public class CvInfo {
	public int code;
	public String message;
	public int ttl;
	public Data data;

	public class Data {
		public int like;
		public boolean attention;
		public boolean favorite;
		public int coins;

		public Stats stats;

		public String title;
		public String banner_url;
		public long mid;
		public String author_name;
		public boolean is_author;
		public ArrayList<String> image_urls;
		public ArrayList<String> origin_image_urls;
		public boolean shareable;
		public boolean show_later_watch;
		public boolean show_small_window;
		public boolean in_list;
		public int pre;
		public int next;
		public ArrayList<Channals> share_channels;

		public class Stats {
			public int view;
			public int favorite;
			public int like;
			public int dislike;
			public int reply;
			public int share;
			public int coin;
			public int dynamic;
		}

		public class Channals {
			public String name;
			public String picture;
			public String share_channel;
		}
	}

	@Override
	public String toString() {
		return String.format("标题:%s,\n作者:%s,\n浏览:%d次,点赞:%d次,收藏:%d次,硬币:%d个,%d条回复", data.title, data.author_name, data.stats.view, data.stats.like, data.stats.favorite, data.stats.coin, data.stats.reply);
	}

}
