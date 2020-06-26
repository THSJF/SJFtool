package com.meng.sjftool.bilibili.result;

import com.meng.sjftool.libs.*;

public class UserInfo {
    public int code;
    public String message;
    public Data data;

    public class Data {
        public long mid;
        public String name;
        public String sex;
        public String face;
        public String sign;
        public int rank;
        public int level;
        public int jointime;
        public int moral;
        public int silence;
        public String birthday;
        public float coins;
        public Official official;
        public Vip vip;
        public boolean is_followed;
        public String top_photo;
        public Theme theme;
    }

    public class Official {
        public int mid;
        public String title;
        public String desc;
    }

    public class Theme {
    }

    public class Vip {
        public int type;
        public int status;
        public int theme_type;
    }
	
	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
