package com.meng.sjftool.bilibili.result;
import java.util.*;

public class StartLive {
//	{
//		"code":0,
//		"msg":"",
//		"message":"",
//		"data":{
//			"change":1,
//			"status":"LIVE",
//			"room_type":0,
//			"rtmp":{
//				"addr":"rtmp://txy.live-send.acg.tv/live-txy/",
//				"code":"?streamname=live_64483321_2582558&key=7776fcf83eb2bb7883733f598285caf7",
//				"new_link":"http://tcdns.myqcloud.com:8086/bilibili_redirect?up_rtmp=txy.live-send.acg.tv%2Flive-txy%2F%3Fstreamname%3Dlive_64483321_2582558%26key%3D7776fcf83eb2bb7883733f598285caf7",
//				"provider":"txy"
//			},
//			"protocols":[
//			{
//				"protocol":"rtmp",
//				"addr":"rtmp://txy.live-send.acg.tv/live-txy/",
//				"code":"?streamname=live_64483321_2582558&key=7776fcf83eb2bb7883733f598285caf7",
//				"new_link":"http://tcdns.myqcloud.com:8086/bilibili_redirect?up_rtmp=txy.live-send.acg.tv%2Flive-txy%2F%3Fstreamname%3Dlive_64483321_2582558%26key%3D7776fcf83eb2bb7883733f598285caf7",
//				"provider":"txy"
//			}
//			],
//			"try_time":"0000-00-00 00:00:00",
//			"live_key":"l:one:live:record:9721948:1589025078",
//			"notice":{
//				"type":1,
//				"status":0,
//				"title":"",
//				"msg":"",
//				"button_text":"",
//				"button_url":""
//			}
//		}
//	}

	public int code;
	public String message;
	public String msg;
	public Data data;

	public class Data {
		public int change;
		public String status;
		public int room_type;
		public Rtmp rtmp;
		public ArrayList<Protocols> protocols;
		public String try_time;
		public String live_key;
		public Notice notice;
	}

	public class Rtmp {
		public String addr;
		public String code;
		public String new_link;
		public String provider;
	}

	public class Protocols {
		public String protocol;
		public String addr;
		public String code;
		public String new_link;
		public String provider;
	}

	public class Notice {
		public int type;
		public int status;
		public String title;
		public String msg;
		public String button_text;
		public String button_url;
	}
}
