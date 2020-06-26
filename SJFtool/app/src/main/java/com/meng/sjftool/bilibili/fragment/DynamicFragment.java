package com.meng.sjftool.bilibili.fragment;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.adapters.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.util.*;

public class DynamicFragment extends Fragment implements View.OnClickListener {

	private TabHost tab;
	private Spinner selectAccount;
	private ArrayAdapter<String> spinnerAccountAdapter=null;
	private ArrayList<String> spList=new ArrayList<>();
	private SelectedImageAdapter selectedImageAdapter=new SelectedImageAdapter();
	private Button btnSelect,btnSend;
	private EditText et;
	private ListView selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tab = (TabHost) view.findViewById(android.R.id.tabhost);
		tab.setup();
        LayoutInflater layoutInflater=LayoutInflater.from(getActivity()); 
		//layoutInflater.inflate(R.layout.av_fragment, tab.getTabContentView()); 
		layoutInflater.inflate(R.layout.send_dynamic, tab.getTabContentView());
		//tab.addTab(tab.newTabSpec("tab1").setIndicator("浏览" , null).setContent(R.id.av_fragmentLinearLayout));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("发送动态" , null).setContent(R.id.send_dynamicRelativeLayout));
		et = (EditText) view.findViewById(R.id.send_dynamicEditText);
		btnSelect = (Button) view.findViewById(R.id.send_dynamicButton_pic);
		btnSend = (Button) view.findViewById(R.id.send_dynamicButton_send);
		selected = (ListView) view.findViewById(R.id.send_dynamicListView);
		btnSelect.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		selectAccount = (Spinner) view.findViewById(R.id.send_dynamicSpinner_account);
		for (AccountInfo ai:AccountManager.iterate()) {
			spList.add(ai.name);
		}
		selectAccount.setAdapter(spinnerAccountAdapter = new ArrayAdapter<String>(MainActivity.instance, android.R.layout.simple_list_item_1, spList));
		selected.setAdapter(selectedImageAdapter);
		selected.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, final int p3, long p4) {
					new AlertDialog.Builder(getActivity()).setTitle("确定删除吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p1, int p2) {
								selectedImageAdapter.removeFile(p3);
							}
						}).setNegativeButton("取消", null).show();
					return true;
				}
			});
	}

	@Override
	public void onClick(View p1) {
		switch (p1.getId()) {
			case R.id.send_dynamicButton_pic:
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, 9961);
				break;
			case R.id.send_dynamicButton_send:
				MainActivity.instance.threadPool.execute(new Runnable(){

						@Override
						public void run() {
							String result;
							ArrayList<File> files=selectedImageAdapter.getFiles();
							if (files.size() == 0) {
								result = Tools.BilibiliTool.sendDynamic(et.getText().toString(), AccountManager.getAccount((String)selectAccount.getSelectedItem()).cookie);
								MainActivity.instance.showToast(result);
							} else {
								MainActivity.instance.showToast(Tools.BilibiliTool.sendDynamic(et.getText().toString(), AccountManager.getAccount((String)selectAccount.getSelectedItem()).cookie, files).toString());
							}
//							if (new JsonParser().parse(result).getAsJsonObject().get("code").getAsInt() == 0) {
//								MainActivity.instance.showToast("发送成功");
//							} else {
//								MainActivity.instance.showToast("发送失败");
//							}
							MainActivity.instance.runOnUiThread(new Runnable(){

									@Override
									public void run() {
										et.setText("");
									}
								});
						}
					});
		}
	}

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 9961 && data.getData() != null) {
				selectedImageAdapter.addFile(new File(Tools.AndroidContent.absolutePathFromUri(getActivity().getApplicationContext(), data.getData())));
			} 
		} else if (resultCode == Activity.RESULT_CANCELED) {
			MainActivity.instance.showToast("取消选择图片");
		}
        super.onActivityResult(requestCode, resultCode, data);
    }
}
