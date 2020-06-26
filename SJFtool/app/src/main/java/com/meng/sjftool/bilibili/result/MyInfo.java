package com.meng.sjftool.bilibili.result;

import com.meng.sjftool.libs.*;

public class MyInfo {
    public int code;
    public String message;
    public int ttl;
    public Data data;

    public class Data {
        public long mid;
        public String name;
        public String sex;
        public String face;
        public String sign;
        public int rank;
        public int level;
        public long jointime;
        public int moral;
        public int silence;
        public int email_status;
        public int tel_status;
        public int identification;
        public Vip vip;
        public Pendant pendant;
        public Official official;
        public long birthday;
        public int is_tourist;
        public LevelExp level_exp;
        public float coins;
        public int following;
        public int follower;
    }

    public class LevelExp {
        public int current_level;
        public int current_min;
        public int current_exp;
        public int next_exp;
    }

    public class Nameplate {
        public long nid;
        public String name;
        public String image;
        public String image_small;
        public String level;
        public String condition;
    }

    public class Official {
        public int role;
        public String title;
        public String desc;
    }

    public class Pendant {
        public int pid;
        public String name;
        public String image;
        public int expire;
    }

    public class Vip {
        public int type;
        public int status;
        public long due_date;
        public int vip_pay_type;
        public int theme_type;
    }
	
	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
