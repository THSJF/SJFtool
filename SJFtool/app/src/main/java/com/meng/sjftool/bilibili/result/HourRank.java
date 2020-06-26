package com.meng.sjftool.bilibili.result;

import com.meng.sjftool.libs.*;
import java.util.*;

public class HourRank {
    public int code;
    public String message;
    public String msg;
    public Data data;

    public class Data {
        public RankInfo rank_info;
        public ArrayList<Object> realtime_hour_rank_info;
        public ArrayList<DataListItem> list;
    }

    public class RankInfo {
        public String hour_rank_text;
        public int start_timestamp;
        public int end_timestamp;
    }

    public class DataListItem {
        public long id;
        public int score;
        public int rank;
        public long uid;
        public String uname;
        public String face;
        public int prescore;
        public String cover;
        public int live_status;
        public int roomid;
        public String link;
        public String area_v2_name;
        public int area_v2_id;
        public String area_v2_parent_name;
        public int area_v2_parent_id;
        public int trend;
        public int follow_status;
        public String unit;
        public int self;
        public int broadcast_type;
        public int personal_verify;
        public ArrayList<BestAssist> best_assist;
        public Own own;
    }

    public class BestAssist {
        public long uid;
        public String face;
        public String uname;
    }

    public class Own {
        public long id;
        public int score;
        public String rank;
        public long uid;
        public String uname;
        public String face;
        public String cover;
        public String link;
        public int is_show_own;
        public ArrayList<Integer> follow_list;
        public String unit;
        public int prescore;
        public String distance_text;
        public int identification;
        public int broadcast_type;
    }
	
	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
