package com.meng.sjftool.bilibili.javabean;
import com.meng.sjftool.bilibili.enums.*;

public class Recent {
	public IDType type;
	public long id;
	public String name;

	public Recent(IDType type, long id, String name) {
		this.type = type;
		this.id = id;
		this.name = name;
	}
}
