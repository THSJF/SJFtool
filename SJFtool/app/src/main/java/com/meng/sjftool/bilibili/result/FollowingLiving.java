package com.meng.sjftool.bilibili.result;
import com.meng.sjftool.libs.*;
import java.util.*;

public class FollowingLiving {
//	{
//		"code":0,
//		"msg":"success",
//		"message":"success",
//		"data":{
//			"results":1,
//			"page":"1",
//			"pagesize":"10",
//			"list":[
//            {
//                "cover":"https://i2.hdslb.com/bfs/face/c106a6eda4c7eca3add4ad8b63870ff9dcd04f80.jpg",
//                "face":"https://i2.hdslb.com/bfs/face/c106a6eda4c7eca3add4ad8b63870ff9dcd04f80.jpg",
//                "uname":"欧艾露汐",
//                "title":"testing",
//                "roomid":3241620,
//                "pic":"https://i0.hdslb.com/bfs/live/2417bd2db9cac291f309b7bfdadb3426521e8715.jpg",
//                "online":0,
//                "link":"https://live.bilibili.com/3241620"
//            }
//			]
//		}
//	}
	public int code;
    public String msg;
    public String message;
    public Data data;

    public class Data {
		public int result;
		public String page;
		public String pagesize;
		public ArrayList<Item> list;
    }

	public class Item {
		public String cover;
		public String face;
		public String uname;
		public String title;
		public long roomid;
		public String pic;
		public int online;
		public String link;
	}

	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
