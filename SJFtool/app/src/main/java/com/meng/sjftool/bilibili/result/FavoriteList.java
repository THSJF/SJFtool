package com.meng.sjftool.bilibili.result;
import com.meng.sjftool.libs.*;
import java.util.*;

public class FavoriteList {
//	{
//		"status": true,
//		"data": {
//			"list": [{
//				"fav_box": 1014111,
//				"name": "\u9ed8\u8ba4\u6536\u85cf\u5939",
//				"max_count": 50000,
//				"count": 28,
//				"atten_count": 0,
//				"state": 0,
//				"ctime": 1481787029,
//				"videos": [{
//					"aid": 667534180,
//					"pic": "http:\/\/i2.hdslb.com\/bfs\/archive\/88ec7c7d0ecd5edaeeeb7a2fb16737efdc36366b.jpg",
//					"type": 2
//				}, {
//					"aid": 710143864,
//					"pic": "http:\/\/i0.hdslb.com\/bfs\/archive\/988a3999e115b42907b02f092a9b1a9e9e3a2db1.jpg",
//					"type": 2
//				}, {
//					"aid": 36647170,
//					"pic": "http:\/\/i1.hdslb.com\/bfs\/archive\/e633b9c482bbda91548368fb3aa559c91f34ef81.jpg",
//					"type": 2
//				}]
//			}],
//			"count": 1
//		}
//	}
	public boolean status;
	public int count;
	public Data data;

	public class Data {
		public ArrayList<FavBoxItem> list;
	}

	public class FavBoxItem {
		public int fav_box;
		public String name;
		public int max_count;
		public int count;
		public int atten_count;
		public int state;
		public int ctime;
		public ArrayList<VideoItem> videos;
	}

	public class VideoItem {
		public int aid;
		public String pic;
		public int type;
	}

	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
