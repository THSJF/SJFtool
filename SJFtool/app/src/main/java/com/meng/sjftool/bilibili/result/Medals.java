package com.meng.sjftool.bilibili.result;
import com.meng.sjftool.libs.*;
import java.util.*;

public class Medals {
//	{
//		"code": 0,
//		"msg": "",
//		"message": "",
//		"data": {
//			"medalCount": 20,
//			"count": 3,
//			"fansMedalList": [
//            {
//                "uid": 64483321,
//                "target_id": 352623,
//                "medal_id": 22371,
//                "score": 99,
//                "level": 1,
//                "intimacy": 99,
//                "status": 0,
//                "source": 1,
//                "receive_channel": 1,
//                "is_receive": 1,
//                "master_status": 1,
//                "receive_time": "2019-05-11 00:27:05",
//                "today_intimacy": 0,
//                "last_wear_time": 0,
//                "medal_name": "華霊",
//                "master_available": 1,
//                "guard_type": 0,
//                "lpl_status": 0,
//                "target_name": "夜桜鎮魂歌",
//                "target_face": "https://i1.hdslb.com/bfs/face/a102a8806ed74af37d35caafa8910222bf24c245.jpg",
//                "live_stream_status": 0,
//                "icon_code": 0,
//                "icon_text": "",
//                "rank": 22,
//                "medal_color": 6406234,
//                "today_feed": 0,
//                "day_limit": 500,
//                "next_intimacy": 201,
//                "todayFeed": 0,
//                "dayLimit": 500,
//                "uname": "夜桜鎮魂歌",
//                "color": 6406234,
//                "medalName": "華霊",
//                "guard_level": 0,
//                "guard_medal_title": "未开启加成",
//                "anchorInfo": {
//                    "uid": 352623,
//                    "uname": "夜桜鎮魂歌",
//                    "gender": 0,
//                    "face": "https://i1.hdslb.com/bfs/face/a102a8806ed74af37d35caafa8910222bf24c245.jpg",
//                    "silence": 0,
//                    "masterVip": 2,
//                    "masterRank": 10000,
//                    "masterLevel": 6,
//                    "masterHeadpic": "https://i0.hdslb.com/bfs/face/295cd9505bfe2edd360becd1ffd70f1870505696.png",
//                    "masterVerify": -1,
//                    "mobileVerified": 1,
//                    "identification": 1,
//                    "official": {
//                        "role": 0,
//                        "title": "",
//                        "desc": ""
//                    },
//                    "rank": 10000,
//                    "platform_user_level": 6,
//                    "vip_type": 2,
//                    "mobile_verify": 0,
//                    "official_verify": {
//                        "role": 0,
//                        "title": "",
//                        "desc": "",
//                        "type": -1
//                    }
//                },
//                "roomid": 475904
//            },
//            {
//                "uid": 64483321,
//                "target_id": 100039025,
//                "medal_id": 97031,
//                "score": 3999,
//                "level": 6,
//                "intimacy": 1298,
//                "status": 0,
//                "source": 1,
//                "receive_channel": 5,
//                "is_receive": 1,
//                "master_status": 1,
//                "receive_time": "2019-08-14 14:25:56",
//                "today_intimacy": 0,
//                "last_wear_time": 0,
//                "medal_name": "发发发",
//                "master_available": 1,
//                "guard_type": 0,
//                "lpl_status": 0,
//                "target_name": "阿发大官人",
//                "target_face": "https://i0.hdslb.com/bfs/face/b29a6c79ad08411f18c0de1e09ab7a62f4e92bd7.jpg",
//                "live_stream_status": 0,
//                "icon_code": 0,
//                "icon_text": "",
//                "rank": 12,
//                "medal_color": 5805790,
//                "today_feed": 0,
//                "day_limit": 1000,
//                "next_intimacy": 1500,
//                "todayFeed": 0,
//                "dayLimit": 1000,
//                "uname": "阿发大官人",
//                "color": 5805790,
//                "medalName": "发发发",
//                "guard_level": 0,
//                "guard_medal_title": "未开启加成",
//                "anchorInfo": {
//                    "uid": 100039025,
//                    "uname": "阿发大官人",
//                    "gender": 1,
//                    "face": "https://i0.hdslb.com/bfs/face/b29a6c79ad08411f18c0de1e09ab7a62f4e92bd7.jpg",
//                    "silence": 0,
//                    "masterVip": 0,
//                    "masterRank": 10000,
//                    "masterLevel": 4,
//                    "masterHeadpic": "",
//                    "masterVerify": -1,
//                    "mobileVerified": 1,
//                    "identification": 1,
//                    "official": {
//                        "role": 0,
//                        "title": "",
//                        "desc": ""
//                    },
//                    "rank": 10000,
//                    "platform_user_level": 4,
//                    "vip_type": 0,
//                    "mobile_verify": 0,
//                    "official_verify": {
//                        "role": 0,
//                        "title": "",
//                        "desc": "",
//                        "type": -1
//                    }
//                },
//                "roomid": 7611192
//            },
//            {
//                "uid": 64483321,
//                "target_id": 21895119,
//                "medal_id": 207900,
//                "score": 234631,
//                "level": 17,
//                "intimacy": 84730,
//                "status": 1,
//                "source": 1,
//                "receive_channel": 5,
//                "is_receive": 1,
//                "master_status": 1,
//                "receive_time": "2019-08-01 10:40:35",
//                "today_intimacy": 1558,
//                "last_wear_time": 0,
//                "medal_name": "台混",
//                "master_available": 1,
//                "guard_type": 0,
//                "lpl_status": 0,
//                "target_name": "散落的烛光",
//                "target_face": "https://i0.hdslb.com/bfs/face/3ffad562fb6413fb62db821b0226272afa6d3713.jpg",
//                "live_stream_status": 0,
//                "icon_code": 0,
//                "icon_text": "",
//                "rank": 1,
//                "medal_color": 16752445,
//                "today_feed": 1558,
//                "day_limit": 2000,
//                "next_intimacy": 100000,
//                "todayFeed": 1558,
//                "dayLimit": 2000,
//                "uname": "散落的烛光",
//                "color": 16752445,
//                "medalName": "台混",
//                "guard_level": 0,
//                "guard_medal_title": "未开启加成",
//                "anchorInfo": {
//                    "uid": 21895119,
//                    "uname": "散落的烛光",
//                    "gender": -1,
//                    "face": "https://i0.hdslb.com/bfs/face/3ffad562fb6413fb62db821b0226272afa6d3713.jpg",
//                    "silence": 0,
//                    "masterVip": 2,
//                    "masterRank": 10000,
//                    "masterLevel": 5,
//                    "masterHeadpic": "https://i0.hdslb.com/bfs/face/1cdf174c75dd6493f3c8f0797e972b69e3293870.png",
//                    "masterVerify": -1,
//                    "mobileVerified": 1,
//                    "identification": 1,
//                    "official": {
//                        "role": 0,
//                        "title": "",
//                        "desc": ""
//                    },
//                    "rank": 10000,
//                    "platform_user_level": 5,
//                    "vip_type": 2,
//                    "mobile_verify": 0,
//                    "official_verify": {
//                        "role": 0,
//                        "title": "",
//                        "desc": "",
//                        "type": -1
//                    }
//                },
//                "roomid": 2409909
//            }
//			],
//			"name": "妖怪之山的厄神",
//			"pageinfo": {
//				"totalpages": 1,
//				"curPage": 1
//			}
//		}
//	}
	public int code;
    public String msg;
    public String message;
    public Data data = new Data();

