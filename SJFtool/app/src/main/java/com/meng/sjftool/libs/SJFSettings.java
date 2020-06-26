package com.meng.sjftool.libs;

import android.content.*;

public class SJFSettings {
	private SharedPreferences sp;

	public SJFSettings(Context c) {
		sp = c.getSharedPreferences("settings", 0);
	}

	public String getTheme() {
		return sp.getString("theme", "Holo");
	}

	public void setTheme(String v) {
		putString("theme", v);
	}

	public String getVersion() {
		return sp.getString("newVersion", "0.0.0");
	}

	public void setVersion(String v) {
		putString("newVersion", v);
	}

	public long getMainAccount() {
		try {
			return sp.getLong("mainAccount", -1);
		} catch (ClassCastException e) {
			return Long.parseLong(sp.getString("mainAccount", "-1"));
		}
	}

	public void setSaveDebugLog(boolean b){
		putBoolean("debugLog",b);
	}
	
	public boolean isSaveDebugLog(){
		return sp.getBoolean("debugLog",false);
	}
	
	public void setMainAccount(long ac) {
		putLong("mainAccount", ac);
	}

	public boolean getShowNotify() {
		return sp.getBoolean("notifi", false);
	}

	public void setShowNotify(boolean b) {
		putBoolean("notifi", b);
	}

	public void setOpenDrawer(boolean b) {
		putBoolean("opendraw", b);
	}

	public boolean getOpenDrawer() {
		return sp.getBoolean("opendraw", true);
	}

	public boolean getExit0() {
		return sp.getBoolean("exit", false);
	}

	public void setExit0(boolean b) {
		putBoolean("exit", b);
	}

	public boolean isUseNetLog() {
		return sp.getBoolean("netLog", false);
	}

	public void setUseNetLog(boolean use) {
		putBoolean("netLog", use);
	}

	public String getPicCacheMode() {
		return sp.getString("cacheModePic", "CachePrefer");
	}

	public void setPicCacheMode(String s) {
		putString("cacheModePic", s);
	}

	public String getJsonCacheMode() {
		return sp.getString("cacheModeJson", "NoCache");
	}

	public void setJsonCacheMode(String s) {
		putString("cacheModeJson", s);
	}

	private void putLong(String key, long value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.apply();
	}

	private void putBoolean(String key, Boolean value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	private void putString(String key, String value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.apply();
	}
}
