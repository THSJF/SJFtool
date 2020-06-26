package com.meng.sjftool.bilibili.result;
import java.util.*;

public class LiveStream {
//	{
//		"code":0,
//		"data":{
//			"rtmp":{
//				"addr":"rtmp://txy.live-send.acg.tv/live-txy/",
//				"code":"?streamname=live_64483321_2582558\u0026key=7776fcf83eb2bb7883733f598285caf7"
//			},
//			"stream_line":[
//			{
//				"cdn_name":"txy",
//				"checked":1,
//				"name":"默认线路",
//				"src":8
//			}
//			]
//		},
//		"message":"0"
//	}
	public int code;
	public String message;
	public Data data;

	public class Data {
		public Rtmp rtmp;
		public ArrayList<StreamLine> stream_line;
	}

	public class Rtmp {
		public String addr;
		public String code;
	}

	public class StreamLine {
		public String cdn_name;
		public int checked;
		public String name;
		public int src;
	}
}
