package com.meng.sjftool.bilibili.lib;

import android.content.*;
import android.os.*;
import com.google.gson.reflect.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.util.*;

public class AccountManager {

	private static AccountManager instance;

	private ArrayList<AccountInfo> loginAccounts;
	private String jsonPath;

	public static void init(Context c) {
		if (instance == null) {
			instance = new AccountManager();
		}
		instance.jsonPath = c.getFilesDir() + "/account.json";
		File f = new File(instance.jsonPath);
		if (!f.exists()) {
			instance.saveConfig();
		}
		instance.loginAccounts = GSON.fromJson(FileTool.readString(instance.jsonPath), new TypeToken<ArrayList<AccountInfo>>(){}.getType());
		if (instance.loginAccounts == null) {
			instance.loginAccounts = new ArrayList<>();
		}
	}

	public static boolean contains(long uid) {
		for (AccountInfo a:instance.loginAccounts) {
			if (a.uid == uid) {
				return true;
			}
		}
		return false;
	}

	public static boolean contains(AccountInfo aci) {
		return contains(aci.uid);
	}

	public static void add(AccountInfo aci) {
		instance.loginAccounts.add(aci);
		saveConfig();
	}

	public static void remove(int index) {
		instance.loginAccounts.remove(index);
		saveConfig();
	}

	public static List<AccountInfo> iterate() {
		return instance.loginAccounts;
	}

	public static int size() {
		return instance.loginAccounts.size();
	}

	public static boolean moveUp(int index) {
		if (index == 0) {
			return false;
		}
		instance.loginAccounts.add(index - 1, instance.loginAccounts.remove(index)); 
		saveConfig();
		return true;
	}

	public static boolean moveDown(int index) {
		if (index == instance.loginAccounts.size() - 1) {
			return false;
		}
		instance.loginAccounts.add(index + 1, instance.loginAccounts.remove(index));
		saveConfig();
		return true;
	}

	public static String getCookie(long bid) {
        for (AccountInfo l : instance.loginAccounts) {
            if (bid == l.uid) {
                return l.cookie;
            }
        }
        return null;
    }

	public static AccountInfo get(int index) {
		return instance.loginAccounts.get(index);
	}

	public static void set(int i, AccountInfo account) {
		instance.loginAccounts.set(i, account);
		saveConfig();
	}

	public static AccountInfo getAccount(long id) {
		for (AccountInfo ai:instance.loginAccounts) {
			if (ai.uid == id) {
				return ai;
			}
		}
		return null;
	}

	public static AccountInfo getAccount(String name) {
		for (AccountInfo ai:instance.loginAccounts) {
			if (ai.name.equals(name)) {
				return ai;
			}
		}
		return null;
	}

	public static int getAccountIndex(long uid) {
		for (int i=0;i < instance.loginAccounts.size();++i) {
			if (instance.loginAccounts.get(i).uid == uid) {
				return i;
			}
		}
		return -1;
	}

	/*public int getAccountIndex(String name) {
	 for (int i=0;i < loginAccounts.size();++i) {
	 if (loginAccounts.get(i).name.equals(name)) {
	 return i;
	 }
	 }
	 return -1;
	 }
	 */
    public static void saveConfig() {
        try {
            FileOutputStream fos = null;
            OutputStreamWriter writer = null;
            File file = new File(instance.jsonPath);
            fos = new FileOutputStream(file);
            writer = new OutputStreamWriter(fos, "utf-8");
            writer.write(GSON.toJson(instance.loginAccounts));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		saveConfig2();
    }

	public static void saveConfig2() {
        try {
            FileOutputStream fos = null;
            OutputStreamWriter writer = null;
            File file = new File(Environment.getExternalStorageDirectory() + "/account.json");
            fos = new FileOutputStream(file);
            writer = new OutputStreamWriter(fos, "utf-8");
            writer.write(GSON.toJson(instance.loginAccounts));
            writer.flush();
            fos.close();
		} catch (IOException e) {
            e.printStackTrace();
		}
	}
}
