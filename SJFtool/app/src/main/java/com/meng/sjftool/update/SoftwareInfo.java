package com.meng.sjftool.update;

import java.util.*;

public class SoftwareInfo {
	public String lastestVersionName;
	public int lastestVersionCode;
	public int lastestSize;
	public ArrayList<VersionInfo> infoList=new ArrayList<>();

	public class VersionInfo {
		public String versionName;
		public int versionCode;
		public String versionDescribe;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for (VersionInfo si:infoList) {
			sb.append("v").append(si.versionName).append(":").append(si.versionDescribe).append("\n");
		}
		sb.append(String.format("最新版本大小:%.2f", lastestSize / 1024f / 1024f)).append("M");
		return sb.toString();
	}
}
