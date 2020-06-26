package com.meng.sjftool.bilibili.result;
import com.meng.sjftool.libs.*;
import java.util.*;

public class VideoUrl {
	
//	{
//		"code": 0,
//		"message": "0",
//		"ttl": 1,
//		"data": {
//			"from": "local",
//			"result": "suee",
//			"message": "",
//			"quality": 32,
//			"format": "flv480",
//			"timelength": 33759,
//			"accept_format": "flv720_p60,flv720,flv480,flv360",
//			"accept_description": [
//            "高清 720P60",
//            "高清 720P",
//            "清晰 480P",
//            "流畅 360P"
//			],
//			"accept_quality": [
//            74,
//            64,
//            32,
//            16
//			],
//			"video_codecid": 7,
//			"seek_param": "start",
//			"seek_type": "offset",
//			"durl": [
//            {
//                "order": 1,
//                "length": 33759,
//                "size": 4919312,
//                "ahead": "EhA=",
//                "vhead": "AWQAHv/hABhnZAAerNlAoD2hAAADAAEAAAMAPA8WLZYBAARo6+8s",
//                "url": "http://upos-sz-mirrorhw.bilivideo.com/upgcxcode/37/18/84001837/84001837-1-32.flv?e=ig8euxZM2rNcNbhjhbUVhoMz7bNBhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1591510111&gen=playurl&os=hwbv&oi=661437796&trid=7f18e92b615047849eab22966f90ba1du&platform=pc&upsig=1baf63c4330c191ebec0b63509610f9d&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&orderid=0,2&logo=80000000",
//                "backup_url": [
//				"http://upos-sz-mirrorks3c.bilivideo.com/upgcxcode/37/18/84001837/84001837-1-32.flv?e=ig8euxZM2rNcNbhjhbUVhoMz7bNBhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1591510111&gen=playurl&os=ks3cbv&oi=661437796&trid=7f18e92b615047849eab22966f90ba1du&platform=pc&upsig=884b20d2cc6451b4d8d609b574cc55a8&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&orderid=1,2&logo=40000000"
//                ]
//            }
//			]
//		}
//	}
	
	public int code;
	public String message;
	public int ttl;
	public Data data;

	public class Data {
		public String from;
		public String result;
		public String message;
		public int quality;
		public String format;
		public int timelength;
		public String accept_format;
		public ArrayList<String> accept_description;
		public ArrayList<Integer> accept_quality;
		public int video_codecid;
		public String seek_param;
		public String seek_type;
		public ArrayList<Durl> durl;
	}

	public class Durl {
		public int order;
		public long length;
		public long size;
		public String ahead;
		public String vhead;
		public String url;
		public ArrayList<String> backup_url;
	}

	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
