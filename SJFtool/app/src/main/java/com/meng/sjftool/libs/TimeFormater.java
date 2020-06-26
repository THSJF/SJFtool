package com.meng.sjftool.libs;

import java.text.*;
import java.util.*;
import com.meng.*;

public class TimeFormater {

	public static String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String getTime(long timeStamp) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeStamp));
	}

	public static String getDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static String getDate(long timeStamp) {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStamp));
	}

	public static String formatDate(int y, int m, int d) {
		return getDate(new Date(y, m, d).getTime());
	}
}
