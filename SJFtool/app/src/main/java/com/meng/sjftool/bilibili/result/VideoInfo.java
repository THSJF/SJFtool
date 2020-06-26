package com.meng.sjftool.bilibili.result;

import com.meng.sjftool.bilibili.lib.*;
import java.util.*;

public class VideoInfo {
	public int code; 
	public String message;
	public int ttl;
	public Data data;

	public class Data {
		public String bvid;
		public int aid;
		public int videos;
		public int tid;
		public String tname; 
		public int copyright; 
		public String pic;
		public String title;
		public int pubdate;
		public int ctime;
		public String desc;
		public int state;
		public int attribute;
		public int duration;
		public Rights rights;
		public Owner owner;
		public Stat stat;
		public String dynamic;
		public int cid;
		public Dimension dimension;
		public boolean no_cache;
		public ArrayList<Page> pages;
		public Subtitle subtitle;

		public class Rights {
			public int bp; 
			public int elec;
			public int download; 
			public int movie;
			public int pay;
			public int hd5;
			public int no_reprint;
			public int autoplay;
			public int ugc_pay;
			public int is_cooperation;
			public int ugc_pay_preview;
			public int no_background;
		}

		public class Owner {
			public int mid;
			public String name;
			public String face;
		}

		public class Stat {
			public int aid;
			public int view;
			public int danmaku;
			public int reply;
			public int favorite;
			public int coin;
			public int share;
			public int now_rank;
			public int his_rank;
			public int like;
			public int dislike;
			public String evaluation;	
		}

		public class Dimension {
			public int width;
			public int height;
			public int rotate;
		}

		public class Page {
			public int cid;
			public int page;
			public String from;
			public String part;
			public int duration;
			public String vid;
			public String weblin;
			public Dimension dimension;
		}

		public class Subtitle {
			public String allow_submit;
			ArrayList list;
		}
	}

	@Override
	public String toString() {
		int days = (int) ((System.currentTimeMillis() / 1000 - data.pubdate) / 86400);
		return String.format("AV%s  %s\n标题:%s\nup:%s\n%s次播放,%s条弹幕,%s条评论,%s个硬币,%s次分享,%s人收藏,%s人赞,%s", data.stat.aid, AvBvConverter.getInstance().encode(data.stat.aid), data.title, data.owner.name, data.stat.view, data.stat.danmaku, data.stat.reply, data.stat.coin, data.stat.share, data.stat.favorite, data.stat.like, days == 0 ?"24小时内发布.": days + "天前发布，平均每天" + ((float)data.stat.view / days) + "次播放.");
	}
}
