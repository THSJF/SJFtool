package com.meng.sjftool.bilibili.result;
import com.meng.sjftool.libs.*;

public class DynamicWithPictureResult {
//	{
//		"code": 0,
//		"msg": "",
//		"message": "",
//		"data": {
//			"doc_id": "72258228",
//			"dynamic_id": 385763031840858400,
//			"dynamic_id_str": "385763031840858377",
//			"_gt_": 0
//		}
//	}
	public int code;
	public String message;
	public String msg;
	public Data data;

	public class Data {
		public String doc_id;
		public long dynamic_id;
		public String dynamic_id_str;
		public int _gt_;
	}

	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
