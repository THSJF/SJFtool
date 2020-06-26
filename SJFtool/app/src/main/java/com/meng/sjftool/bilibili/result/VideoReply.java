package com.meng.sjftool.bilibili.result;

import com.meng.sjftool.libs.*;
import java.util.*;

public class VideoReply {
	public int code;
    public String message;
    public int ttl;
	public Data data;

	public class Data {
		public Page page;
		public Config config;
		public ArrayList<Reply> replies;
		public Object hots;
		public Upper uper;
		public Object top;
		public Object notice;
		public int vote;
		public int blacklist;
		public int assist;
		public int mode;
		ArrayList<Integer> support_mode;
		public Folder folder;
		public Object lottery_card;
		public boolean show_bvid;
		public Control control;
	}

	public class Page {
		public int num;
		public int size;
		public int count;
		public int acount;
	}

	public class Config {
		public int showadmin;
		public int showentry;
		public int showfloor;
		public int showtopic;
		public boolean show_up_flag;
		public boolean read_only;
		public boolean show_del_log;
	}

	public class Reply {
		public long rpid;
		public long oid;
		public int type;
		public long mid;
		public long root;
		public long parent;
		public long dialog;
		public int count;
		public int rcount;
		public int state;
		public int fansgrade;
		public int attr;
		public long ctime;
		public String rpid_str;
		public String root_str;
		public String parent_str;
		public int like;
		public int action;
		public Member member;
		public Content content;
		public ArrayList<Reply> replies;
		public int assist;
		public Folder folder;
		public UpAction up_action;
		public boolean show_follow;
	}

	public class UpAction {
		public boolean like;
		public boolean reply;  
	}

	public class Member {
		public String mid;
		public String uname;
		public String sex;
		public String sign;
		public String avatar;
		public String rank;
		public String DisplayRank;
		public LevelInfo level_info;
		public Pendant pendant;
		public Nameplate nameplate;
		public OfficialVerify official_verify;
		public Vip vip;
		public Object fans_detail;
		public int following;
		public UserSailing user_sailing;
	}

	public class Folder {
		public boolean has_folded;
		public boolean is_folded;
		public String rule;
	}

	public class Pendant {
		public int pid;
		public String name;
		public String image;
		public int expire;
		public String image_enhance;
	}

	public class OfficialVerify {
		public int type;
		public String desc;
	}

	public class LevelInfo {
		public int current_level;
		public int current_min;
		public int current_exp;
		public int next_exp;
	}

	public class Nameplate {
		public int nid;
		public String name;
		public String image;
		public String image_small;
		public String level;
		public String condition;
	}

	public class Vip {
		public int vipType;
		public long vipDueDate;
		public String dueRemark;
		public int accessStatus;
		public int vipStatus;
		public String vipStatusWarn;
		public int themeType;
		public Lable lable;
	}

	public class Lable {
		public String path;
	}

	public class Content {
		public String message;
		public int plat;
		public String device;
		public Object members;
		public Object jump_url;
		public int max_line;
	}

	public class UserSailing {
		public Object pendant;
		public Object cardbg;
		public Object cardbg_with_focus;
	}

	public class Upper {
		public long mid;
		public Object top;
		public Object vote;
	}

	public class Control {
		public boolean input_disable;
		public String root_input_text;
		public String child_input_text;
		public String bg_text;
		public boolean web_selection;
		public String answer_guide_text;
		public String answer_guide_icon_url;
		public String answer_guide_ios_url;
		public String answer_guide_android_url;
	}
	
	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
