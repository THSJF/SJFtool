package com.meng.sjftool.update;

import com.meng.sjftool.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

public class RanConnect extends WebSocketClient {

	private HashSet<WebSocketOnOpenAction> onOpenAction = new HashSet<>();

	private static RanConnect crn;

	public static RanConnect getRanconnect() {
		if (crn == null) {
			try {
				crn = new RanConnect();
			} catch (Exception e) {
				MainActivity.instance.showToast(e.toString());
			}
		}
		return crn;
	}
	private RanConnect() throws Exception {
		super(new URI("ws://123.207.65.93:8888"));
	}

	@Override
	public void onOpen(ServerHandshake p1) {
		Iterator<WebSocketOnOpenAction> iterator=onOpenAction.iterator();
		while (iterator.hasNext()) {
			WebSocketOnOpenAction wma=iterator.next();
			wma.action(this);
			if (wma.useTimes() == 1) {
				iterator.remove();
			}
		}
	}

	public void addOnOpenAction(WebSocketOnOpenAction wma) {
		onOpenAction.add(wma);
	}

	@Override
	public void onMessage(String p1) {

	}

	@Override
	public void onMessage(ByteBuffer bytes) {
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
