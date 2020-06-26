package com.meng.sjftool.bilibili.tasks;

import com.google.gson.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.libs.*;

public class AutoSign implements Runnable {

	@Override
	public void run() {
		StringBuilder sb= new StringBuilder();
		for (AccountInfo ai:AccountManager.iterate()) {
			if (!Tools.Time.getDate().equals(Tools.Time.getDate(ai.lastSign))) {
				ai.setSigned(false);
			}
			if (!ai.isSigned() && !ai.isCookieExceed()) {
				int rc =new JsonParser().parse(Tools.BilibiliTool.sendLiveSign(ai.cookie)).getAsJsonObject().get("code").getAsInt();
				ai.lastSign = System.currentTimeMillis();
				switch (rc) {
					case -101:
						ai.setCookieExceed(true);
						sb.append("\n").append(ai.name).append(":cookie过期");
						break;
					case 0:
						ai.lastSign = System.currentTimeMillis();
						sb.append("\n").append(ai.name).append(":成功");
						ai.setSigned(true);
						break;
					case 1011040:
						sb.append("\n").append(ai.name).append(":已在其他设备签到");
						ai.setSigned(true);
						break;
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {}
			} else if (ai.isSigned()) {
				sb.append("\n").append(ai.name).append(":今日已签到");
			} else if (ai.isCookieExceed()) {
				sb.append("\n").append(ai.name).append(":cookie过期");
			} else {
				sb.append("\n").append(ai.name).append(":未知错误");
			}
		}
		MainActivity.instance.showToast("自动签到:" + sb.toString());
		AccountManager.saveConfig();
	}
}
