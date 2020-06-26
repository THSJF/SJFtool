package com.meng.sjftool.bilibili.result;

import com.meng.sjftool.libs.*;
import java.util.*;

public class GiftBag {
    public int code;
    public String message;
    public int ttl;
    public Data data;

    public class Data {
        public ArrayList<ListItem> list;
        public long time;
    }

    public class ListItem {
        public int bag_id;
        public int gift_id;
        public String gift_name;
        public int gift_num;
        public int gift_type;
        public long expire_at;
        public String corner_mark;
        public ArrayList<Count_map> count_map;
        public int type;
        public String card_image;
        public String card_gif;
        public int card_id;
        public int card_record_id;
        public boolean is_show_send;
    }

    public class Count_map {
        public long num;
        public String text;
    }

	@Override
	public String toString() {
		return GSON.toJson(this);
	}

	public int getStripCount() {
		int i=0;
		for (ListItem item:data.list) {
			if (item.gift_name.equals("辣条")) {
				i += item.gift_num;
			}
		}
		return i;
	}
}
