package com.meng.sjftool.bilibili.fragment;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.github.clans.fab.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.activity.main.*;
import com.meng.sjftool.bilibili.enums.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.bilibili.result.*;
import com.meng.sjftool.update.*;
import org.java_websocket.client.*;

import android.view.View.OnClickListener;

public class ManagerFragment extends Fragment {

	private AlertDialog selectOpDialog;

	private FloatingActionMenu menuGroup;
	private FloatingActionButton fabCookie;
    private FloatingActionButton fabLogin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		menuGroup = (FloatingActionMenu) view.findViewById(R.id.account_managerButton);
		menuGroup.setClosedOnTouchOutside(true);

        ListView list = (ListView) view.findViewById(R.id.account_managerListView);
        list.setAdapter(MainActivity.instance.mainAccountAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
					MainActivity.instance.showFragment(UidFragment.class, IDType.UID, AccountManager.get(position).uid);
				}
			});

		fabCookie = (FloatingActionButton) view.findViewById(R.id.account_manager_fab_cookie);
		fabLogin = (FloatingActionButton) view.findViewById(R.id.account_manager_fab_login);
		fabCookie.setOnClickListener(onClick);
		fabLogin.setOnClickListener(onClick);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(final AdapterView<?> p11, View p2, final int position, long p4) {

					ListView lvSelectOp = new ListView(getActivity());
					lvSelectOp.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, new String[]{"设置账号", "上移", "下移", "更新登录状态","上传cookie", "删除"}));
					lvSelectOp.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> p1, View p2, int selectOp, long p4) {
								switch (((String) p1.getAdapter().getItem(selectOp))) {
									case "设置账号":
										final AccountInfo aci=AccountManager.get(position);
										final EditText et1=new EditText(getActivity());
										new AlertDialog.Builder(getActivity()).setTitle("账号").setView(et1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface p1, int p2) {
													aci.phone = Long.parseLong(et1.getText().toString());
													final EditText et2=new EditText(getActivity());
													new AlertDialog.Builder(getActivity())
														.setTitle("密码").setView(et2).setPositiveButton("确定", new DialogInterface.OnClickListener() {
															@Override
															public void onClick(DialogInterface p1, int p2) {
																aci.password = et2.getText().toString();
																AccountManager.saveConfig();
															}
														}).show();
												}
											}).show();
										break;
									case "上移":
										if (!AccountManager.moveUp(position)) {
											MainActivity.instance.showToast("已经是最上面了");
										}
										break;
									case "下移":
										if (!AccountManager.moveDown(position)) {
											MainActivity.instance.showToast("已经是最下面了");
										}
										break;
									case "更新登录状态":
										Intent inte=new Intent(getActivity(), Login.class);
										inte.putExtra("pos", position);
										startActivity(inte);
										break;
									case "上传cookie":
										final AccountInfo aif = AccountManager.get(position);
										MainActivity.instance.threadPool.execute(new Runnable(){

												@Override
												public void run() {
													try {
														RanConnect rc=RanConnect.getRanconnect();
														if (!rc.isOpen()) {
															rc.addOnOpenAction(new WebSocketOnOpenAction(){

																	@Override
																	public void action(WebSocketClient wsc) {
																		wsc.send(BotDataPack.encode(BotDataPack.cookie).write((int)aif.uid).write(aif.cookie).getData());
																	}
																});
															rc.connect();
														} else {
															rc.send(BotDataPack.encode(BotDataPack.cookie).write((int)aif.uid).write(aif.cookie).getData());
														}
														MainActivity.instance.showToast(aif.name + "的cookie已上传");
													} catch (Exception e) {
														MainActivity.instance.showToast(e.toString());
													}
												}
											});
										break;
									case "删除":
										new AlertDialog.Builder(getActivity()).setTitle("确定删除" + AccountManager.get(position).name + "吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface p1, int p2) {
													AccountManager.remove(position);
													MainActivity.instance.mainAccountAdapter.notifyDataSetChanged();
													BaseIdFragment.createSpinnerList();
												}
											}).setNegativeButton("取消", null).show();
										break;
								}
								MainActivity.instance.mainAccountAdapter.notifyDataSetChanged();
								BaseIdFragment.createSpinnerList();
								selectOpDialog.cancel();
							}
						});
					selectOpDialog = new AlertDialog.Builder(getActivity()).setView(lvSelectOp).setTitle("选择操作").setNegativeButton("返回", null).show();
					return true;
				}
			});
    }

	OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
			menuGroup.close(true);
            switch (v.getId()) {
                case R.id.account_manager_fab_cookie:
					final EditText et = new EditText(getActivity());
					new AlertDialog.Builder(getActivity()).setTitle("输入cookie").setView(et)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface p1, int p2) {
								addByCookie(et.getText().toString());						
							}
						}).show();
                    break;
                case R.id.account_manager_fab_login:
					startActivity(new Intent(getActivity(), Login.class));
                    break;
            }
        }
    };

	private void addByCookie(final String cookie) {
		MainActivity.instance.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					MyInfo myInfo = Bilibili.getMyInfo(cookie);
					if (AccountManager.contains(myInfo.data.mid)) {
						MainActivity.instance.showToast("已添加过此帐号");
						return;
					}
					AccountInfo accountInfo = new AccountInfo();
					accountInfo.cookie = cookie;
					accountInfo.name = myInfo.data.name;
					accountInfo.uid = myInfo.data.mid;
					AccountManager.add(accountInfo);
					MainActivity.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity.instance.mainAccountAdapter.notifyDataSetChanged();
								BaseIdFragment.createSpinnerList();
							}
						});
				}
			});
	}
}
