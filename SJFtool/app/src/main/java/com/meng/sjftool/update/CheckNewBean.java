package com.meng.sjftool.update;

public class CheckNewBean {
	
	public String packageName;
	public int nowVersionCode;

	public CheckNewBean(String packageName, int nowVersionCode) {
		this.packageName = packageName;
		this.nowVersionCode = nowVersionCode;
	}
}
