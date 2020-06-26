package com.meng.sjftool.bilibili.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.lib.*;
import java.util.regex.*;

public class ABquickConvert extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
		// boolean readonly = getIntent().getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);
		String s=null;
		if (text == null) {
			finish();
		}
		s = text.toString();
		long avid=getAVId(s);
		AvBvConverter cvt=AvBvConverter.getInstance();
		if (avid != -1) {
			String encode = cvt.encode(avid);
			((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text", encode));
			Toast.makeText(this, "已复制" + encode + "到剪贴板", Toast.LENGTH_SHORT).show();
			finish();
		}
		String bvid=getBVId(s);
		if (bvid != null) {
			long decode = cvt.decode(bvid);
			((ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text", "av" + decode));
			Toast.makeText(this, "已复制av" + decode + "到剪贴板", Toast.LENGTH_SHORT).show();
			finish();
		}
		Toast.makeText(this, "没有发现有效的链接", Toast.LENGTH_SHORT);
	}

	private long getAVId(String s) {  
		Matcher matcher = Pattern.compile(MainActivity.regAv).matcher(s);  
		if (matcher.find()) {  
			return Long.parseLong(matcher.group(1));
		}
		return -1;
	}

	private String getBVId(String s) {  
		Matcher matcher = Pattern.compile(MainActivity.regBv).matcher(s);  
		if (matcher.find()) {  
			return matcher.group(1);
		}
		return null;
	}
}
