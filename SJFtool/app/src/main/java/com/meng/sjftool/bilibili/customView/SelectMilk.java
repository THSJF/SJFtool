package com.meng.sjftool.bilibili.customView;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.bilibili.lib.*;
import java.util.*;

public class SelectMilk extends LinearLayout {

	private EditText et;
	private Spinner sp;
	private Dialog editSerialDialog;
	private long id;
	private ArrayList<SendBean> msgList=new ArrayList<>();
	private LinearLayout editSerialView;

	private TextView tv;
	public SelectMilk(final Context c, long id) {
		super(c);
		this.id = id;
		tv = new TextView(c);
		this.addView(tv);
		editSerialView = (LinearLayout) LayoutInflater.from(c).inflate(R.layout.add_serial, null);
		et = (EditText) editSerialView.findViewById(R.id.add_serialEditText);
		sp = (Spinner) editSerialView.findViewById(R.id.add_serialSpinner);
		sp.setAdapter(AccountManager.getInstance());
		editSerialDialog = new AlertDialog.Builder(c).setView(editSerialView).setTitle("编辑").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface p11, int p2) {
					AccountInfo ai=(AccountInfo) sp.getSelectedItem();
					tv.setText(tv.getText() + "\n" + ai.name + ":" + et.getText().toString());
					add(ai.name, ai.cookie, et.getText().toString());
					et.setText("");
				}
			}).setNegativeButton("取消", null).create();
	}

	public void clear() {
		msgList.clear();
		tv.setText("");
	}

	public void add() {
		editSerialDialog.show();
	}

	private void add(String name, String cookie, String msg) {
		SendBean sb=new SendBean();
		sb.name = name;
		sb.cookie = cookie;
		sb.msg = msg;
		msgList.add(sb);
	}

	private class SendBean {
		private String name;
		private String cookie;
		private String msg;
	}

	public SerialDanmakuSend getSendTask() {
		SerialDanmakuSend sdc=new SerialDanmakuSend();
		for (SendBean sb:msgList) {
			sdc.add(sb.msg, sb.cookie, id);
		}
		return sdc;
	}
}
