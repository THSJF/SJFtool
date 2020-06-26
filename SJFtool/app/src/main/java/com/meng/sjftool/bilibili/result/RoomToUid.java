package com.meng.sjftool.bilibili.result;
import com.meng.sjftool.libs.*;
import java.util.*;

public class RoomToUid {
//	{
//		"code": 0,
//		"msg": "success",
//		"message": "success",
//		"data": {
//			"info": {
//				"uid": 64483321,
//				"uname": "妖怪之山的厄神",
//				"face": "https://i1.hdslb.com/bfs/face/74e9f1cbb3cd890b197562f6361a84f6b1271ed7.jpg",
//				"rank": "10000",
//				"platform_user_level": 5,
//				"mobile_verify": 1,
//				"identification": 1,
//				"official_verify": {
//					"type": -1,
//					"desc": "",
//					"role": 0
//				},
//				"vip_type": 0,
//				"gender": -1
//			},
//			"level": {
//				"uid": 64483321,
//				"cost": 43706500,
//				"rcost": 7608930,
//				"user_score": "0",
//				"vip": 0,
//				"vip_time": "2020-04-13 00:00:51",
//				"svip": 0,
//				"svip_time": "0000-00-00 00:00:00",
//				"update_time": "2020-05-07 20:13:47",
//				"master_level": {
//					"level": 13,
//					"color": 5805790,
//					"current": [16800, 56410],
//					"next": [22400, 78810],
//					"anchor_score": 76089,
//					"upgrade_score": 2721,
//					"master_level_color": 5805790,
//					"sort": ">10000"
//				},
//				"user_level": 24,
//				"color": 5805790,
//				"anchor_score": 76089
//			},
//			"san": 12
//		}
//	}
	public int code;
    public String msg;
    public String message;
    public Data data;
	
    public class Data {
		public Info info;
		public Level level;
		public int san;
    }

	public class Info {
		public long uid;
        public String uname;
        public String face;
        public String rank;
        public int platform_user_level;
        public int mobile_verify;
        public int identification;
		public OfficialVerify official_verify;
		public int vip_type;
		public int gender;
	}

	public class OfficialVerify {
        public int type;
        public String desc;
        public String roll;
    }

	public class Level {
		public long uid;
        public long cost;
        public long rcost;
		public String user_score;
        public int vip;
        public String vip_time;
		public int svip;
        public String svip_time;
		public String update_time;
		public MasterLevel master_level;
		public int user_level;
        public int color;
        public int anchor_score;
	}

	public class MasterLevel {
		public int level;
        public int color;
		public ArrayList<Integer> current;
		public ArrayList<Integer> next;
		public int anchor_score;
        public int upgrade_score;
        public int master_level_color;
		public String sort;
	}
	
	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
