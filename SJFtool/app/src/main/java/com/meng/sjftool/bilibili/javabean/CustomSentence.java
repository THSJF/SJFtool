package com.meng.sjftool.bilibili.javabean;

import com.meng.sjftool.libs.*;
import java.util.*;

public class CustomSentence {
	public ArrayList<String> sent = new ArrayList<String>();

	@Override
	public String toString() {
		return GSON.toJson(this);
	}
}
