package com.meng.sjftool.bilibili.fragment;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.github.clans.fab.*;
import com.google.gson.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.customView.*;
import com.meng.sjftool.bilibili.enums.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.bilibili.result.*;
import com.meng.sjftool.libs.*;
import com.universalvideoview.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import org.java_websocket.client.*;
import org.java_websocket.exceptions.*;
import org.java_websocket.handshake.*;

import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class LiveFragment extends BaseIdFragment implements View.OnClickListener, UniversalVideoView.VideoViewCallback {

	private Button editPre,preset;

	private ImageButton send;
	private FloatingActionMenu menuGroup;
	private FloatingActionButton fabSilver;
    private FloatingActionButton fabPack;
	private FloatingActionButton fabDownload;
    private FloatingActionButton fabMilk;
	private LinearLayout llInput;



	private EditText et;
	private TextView info;
	private Spinner selectAccount;

	private Bitmap preview;
	private ImageView img;
	private Uri uri;

    View mVideoLayout;
	View mBottomLayout;
	private int cachedHeight;

	//private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
	private int mSeekPosition;
	private UniversalVideoView mVideoView;
    private UniversalMediaController mMediaController;

	private boolean isFullscreen;

	private JsonObject liveInfo;
	private UidToRoom utr;
	private long uid;


	public ArrayList<String> recieved = new ArrayList<>();
	public ArrayAdapter<String> adapter;
	private ListView danmakuList;

	private DanmakuListener danmakuListener;

	public LiveFragment(IDType type, long id) {
		super(type, id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.live_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		llInput = (LinearLayout) view.findViewById(R.id.live_fragmentLinearLayout_input);
		llInput.setBackgroundColor(0xffffffff);
		send = (ImageButton) view.findViewById(R.id.live_fragment2Button_send);
		fabSilver = (FloatingActionButton) view.findViewById(R.id.live_fragment2Button_silver);
		fabPack = (FloatingActionButton) view.findViewById(R.id.live_fragment2Button_pack);
		menuGroup = (FloatingActionMenu) view.findViewById(R.id.lv_float_menu);
		//editPre = (Button) view.findViewById(R.id.live_fragmentButton_edit_pre);
		preset = (Button) view.findViewById(R.id.live_fragment2Button_preset);
		img = (ImageView) view.findViewById(R.id.live_fragmentImageView);  
		et = (EditText) view.findViewById(R.id.live_fragment2EditText_danmaku);
		info = (TextView) view.findViewById(R.id.live_fragmentTextView_info);
		selectAccount = (Spinner) view.findViewById(R.id.live_fragment2Spinner);
		fabMilk = (FloatingActionButton) view.findViewById(R.id.livefragmentButtonSerialMilk);
		fabDownload = (FloatingActionButton) view.findViewById(R.id.livefragmentButtonDownload);
		//fabDownload.setEnabled(false);
		mVideoLayout = view.findViewById(R.id.videoLayout);
        mBottomLayout = view.findViewById(R.id.live_fragmentLinearLayout_b);
		mVideoView = (UniversalVideoView) view.findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) view.findViewById(R.id.media_controller);
		danmakuList = (ListView) view.findViewById(R.id.livefragmentListView_danmaku);
		final Animation animShow = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_slide_in_from_right);
		final Animation animHide = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_slide_out_to_right);
		menuGroup.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
				@Override
				public void onMenuToggle(boolean opened) {
					if (opened) {
						llInput.startAnimation(animShow);
						llInput.setVisibility(View.VISIBLE);
					} else {
						llInput.startAnimation(animHide);
						llInput.setVisibility(View.GONE);
					}
				}
			});
		adapter = new ArrayAdapter<String>(MainActivity.instance, android.R.layout.simple_list_item_1, recieved);
		danmakuList.setAdapter(adapter);
		danmakuList.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
					AndroidContent.copyToClipboard(recieved.get(p3));
				}
			});
		try {
			danmakuListener = new DanmakuListener(this, id);
			danmakuListener.connect();
		} catch (URISyntaxException e) {
			MainActivity.instance.showToast("弹幕服务器连接失败:" + e.toString());
		}
		mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);
		fabDownload.setOnClickListener(this);
		preset.setOnClickListener(this);
		send.setOnClickListener(this);
		fabSilver.setOnClickListener(this);
		fabPack.setOnClickListener(this);
		//editPre.setOnClickListener(onclick);
		fabMilk.setOnClickListener(this);
		selectAccount.setAdapter(spinnerAccountAdapter);
		img.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					if (mSeekPosition > 0) {
						mVideoView.seekTo(mSeekPosition);
					}
					mVideoLayout.setVisibility(View.VISIBLE);
					img.setVisibility(View.GONE);
					mVideoView.start();
					mMediaController.setTitle("发发发");
				}
			});
		mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					MainActivity.instance.showToast("播放完成");
					mVideoLayout.setVisibility(View.GONE);
					img.setVisibility(View.VISIBLE);
				}
			});
		img.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1) {
					try {
						saveBitmap(type.toString() + id, preview);
						MainActivity.instance.showToast("图片已保存至" + MainActivity.instance.mainDic + type + id + ".png");
					} catch (Exception e) {}
					return true;
				}
			});
		if (!MainActivity.instance.sjfSettings.isSaveDebugLog()) {
			FileTool.deleteFiles(new File(Environment.getExternalStorageDirectory() + "/d/"));
		}
		MainActivity.instance.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					JsonParser parser = new JsonParser();
					liveInfo = parser.parse(com.meng.sjftool.libs.Network.httpGet("https://api.live.bilibili.com/room/v1/Room/playUrl?cid=" + id + "&quality=4&platform=web")).getAsJsonObject();
					if (liveInfo.get("code").getAsInt() == 19002003) {
						MainActivity.instance.showToast("不存在的房间");
						return;
					}
					final JsonArray ja = liveInfo.get("data").getAsJsonObject().get("durl").getAsJsonArray();
					JsonObject liveToMainInfo=null;
					try {
						liveToMainInfo = new JsonParser().parse(com.meng.sjftool.libs.Network.httpGet("https://api.live.bilibili.com/live_user/v1/UserInfo/get_anchor_in_room?roomid=" + id)).getAsJsonObject().get("data").getAsJsonObject().get("info").getAsJsonObject();
					} catch (Exception e) {
						return;
					}
					uid = liveToMainInfo.get("uid").getAsLong();
					final String uname=liveToMainInfo.get("uname").getAsString();
					utr = Bilibili.getUidToRoom(uid);
					final byte[] imgbs = NetworkCacher.getNetPicture(Bilibili.getUidToRoom(uid).data.cover);
					preview = BitmapFactory.decodeByteArray(imgbs, 0, imgbs.length);
					getActivity().runOnUiThread(new Runnable(){

							@Override
							public void run() {
								img.setImageBitmap(preview);
								if (utr.data.liveStatus != 1) {
									info.setText("房间号:" + id + "\n主播:" + uname + "\n未直播");
									mMediaController.setTitle("未直播");
									MainActivity.instance.renameFragment(type.toString() + id, uname + "的直播间");
								} else {
									uri = Uri.parse(ja.get(0).getAsJsonObject().get("url").getAsString());
									mVideoView.setVideoURI(uri);
									mVideoView.requestFocus();
									mMediaController.setTitle(utr.data.title);
									info.setText("房间号:" + id + "\n主播:" + uname + "\n标题:" + utr.data.title);
									MainActivity.instance.renameFragment(type.toString() + id, uname + "的直播间");
								}
							}
						});

					/*	String html = Tools.Network.getSourceCode("https://live.bilibili.com/" + id);
					 String jsonInHtml = html.substring(html.indexOf("{\"roomInitRes\":"), html.lastIndexOf("}") + 1);
					 final JsonObject data = new JsonParser().parse(jsonInHtml).getAsJsonObject().get("baseInfoRes").getAsJsonObject().get("data").getAsJsonObject();
					 getActivity().runOnUiThread(new Runnable(){

					 @Override
					 public void run() {
					 info.setText("房间号:" + id + "\n主播:" + uname + "\n房间标题:" + data.get("title").getAsString() +
					 "\n分区:" + data.get("parent_area_name").getAsString() + "-" + data.get("area_name").getAsString() +
					 "\n标签:" + data.get("tags").getAsString());
					 }
					 });	*/
				}
			});
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (!isVisibleToUser && mVideoView != null && mVideoView.isPlaying()) {
			mSeekPosition = mVideoView.getCurrentPosition();
			mVideoView.pause();
		}
		if (isVisibleToUser) {
			try {
				danmakuListener.send(danmakuListener.encode(2, "").data);
			} catch (WebsocketNotConnectedException e) {
				MainActivity.instance.showToast("连接中断,重新连接....");
				danmakuListener.reconnect();
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	/**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
				@Override
				public void run() {
					int width = mVideoLayout.getWidth();
					cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
					ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
					videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
					videoLayoutParams.height = cachedHeight;
					mVideoLayout.setLayoutParams(videoLayoutParams);
					mVideoView.requestFocus();
				}
			});
    }

	@Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);
			menuGroup.hideMenuButton(true);
        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);
			menuGroup.showMenuButton(true);
        }
		switchTitleBar(!isFullscreen);
    }

	private void switchTitleBar(boolean show) {
        ActionBar supportActionBar = getActivity().getActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }


	@Override
	public void onClick(final View p1) {
		switch (p1.getId()) {
			case R.id.live_fragment2Button_preset:
				ListView naiSentenseListview = new ListView(getActivity());
				naiSentenseListview.setAdapter(sencencesAdapter);
				naiSentenseListview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
							sendBili((String) selectAccount.getSelectedItem(), SendDanmaku, (String)p1.getAdapter().getItem(p3));
						}
					});
				new AlertDialog.Builder(getActivity()).setView(naiSentenseListview).setTitle("选择预设语句").setNegativeButton("返回", null).show();
				break;
			case R.id.live_fragment2Button_send:
				sendBili((String) selectAccount.getSelectedItem(), SendDanmaku, et.getText().toString());
				break;
			case R.id.live_fragment2Button_pack:
				sendBili((String) selectAccount.getSelectedItem(), Pack, "");
				break;
			case R.id.live_fragment2Button_silver:
				sendBili((String) selectAccount.getSelectedItem(), Silver, "");
				break;
			case R.id.livefragmentButtonSerialMilk:
				final SelectMilk sm=new SelectMilk(getActivity(), id);
				new AlertDialog.Builder(getActivity()).setView(sm).setTitle("选择").setPositiveButton("发送",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//MainActivity.instance.showToast(sm.toString());
							MainActivity.instance.threadPool.execute(sm.getSendTask());
							try {  
								Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");  
								field.setAccessible(true);
								field.set(dialog, true);  
							} catch (Exception e) {  
							}
						}
					}).setNeutralButton("添加",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							sm.add();
							try {  
								Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");  
								field.setAccessible(true);
								field.set(dialog, false);  
							} catch (Exception e) {  
							}  
						}
					}).setNegativeButton("清空", 
					new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int witch) {
							sm.clear();
							try {  
								Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");  
								field.setAccessible(true);
								field.set(dialog, false);  
							} catch (Exception e) {  
							}
						}
					}).show();
				break;
			case R.id.livefragmentButtonDownload:
				JsonArray ja = liveInfo.get("data").getAsJsonObject().get("durl").getAsJsonArray();
				AndroidContent.copyToClipboard(ja.get(0).getAsJsonObject().get("url").getAsString());
				MainActivity.instance.showToast("已复制到剪贴板,可使用ADM下载直播视频");
//				MainActivity.instance.threadPool.execute(new Runnable(){
//
//						@Override
//						public void run() {
//							String fileName = new File(String.format("Live%d %s %s.flv", id, utr.data.title, TimeFormater.getTime())).getName();
//							JsonArray ja = liveInfo.get("data").getAsJsonObject().get("durl").getAsJsonArray();
//							String url = ja.get(0).getAsJsonObject().get("url").getAsString();
//							DownloadManager downloadManager = (DownloadManager)getActivity(). getSystemService(Context.DOWNLOAD_SERVICE);
//							DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//							// 通知栏的下载通知
//							request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//							request.setTitle(fileName);
//							request.addRequestHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//							//request.addRequestHeader("Referer", "https://live.bilibili.com/" + id);
//							request.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
//							//  request.setMimeType("application/vnd.android.package-archive");
//							request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, fileName);
//							long downloadId = downloadManager.enqueue(request);
//							// MainActivity.instance.showToast("task id:"+downloadId,"id:"+id+"  cid:"+cid);
//							MainActivity.instance.showToast(url);
//
//						}
//					});
				break;
		}
	}

	@Override
	public void onPause(MediaPlayer mediaPlayer) {
		// TODO: Implement this method
	}

	@Override
	public void onStart(MediaPlayer mediaPlayer) {
		// TODO: Implement this method
	}

	@Override
	public void onBufferingStart(MediaPlayer mediaPlayer) {
		// TODO: Implement this method
	}

	@Override
	public void onBufferingEnd(MediaPlayer mediaPlayer) {
		// TODO: Implement this method
	}

	private class DanmakuListener extends WebSocketClient {

		private long roomId;

		private static final int customHeartBeat=2;
		private static final int hot=3;
		private static final int command=5;
		private static final int initJoin=7;
		private static final int serverHeartBeat=8;

		private static final String DANMU_MSG="DANMU_MSG";//弹幕
		private static final String SEND_GIFT="SEND_GIFT";//有人送礼
		private static final String WELCOME="WELCOME";//欢迎加入房间
		private static final String WELCOME_GUARD="WELCOME_GUARD";//欢迎房管加入房间
		private static final String SYS_MSG="SYS_MSG";//系统消息
		private static final String PREPARING="PREPARING";//主播准备中
		private static final String LIVE="LIVE";//直播开始
		private static final String ROOM_BLOCK_MSG="ROOM_BLOCK_MSG";//禁言通知

		private LiveFragment liveFragment;

		public DanmakuListener(LiveFragment lvf, long roomId) throws URISyntaxException {
			super(new URI("wss://broadcastlv.chat.bilibili.com:2245/sub"));
			this.roomId = roomId;
			liveFragment = lvf;
		}

		@Override
		public void onMessage(String p1) {
			// TODO: Implement this method
		}

		@Override
		public void onOpen(ServerHandshake serverHandshake) {
			send(encode(initJoin, String.format(AndroidContent.readAssetsString("bliveInit.json"), roomId)).data);
			MainActivity.instance.threadPool.execute(new Runnable(){

					@Override
					public void run() {
						while (true) {
							try {
								send(encode(customHeartBeat, "").data);
								Thread.sleep(30000);
							} catch (InterruptedException e) {

							} catch (Exception e) {

							}
						}
					}
				});
		}

		@Override
		public void onMessage(ByteBuffer bs) {
			if (MainActivity.instance.sjfSettings.isSaveDebugLog()) {
				try {
					File binFile=new File(Environment.getExternalStorageDirectory() + "/d/" + System.currentTimeMillis() + ".dat");
					FileOutputStream fos=new FileOutputStream(binFile);
					fos.write(bs.array());
					fos.flush();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.toString());
					throw new SJFException(e.toString());
				}
			}
			byte[] bytes=bs.array();
			int offset=0;
			do{
				DataPackage dp=decode(bytes, offset);
				offset += dp.length;
				switch (dp.op) {
					case hot:
						break;
					case command:
						onCommand(dp);
						break;
					case serverHeartBeat:
						break;
				}
			}while(offset < bytes.length - 1);
		}

		private void onCommand(DataPackage dp) {
			try {
				JsonObject jobj=new JsonParser().parse(dp.body).getAsJsonObject();
				switch (jobj.get("cmd").getAsString()) {
					case DANMU_MSG:
//					{
//						"cmd": "DANMU_MSG",
//						"info": [
//						[ 0, 1, 25, 16777215, 1587168682304, 61431705, 0, "0c0beae3", 0, 0, 0 ],
//						"a",
//						[ 64483321, "妖怪之山的厄神", 0, 0, 0, 10000, 1, "" ],
//						[ 17, "台混", "散落的烛光", 2409909, 16752445, "", 0 ],
//						[ 23, 0, 5805790, ">50000" ],
//						[ "title-220-1", "title-220-1" ],
//						0,
//						0,
//						null,
//						{ "ts": 1587168682, "ct": "28CDA054" },
//						0,
//						0,
//						null,
//						null,
//						0
//						]
//					}
						JsonArray jaar=jobj.get("info").getAsJsonArray();
						String danmakuText=jaar.get(1).getAsString();
						JsonArray jaar2=jaar.get(2).getAsJsonArray();
						String speakerName=jaar2.get(1).getAsString();
						long speakerUid=jaar2.get(0).getAsLong();
						liveFragment.recieved.add(speakerName + ":" + danmakuText);
						break;
					case SEND_GIFT:
						JsonObject giftData=jobj.get("data").getAsJsonObject();
						liveFragment.recieved.add(String.format("%s(%d)赠送了%d个%s", giftData.get("uname").getAsString(), giftData.get("uid").getAsLong(), giftData.get("num").getAsInt(), giftData.get("giftName").getAsString()));
						break;
					case ROOM_BLOCK_MSG:
//					{
//						"cmd": "ROOM_BLOCK_MSG",
//						"uid": "424494698",
//						"uname": "八云橙的幻想",
//						"data": {
//							"uid": 424494698,
//							"uname": "八云橙的幻想",
//							"operator": 2
//						}
//					}
						liveFragment.recieved.add("用户 " + jobj.get("uname").getAsString() + " 已被禁言");
						break;
					case WELCOME:

						break;
					case WELCOME_GUARD:

						break;
					case LIVE:
//					LiveStatues lsl=MainActivity.instance.gson.fromJson(dp.body,LiveStatues.class);
						liveFragment.recieved.add("开始直播");
						break;
					case PREPARING:
//					LiveStatues lsp=MainActivity.instance.gson.fromJson(dp.body,LiveStatues.class);
						liveFragment.recieved.add("直播结束");
						break;
					case SYS_MSG:
						liveFragment.recieved.add(dp.body);
						break;
				}
				MainActivity.instance.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							liveFragment.adapter.notifyDataSetChanged();
						}
					});
			} catch (JsonSyntaxException je) {
				MainActivity.instance.showToast("json解析失败:" + dp.body);
			}
		}

		@Override
		public void onClose(int i, String s, boolean b) {

		}

		@Override
		public void onError(Exception e) {
			MainActivity.instance.showToast(e.toString());
		}

		private class LiveStatues {

			public String cmd;
			public String roomid;

			public boolean isStartLive() {
				if (!cmd.equals(LIVE) && !cmd.equals(PREPARING)) {
					throw new RuntimeException("unkown cmd");
				}
				return cmd.equals(LIVE);
			}
		}




		public DataPackage encode(int op, String body) {
			return new DataPackage(op, body);
		}

		public DataPackage decode(byte[] pack, int pos) {
			return new DataPackage(pack, pos);
		}

		private class DataPackage {
			public byte[] data;
			private int pos=0;

			public int length;
			public short headLen;
			public short version;
			public int op;
			public int seq;
			public String body="";
			public byte[] bodyArray;

			public DataPackage(int opCode, String jsonStr) {
				byte[] jsonByte=jsonStr.getBytes();
				data = new byte[16 + jsonByte.length];
				write(getBytes(length = data.length));
				write(getBytes(headLen = (short)16));
				write(getBytes(version = (short)1));
				write(getBytes(op = opCode));
				write(getBytes(seq = 1));
				write(jsonByte);
			}   

			public DataPackage(byte[] pack, int offset) {
				data = pack;
				pos = offset;
				length = readInt();
				headLen = readShort();
				version = readShort();
				op = readInt();
				seq = readInt();
				bodyArray = new byte[length - 16];
				for (int i=0;i < bodyArray.length;++i) {
					bodyArray[i] = data[i + 16];
				}

				if (version == 1) {
					body = new String(data, offset + 16, length - 16, StandardCharsets.UTF_8);
				} else if (version == 2) {
					//	try {
					//		DataPackage ndp=new DataPackage(NetWorkUtil.uncompress(bodyArray), 0);
					//	body=ndp.body;
					//		} catch (IOException e) {}
				}

				data = null;
			}

			private void write(byte[] bs) {
				for (int i=0;i < bs.length;++i) {
					data[pos++] = bs[i];
				}
			}

			private byte[] getBytes(int i) {
				return BitConverter.getInstanceBigEndian().getBytes(i);
				/*	byte[] bs=new byte[4];
				 bs[0] = (byte) ((i >> 24) & 0xff);
				 bs[1] = (byte) ((i >> 16) & 0xff);
				 bs[2] = (byte) ((i >> 8) & 0xff);
				 bs[3] = (byte) (i & 0xff);
				 return bs;	*/
			}

			private byte[] getBytes(short s) {
				return BitConverter.getInstanceBigEndian().getBytes(s);
				/*	byte[] bs=new byte[2];
				 bs[0] = (byte) ((s >> 8) & 0xff);
				 bs[1] = (byte) (s & 0xff) ;
				 return bs;	*/
			}
			/*大端模式*/
			public short readShort() {
				short s= BitConverter.getInstanceBigEndian().toShort(data, pos);
				pos += 2;
				return s;
				//	return (short) ((data[pos++] & 0xff) << 8 | (data[pos++] & 0xff) << 0);
			}

			public int readInt() {
				int i=BitConverter.getInstanceBigEndian().toInt(data, pos);
				pos += 4;
				return i;
				//return (data[pos++] & 0xff) << 24 | (data[pos++] & 0xff) << 16 | (data[pos++] & 0xff) << 8 | (data[pos++] & 0xff) << 0;
			}
		}
	}
}
