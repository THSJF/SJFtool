package com.meng.sjftool.update;

public interface WebSocketMessageAction {
	public int useTimes();
	public int forOpCode();
	public BotDataPack onMessage(BotDataPack rec);
}
