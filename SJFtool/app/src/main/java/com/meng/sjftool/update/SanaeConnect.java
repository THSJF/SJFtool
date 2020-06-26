package com.meng.sjftool.update;

import android.app.*;
import android.content.*;
import android.os.*;
import com.meng.sjftool.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

public class SanaeConnect extends WebSocketClient {

	private HashSet<WebSocketMessageAction> messageAction = new HashSet<>();
	private HashSet<WebSocketOnOpenAction> onOpenAction = new HashSet<>();
	public SanaeConnect() throws Exception {
		super(new URI("ws://123.207.65.93:9234"));
	}

	@Override
	public void onOpen(ServerHandshake p1) {
		MainActivity.instance.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(30000);
						} catch (InterruptedException e) {}
						try {
							send("");
						} catch (Exception e) {
							if (isClosed()) {
								reconnect();
							}
						}
					}
				}
			});
		Iterator<WebSocketOnOpenAction> iterator=onOpenAction.iterator();
		while (iterator.hasNext()) {
			WebSocketOnOpenAction wma=iterator.next();
			wma.action(this);
			if (wma.useTimes() == 1) {
				iterator.remove();
			}
		}
	}

	public void addMessageAction(WebSocketMessageAction wma) {
		messageAction.add(wma);
	}

	public void addOnOpenAction(WebSocketOnOpenAction wma) {
		onOpenAction.add(wma);
	}

	@Override
	public void onMessage(String p1) {
		if (!p1.equals("")) {
			final SoftwareInfo esi=GSON.fromJson(p1, SoftwareInfo.class);
			if (!MainActivity.instance.sjfSettings.getVersion().equals(esi.infoList.get(esi.infoList.size() - 1).versionName)) {	
				MainActivity.instance.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							new AlertDialog.Builder(MainActivity.instance)
								.setTitle("发现新版本")
								.setMessage(esi.toString())
								.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface p1, int p2) {
										send(BotDataPack.encode(BotDataPack.opGetApp).write(MainActivity.instance.getPackageName()).getData());
										MainActivity.instance.showToast("开始下载");
									}
								}).setNeutralButton("下次提醒我", null)
								.setNegativeButton("忽略本次更新", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										MainActivity.instance.sjfSettings.setVersion(esi.infoList.get(esi.infoList.size() - 1).versionName);
									}
								}).show();
						}
					});
			}
		}
	}

	@Override
	public void onMessage(ByteBuffer bytes) {
		BotDataPack bdp=BotDataPack.decode(bytes.array());

		//MainActivity.instance.showToast("收到数据" + bdp.getOpCode() + " 长度" + bytes.array().length);
		Iterator<WebSocketMessageAction> iterator=messageAction.iterator();
		while (iterator.hasNext()) {
			WebSocketMessageAction wma=iterator.next();
			if (wma.forOpCode() == bdp.getOpCode()) {
				BotDataPack sendBdp=wma.onMessage(bdp);
				if (sendBdp != null) {
					send(sendBdp.getData());
				}
				if (wma.useTimes() == 1) {
					iterator.remove();
				}
			}
		}
		if (bdp.getOpCode() == BotDataPack.opGetApp) {
			File f=new File(Environment.getExternalStorageDirectory() + "/download/" + MainActivity.instance.getPackageName() + ".apk");
			bdp.readFile(f);
			MainActivity.instance.showToast("文件已保存至" + f.getAbsolutePath());
		} else if (bdp.getOpCode() == BotDataPack.opTextNotify) {
			MainActivity.instance.showToast(bdp.readString());
		} else if (bdp.getOpCode() == BotDataPack.opGetGzApp) {
			File f=new File(Environment.getExternalStorageDirectory() + "/download/" + MainActivity.instance.getPackageName() + ".gz");
			bdp.readFile(f);
			byte[] uncompressed=Gzip.unCompress(FileTool.readBytes(f));
			File fa=new File(Environment.getExternalStorageDirectory() + "/download/" + MainActivity.instance.getPackageName() + ".apk");
			try {
				FileOutputStream fos = new FileOutputStream(fa);
				fos.write(uncompressed);
				fos.flush();
				fos.close();
			} catch (Exception e) {
				throw new RuntimeException(e.toString());
			}
			MainActivity.instance.showToast("文件已保存至" + f.getAbsolutePath());
		}
		super.onMessage(bytes);
	}

	@Override
	public void onClose(int p1, String p2, boolean p3) {

	}

	@Override
	public void onError(Exception p1) {
		// TODO: Implement this method
	}
}