    public class Data {
		public int medalCount;
		public int count;
		public ArrayList<FansMedal> fansMedalList = new ArrayList<>();
		public String name;
		public PageInfo pageinfo;
    }

	public class FansMedal {
		public long uid;
        public long target_id;
        public int medal_id;
		public int score;
        public int level;
        public int intimacy;
		public int status;
        public int source;
        public int receive_channel;
		public int is_receive;
        public String receive_time;
		public int today_intimacy;
        public int last_wear_time;
		public String medal_name;
		public int master_available;
        public int guard_type;
		public int lpl_status;
		public String target_name;
		public String target_face;
		public int live_stream_status;
		public int icon_code;
		public String icon_text;
		public int rank;
		public int medal_color;
		public int today_feed;
		public int day_limit;
		public int next_intimacy;
		public int todayFeed;
		public int dayLimit;
		public String uname;
		public int color;
		public String medalName;
		public int guard_level;
		public String guard_medal_title;
		public AnchorInfo anchorInfo;
		public long roomid;
	}

	public class AnchorInfo {
		public long uid;
        public String uname;
		public int gender;
        public String face;
        public int silence;
        public int masterVip;
        public int masterRank;
        public int masterLevel;
		public String masterHeadpic;
		public int masterVerify;
		public int mobileVerified;
        public int identification;
        public Official official;
		public int rank;
        public int platform_user_level;
        public int vip_type;
        public int mobile_verify;
		public OfficialVerify official_verify;

	}

	public class Official {
        public int role;
        public String title;
        public String desc;
    }

	public class OfficialVerify {
		public int role;
		public String title;
		public String desc;
        public int type;  
    }

	public class PageInfo {
		public int totalpages;
		public int curPage;
	}

	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
