package com.meng.sjftool.bilibili.fragment;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.google.gson.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.activity.live.*;
import com.meng.sjftool.bilibili.adapters.*;
import com.meng.sjftool.bilibili.enums.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.bilibili.result.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.util.*;
import org.jsoup.*;

public class BaseIdFragment extends Fragment {

	protected static final int SendDanmaku=0;
	protected static final int Silver=1;
	protected static final int Pack=2;
	protected static final int Sign=3;
	protected static final int SendVideoJudge=4;
	protected static final int LikeVideo=5;
	protected static final int VideoCoin1=6;
	protected static final int VideoCoin2=7;
	protected static final int Favorite=8;
	protected static final int SendCvJudge=9;
	protected static final int CvCoin1=10;
	protected static final int CvCoin2=11;
	protected static final int LikeArtical=12;

	protected long id;
	protected IDType type;

	protected static ArrayAdapter<String> sencencesAdapter=null;
	protected static ArrayAdapter<String> spinnerAccountAdapter=null;
	private static ArrayList<String> spList=null;
	private static CustomSentence customSentence;
	private static File customSentenseFile;

	public BaseIdFragment(IDType type, long id) {
		this.type = type;
		this.id = id;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		customSentenseFile = new File(Environment.getExternalStorageDirectory() + "/sjf.json");
		if (customSentenseFile.exists() && customSentence != null) {
			customSentence = GSON.fromJson(Tools.FileTool.readString(customSentenseFile), CustomSentence.class);
		} else {
			customSentence = new CustomSentence();
			String[] strings = new String[]{ "此生无悔入东方,来世愿生幻想乡","红魔地灵夜神雪,永夜风神星莲船","非想天则文花贴,萃梦神灵绯想天","冥界地狱异变起,樱下华胥主谋现","净罪无改渡黄泉,华鸟风月是非辨","境界颠覆入迷途,幻想花开啸风弄","二色花蝶双生缘,前缘未尽今生还","星屑洒落雨霖铃,虹彩彗光银尘耀","无寿迷蝶彼岸归,幻真如画妖如月","永劫夜宵哀伤起,幼社灵中幻似梦","追忆往昔巫女缘,须弥之间冥梦现","仁榀华诞井中天,歌雅风颂心无念" };
			customSentence.sent.addAll(Arrays.asList(strings));
			saveConfig();
		}
		createSpinnerList();
		if (sencencesAdapter == null) {
			sencencesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, customSentence.sent);
		}
	}

	protected void saveBitmap(String bitName, Bitmap mBitmap) throws Exception {
		File f = new File(Environment.getExternalStorageDirectory() + "/pictures/" + bitName + ".png");
		f.createNewFile();
		FileOutputStream fOut = null;
		fOut = new FileOutputStream(f);
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		fOut.flush();
		fOut.close();
		getActivity().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
	}

	public static void createSpinnerList() {
		if (spList == null) {
			spList = new ArrayList<>();
		} else {
			spList.clear();
		}
		spList.add("每次选择");
		spList.add("主账号");
		spList.add("全部");
		for (AccountInfo ai:AccountManager.iterate()) {
			spList.add(ai.name);
		}
		if (spinnerAccountAdapter != null) {
			spinnerAccountAdapter.notifyDataSetChanged();
		} else {
			spinnerAccountAdapter = new ArrayAdapter<String>(MainActivity.instance, android.R.layout.simple_list_item_1, spList);
		}
	}

	protected void sendBili(final String sel, final int opValue, final String msg) {
		if (sel.equals("每次选择")) {
			String items[] = new String[AccountManager.size()];
			for (int i=0;i < items.length;++i) {
				items[i] = AccountManager.get(i).name;
			}
			final boolean checkedItems[] = new boolean[items.length];
			new AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_launcher).setTitle("选择账号").setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						checkedItems[which] = isChecked;
					}
				}).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						for (int i = 0; i < checkedItems.length; i++) {
							if (checkedItems[i]) {
								opSwitch(AccountManager.get(i), opValue, msg);
							}
						}
					}
				}).show();
		} else if (sel.equals("全部")) {
			MainActivity.instance.threadPool.execute(new Runnable(){

					@Override
					public void run() {
						for (AccountInfo ac:AccountManager.iterate()) {
							opSwitch(ac, opValue, msg);
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {}
						}
					}
				});
		} else {
			opSwitch(sel.equals("主账号") ?AccountManager.getAccount(MainActivity.instance.sjfSettings.getMainAccount()): AccountManager.getAccount(sel), opValue, msg);
		}
	}

	private void opSwitch(final AccountInfo ai, final int opValue, final String msg) {
		MainActivity.instance.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					switch (opValue) {
						case SendDanmaku:
							String response=Tools.BilibiliTool.sendLiveDanmaku(msg, ai.cookie, id);
							JsonParser parser = new JsonParser();
							JsonObject obj = parser.parse(response).getAsJsonObject();
							switch (obj.get("code").getAsInt()) {
								case 0:
									MainActivity.instance.showToast(obj.get("message").getAsString().equals("") ?id + "发送成功": obj.getAsJsonObject("message").getAsString());
									break;
								case 1990000:
									if (obj.get("message").getAsString().equals("risk")) {
										if (((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
											Intent intent = new Intent(MainActivity.instance, LiveWebActivity.class);
											intent.putExtra("cookie", ai.cookie);
											intent.putExtra("url", "https://live.bilibili.com/" + id);
											MainActivity.instance.startActivity(intent);
										} else {
											MainActivity.instance.showToast("需要在官方客户端进行账号风险验证");
										}
									}
									break;
								default:
									MainActivity.instance.showToast(response);
									break;
							}
							break;
						case Silver:
							MainActivity.instance.runOnUiThread(new Runnable(){

									@Override
									public void run() {
										final EditText editText = new EditText(getActivity());
										new AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_launcher).setTitle("输入辣条数(" + ai.name + ")").setView(editText).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													MainActivity.instance.threadPool.execute(new Runnable(){

															@Override
															public void run() {
																JsonObject liveToMainInfo=null;
																try {
																	liveToMainInfo = new JsonParser().parse(Tools.Network.httpGet("https://api.live.bilibili.com/live_user/v1/UserInfo/get_anchor_in_room?roomid=" + id)).getAsJsonObject().get("data").getAsJsonObject().get("info").getAsJsonObject();
																} catch (Exception e) {
																	return;
																}
																MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendHotStrip(ai.uid, liveToMainInfo.get("uid").getAsLong(), id, Integer.parseInt(editText.getText().toString()), ai.cookie)).getAsJsonObject().get("message").getAsString());
															}
														});
												}
											}).show();
									}
								});
							break;
						case Pack:
							sendPackDialog(ai, Tools.BilibiliTool.getRoomToUid(id).data.info.uid);
							break;
						case Sign:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendLiveSign(ai.cookie)).getAsJsonObject().get("message").getAsString());
							break;
						case SendVideoJudge:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendVideoJudge(msg, id, ai.cookie)).getAsJsonObject().get("message").getAsString());
							break;
						case LikeVideo:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendAvLike(id, ai.cookie)).getAsJsonObject().get("message").getAsString());
							break;
						case VideoCoin1:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendAvCoin(1, id, ai.cookie)).getAsJsonObject().get("message").getAsString());
							break;
						case VideoCoin2:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendAvCoin(2, id, ai.cookie)).getAsJsonObject().get("message").getAsString());
							break;
						case Favorite:
							MainActivity.instance.showToast("未填坑");
							break;
						case SendCvJudge:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendArticalJudge(id, msg, ai.cookie)).getAsJsonObject().get("code").getAsInt() == 0 ?"发送成功": "发送失败");
							break;
						case CvCoin1:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendCvCoin(1, id, ai.cookie)).getAsJsonObject().get("message").getAsString());
							break;
						case CvCoin2:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendCvCoin(2, id, ai.cookie)).getAsJsonObject().get("message").getAsString());
							break;
						case LikeArtical:
							MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendCvLike(id, ai.cookie)).getAsJsonObject().get("message").getAsString());
							break;
					}
				}
			});
	}

	protected void sendPackDialog(final AccountInfo ai, final long targetUid) {
		final GiftBag liveBag =Tools.BilibiliTool.getGiftBag(ai.cookie);
		getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ListView listView=new ListView(getActivity());
					new AlertDialog.Builder(getActivity()).setView(listView).setTitle("选择(" + ai.name + ")").show();
					final GiftAdapter giftAdapter = new GiftAdapter(getActivity(), liveBag.data.list);
					listView.setAdapter(giftAdapter);
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(final AdapterView<?> parent, View view, final int p, long itemid) {
								final EditText editText = new EditText(getActivity());
								editText.setHint("要赠送的数量");
								new AlertDialog.Builder(getActivity()).setView(editText).setTitle("编辑").setPositiveButton("确定", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface p11, int p2) {
											MainActivity.instance.threadPool.execute(new Runnable() {
													@Override
													public void run() {
														int num=Integer.parseInt(editText.getText().toString());
														if (num > liveBag.getStripCount()) {
															MainActivity.instance.showToast("辣条不足");	
															return;
														}
														for (GiftBag.ListItem i:liveBag.data.list) {
															if (i.gift_name.equals("辣条")) {
																if (num > i.gift_num) {
																	sendHotStrip(ai.uid, targetUid, id, i.gift_num, ai.cookie, i);
																	num -= i.gift_num;
																	i.gift_num = 0;
																} else {
																	sendHotStrip(ai.uid, targetUid, id, num, ai.cookie, i);											
																	i.gift_num -= num;
																	break;	
																}
															}
														}
														Iterator<GiftBag.ListItem> iterator = liveBag.data.list.iterator();
														while (iterator.hasNext()) {
															if (iterator.next().gift_num == 0) {
																iterator.remove();
															}
														}
														if (liveBag.getStripCount() == 0) {
															MainActivity.instance.showToast("已送出全部辣条🎁");
														}
														refreshAdapter(giftAdapter);
													}
												});
										}
									}).setNegativeButton("取消", null).show();
							}
						});
					listView.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(final AdapterView<?> p1, View p2, final int p3, long p4) {
								MainActivity.instance.threadPool.execute(new Runnable() {
										@Override
										public void run() {
											sendHotStrip(ai.uid, targetUid, id, liveBag.data.list.get(p3).gift_num, ai.cookie, liveBag.data.list.get(p3));
											liveBag.data.list.remove(p3);
											if (liveBag.getStripCount() == 0) {
												MainActivity.instance.showToast("已送出全部辣条🎁");
											}
											refreshAdapter(giftAdapter);
										}
									});
								return true;
							}
						});
				}
			});
	}

	protected void refreshAdapter(final BaseAdapter adapter) {
		MainActivity.instance.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			});
	}

	protected void sendHotStrip(long uid, long ruid, long roomID, int num, String cookie, GiftBag.ListItem liveBagDataList) {
		Connection connection = Jsoup.connect("https://api.live.bilibili.com/gift/v2/live/bag_send");
		String csrf = Tools.Network.cookieToMap(cookie).get("bili_jct");
		connection.userAgent(MainActivity.instance.userAgent)
			.headers(Tools.liveHead)
			.ignoreContentType(true)
			.referrer("https://live.bilibili.com/" + roomID)
			.cookies(Tools.Network.cookieToMap(cookie))
			.method(Connection.Method.POST)
			.data("uid", uid)
			.data("gift_id", liveBagDataList.gift_id)
			.data("ruid", ruid)
			.data("gift_num", num)
			.data("bag_id", liveBagDataList.bag_id)
			.data("platform", "pc")
			.data("biz_code", "live")
			.data("biz_id", roomID)
			.data("rnd", System.currentTimeMillis() / 1000)
			.data("storm_beat_id", 0)
			.data("metadata", "")
			.data("price", 0)
			.data("csrf_token", csrf)
			.data("csrf", csrf)
			.data("visit_id", "");	
		Connection.Response response=null;
		try {
			response = connection.execute();
		} catch (IOException e) {
			MainActivity.instance.showToast("连接出错");
			return;
		}
		if (response.statusCode() != 200) {
			MainActivity.instance.showToast(String.valueOf(response.statusCode()));
		}
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(response.body()).getAsJsonObject();
		MainActivity.instance.showToast(obj.get("message").getAsString());
	}

	private void saveConfig() {
        try {
			FileOutputStream fos = new FileOutputStream(customSentenseFile);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "utf-8");
            writer.write(GSON.toJson(customSentence));
            writer.flush();
            fos.close();
		} catch (IOException e) {
            throw new RuntimeException(customSentenseFile.getAbsolutePath() + " not found");
		}
	}
}
