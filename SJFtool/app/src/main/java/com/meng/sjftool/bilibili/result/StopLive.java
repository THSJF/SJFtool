package com.meng.sjftool.bilibili.result;

public class StopLive {
//	{
//		"code":0,
//		"msg":"",
//		"message":"",
//		"data":{
//			"change":1,
//			"status":"PREPARING"
//		}
//	}
	public int code;
	public String message;
	public String msg;
	public Data data;

	public class Data {
		public int change;
		public String status;
	}
}
