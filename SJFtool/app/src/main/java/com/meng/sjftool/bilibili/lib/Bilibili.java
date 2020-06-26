package com.meng.sjftool.bilibili.lib;

import com.google.gson.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.bilibili.result.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.util.*;
import org.json.*;
import org.jsoup.*;

public class Bilibili {

	public static String REFERER = "Referer";

	public static Map<String, String> liveHead = new HashMap<String,String>(){
		{
			put("Host", "api.live.bilibili.com");
			put("Accept", "application/json, text/javascript, */*; q=0.01");
			put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			put("Connection", "keep-alive");
			put("Origin", "https://live.bilibili.com");
		}
	};
    public static Map<String, String> mainHead = new HashMap<String,String>(){
		{
			put("Host", "api.bilibili.com");
			put("Accept", "application/json, text/javascript, */*; q=0.01");
			put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			put("Connection", "keep-alive");
			put("Origin", "https://www.bilibili.com");
		}
	};

	public static CvInfo getCvInfo(long cvId) {
		return GSON.fromJson(Network.httpGet("http://api.bilibili.com/x/article/viewinfo?id=" + cvId + "&mobi_app=pc&jsonp=jsonp"), CvInfo.class);
	}

	public static VideoInfo getVideoInfo(long aid) {
		return GSON.fromJson(Network.httpGet("http://api.bilibili.com/x/web-interface/view?aid=" + aid) , VideoInfo.class);
	}

	public static VideoUrl getVideoUrl(long aid, long cid, int quality) {
		return GSON.fromJson(Network.httpGet("https://api.bilibili.com/x/player/playurl?avid=" + aid + "&cid=" + cid + "&qn=" + quality + "&type=flv", AccountManager.get(0).cookie, "https://www.bilibili.com/video/av" + aid), VideoUrl.class);
	}

	public static VideoUrl getVideoUrl(long aid, long cid) {
		return getVideoUrl(aid, cid, 64);
	}

	public static VideoReply getVideoJudge(long aid) {
		return GSON.fromJson(Network.httpGet("https://api.bilibili.com/x/v2/reply?jsonp=jsonp&pn=1&type=1&sort=1&oid=" + aid), VideoReply.class);
	}

	public static VideoReply getVideoJudge(long aid, long root) {
		return GSON.fromJson(Network.httpGet("https://api.bilibili.com/x/v2/reply/reply?jsonp=jsonp&pn=1&type=1&sort=1&oid=" + aid + "&ps=10&root=" + root + "&_=" + System.currentTimeMillis()), VideoReply.class);
	}

	public static MyInfo getMyInfo(String cookie) {
		return GSON.fromJson(Network.httpGet("http://api.bilibili.com/x/space/myinfo?jsonp=jsonp", cookie), MyInfo.class);
	}

	public static UserInfo getUserInfo(long id) {
		return GSON.fromJson(Network.httpGet("https://api.bilibili.com/x/space/acc/info?mid=" + id + "&jsonp=jsonp", AccountManager.get(0).cookie), UserInfo.class);
	}

	public static UidToRoom getUidToRoom(long uid) {
		return GSON.fromJson(Network.httpGet("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + uid), UidToRoom.class);
	}

	public static RoomToUid getRoomToUid(long roomId) {
		return GSON.fromJson(Network.httpGet("https://api.live.bilibili.com/live_user/v1/UserInfo/get_anchor_in_room?roomid=" + roomId), RoomToUid.class);
	}

	public static LivePart getLivePart() {
		return GSON.fromJson(Network.httpGet("https://api.live.bilibili.com/room/v1/Area/getList"), LivePart.class);
	}

	public static String getRoomInfo(long roomId) {
		return Network.httpGet("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=" + roomId);
	}

	public static Relation getRelation(long uid) {
		return GSON.fromJson(Network.httpGet("https://api.bilibili.com/x/relation/stat?vmid=" + uid + "&jsonp=jsonp"), Relation.class);
	}

	public static Upstat getUpstat(long uid) {
		return GSON.fromJson(Network.httpGet("https://api.bilibili.com/x/space/upstat?mid=" + uid + "&jsonp=jsonp"), Upstat.class);
	}

	public static String getFollowing(String cookie, long uid, int page, int pageSize) {
		return Network.httpGet("https://api.bilibili.com/x/relation/followings?vmid=" + uid + "&pn=1&ps=" + pageSize + "&order=desc&jsonp=jsonp", cookie);
	}

	public static FollowingLiving getFollowingLiving(String cookie, int page, int pageSize) {
		return GSON.fromJson(Network.httpGet("https://api.live.bilibili.com/relation/v1/feed/feed_list?page=" + page + "&pagesize=" + pageSize, cookie), FollowingLiving.class);
	}

	public static Medals getMedal(String cookie) {
		Medals mb = GSON.fromJson(Network.httpGet("https://api.live.bilibili.com/i/api/medal?page=1&pagesize=10", cookie), Medals.class);
		for (int i = mb.data.pageinfo.curPage + 1;i <= mb.data.pageinfo.totalpages;++i) {
			Medals tm = GSON.fromJson(Network.httpGet("https://api.live.bilibili.com/i/api/medal?page=" + i + "&pagesize=10", cookie), Medals.class);
			mb.data.fansMedalList.addAll(tm.data.fansMedalList);
		}
		return mb;
	}

	public static GiftBag getGiftBag(String cookie) {
		return GSON.fromJson(Network.httpGet("https://api.live.bilibili.com/xlive/web-room/v1/gift/bag_list?t=" + System.currentTimeMillis(), cookie), GiftBag.class);
	}

	public static String getMedalRank(String cookie, long uid, long roomId) {
		return Network.httpGet("https://api.live.bilibili.com/rankdb/v1/RoomRank/webMedalRank?roomid=" + roomId + "&ruid=" + uid, cookie);
	}

	public static String allSearch(String keyword, int page) {
		return Network.httpGet("https://api.bilibili.com/x/web-interface/search/all/v2?__refresh__=true&highlight=1&single_column=0&jsonp=jsonp&keyword=" + keyword + "&page=" + page);
	}

	public static String userSearch(String keyword, int page) {
		return Network.httpGet("https://api.bilibili.com/x/web-interface/search/type?search_type=bili_user&changing=mid&__refresh__=true&highlight=1&single_column=0&jsonp=jsonp&keyword=" + keyword + "&page=" + page);
	}

	public static String videoSearch(String keyword, int page) {
		return Network.httpGet("https://api.bilibili.com/x/web-interface/search/type?search_type=video&__refresh__=true&highlight=1&single_column=0&jsonp=jsonp&keyword=" + keyword + "&page=" + page);
	}

	public static String photoSearch(String keyword, int page) {
		return Network.httpGet("https://api.bilibili.com/x/web-interface/search/type?search_type=photo&__refresh__=true&highlight=1&single_column=0&jsonp=jsonp&keyword=" + keyword + "&page=" + page);
	}

	public static String getUserDynamicList(long uid, int offsetDynamicID) {
		return Network.httpGet("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?offset_dynamic_id=" + offsetDynamicID  + "&host_uid=" + uid);
	}

	public static String sendDynamic(String content, String cookie) {
		return Network.bilibiliPost("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/create", cookie, "dynamic_id", 0, "type", 4, "rid", 0, "content", content, "extension", "{\"from\":{\"emoji_type\":1}}", "at_uids", "", "ctrl", "[]", "csrf_token", getCsrf(cookie));
	}

	public static DynamicWithPictureResult sendDynamic(String content, String cookie, ArrayList<File> pics) {
		HashSet<UploadPicResult> bset=new HashSet<>(); 
		try {
			for (File pic:pics) {
				Connection.Response response = Jsoup.connect("https://api.vc.bilibili.com/api/v1/drawImage/upload").timeout(60000).method(Connection.Method.POST).userAgent(MainActivity.instance.userAgent).ignoreContentType(true).cookies(Network.cookieToMap(cookie)).data("file_up", pic.getName(), new FileInputStream(pic)).data("biz", "draw").data("category", "daily").execute();
				if (response.statusCode() != 200) {
					return null;
				} 
				JsonObject jo=new JsonParser().parse(response.body()).getAsJsonObject();
				if (jo.get("code").getAsInt() == 0) {
					JsonObject jobj=jo.get("data").getAsJsonObject();
					UploadPicResult upr=new UploadPicResult();
					upr.img_src = jobj.get("image_url").getAsString();
					upr.img_width = jobj.get("image_width").getAsInt();
					upr.img_height = jobj.get("image_height").getAsInt();
					upr.img_size = pic.length() / 1024.0f;
					bset.add(upr);
				} else {
					return null;
				}
			}
			String result=Network.bilibiliPost("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/create_draw", cookie, "biz", 3, "category", 3, "type", 0, "pictures", GSON.toJson(bset), "title", "", "tags", "", "description", "", "content", content, "setting", "{\"copy_forbidden\":0,\"cachedTime\":0}", "from", "create.dynamic.web", "extension", "{\"from\":{\"emoji_type\":1}}", "at_uids", "", "at_control", "[]", "csrf_token", getCsrf(cookie));
			pics.clear();
			return GSON.fromJson(result, DynamicWithPictureResult.class);
		} catch (Exception e) {
			return null;
		}
	}

	public static String sendArticalJudge(long cvId, String msg, String cookie) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/add", cookie, "Referer", "https://www.bilibili.com/", "type", 12, "message", msg, "plat", 1, "jsonp", "jsonp", "csrf", getCsrf(cookie));
	}

	public static String setMyInfo(String cookie, String newName, String newSign, String newSex, String newBirthday) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/member/web/update", cookie, "uname", newName, "usersign", newSign, "sex", newSex, "birthday", newBirthday, "csrf", getCsrf(cookie));
	}

	public static StartLive startLive(long roomID, String partID, String cookie) {
		if (partID == null) {
			partID = "235";
			MainActivity.instance.showToast("没有发现这个分区，已自动选择\"单机-其他分区\"");
		}
		String csrf = getCsrf(cookie);
		return GSON.fromJson(Network.bilibiliLivePost("https://api.live.bilibili.com/room/v1/Room/startLive", cookie, "Referer", "https://link.bilibili.com/p/center/index", "room_id", roomID, "platform", "pc", "area_v2", partID, "csrf_token", csrf, "csrf", csrf), StartLive.class);
	}

	public static StopLive stopLive(int roomID, String cookie) {
		String csrf = getCsrf(cookie);
		return GSON.fromJson(Network.bilibiliLivePost("https://api.live.bilibili.com/room/v1/Room/stopLive", cookie, "Referer", "https://link.bilibili.com/p/center/index", "room_id", roomID, "csrf_token", csrf, "csrf", csrf), StopLive.class);
	}

	public static String renameLive(int roomID, String newName, String cookie) {
		String csrf = getCsrf(cookie);
		return Network.bilibiliLivePost("https://api.live.bilibili.com/room/v1/Room/update", cookie, "Referer", "https://link.bilibili.com/p/center/index", "room_id", roomID, "title", newName, "csrf_token", csrf, "csrf", csrf);
	}

	public static LiveStream getLiveStream(long roomid, String cookie) {
		return GSON.fromJson(NetworkCacher.getNetJson("https://api.live.bilibili.com/live_stream/v1/StreamList/get_stream_by_roomId?room_id=" + roomid, cookie, "https://link.bilibili.com/p/center/index", NetworkCacher.Mode.CachePrefer), LiveStream.class);
	}

	public static String sendLiveSign(String cookie) {
		return Network.httpGet("https://api.live.bilibili.com/sign/doSign", cookie, "https://live.bilibili.com/" + new Random().nextInt(10_000_000));
	}

	public static String sendHotStrip(long myUid, long roomMasterUid, long roomID, int count, String cookie) {
		String csrf = getCsrf(cookie);
		return Network.bilibiliLivePost("http://api.live.bilibili.com/gift/v2/gift/send", cookie, "Referer" , "https://live.bilibili.com/" + roomID, "uid", myUid, "gift_id", 1, "ruid", roomMasterUid, "gift_num", count, "coin_type", "silver", "bag_id", 0, "platform", "pc", "biz_code", "live", "biz_id", roomID, "rnd", System.currentTimeMillis() / 1000, "metadata", "", "price", 0, "csrf_token", csrf, "csrf", csrf, "visit_id", "");
	}

	public static String followUser(String cookie, long UID) {
		String firstStep=Network.bilibiliMainPost("https://api.bilibili.com/x/relation/modify?cross_domain=true", cookie, REFERER, "https://www.bilibili.com/video/av" + new Random().nextInt(47957369), "fid", UID, "act", 1, "re_src", 122, "csrf", getCsrf(cookie));
		if (new JsonParser().parse(firstStep).getAsJsonObject().get("code").getAsInt() != 0) {
			return "关注失败";
		}
		return Network.bilibiliMainPost("https://api.bilibili.com/x/relation/tags/addUsers?cross_domain=true", cookie, REFERER, "https://www.bilibili.com/video/av" + new Random().nextInt(47957369), "fids", UID, "tagids", 0, "csrf", getCsrf(cookie));
	}

	public static String getCsrf(String cookie) {
		return Network.cookieToMap(cookie).get("bili_jct");
	}

	public static String getCsrf(AccountInfo ai) {
		return getCsrf(ai.cookie);
	}

	//收藏,未完成
	public static void sendFavorite(long uid, long aid, String cookie) {
		FavoriteList flist=GSON.fromJson(Network.httpGet("http://space.bilibili.com/ajax/fav/getBoxList?mid=" + uid, cookie), FavoriteList.class);
		System.out.println("flist:" + flist.toString());
		try {
			String url = "https://api.bilibili.com/medialist/gateway/coll/resource/deal";
			String csrf = "rid=" + aid + "&type=2&add_media_ids=" + flist.data.list.get(0).fav_box + "21" + "&del_media_ids=&jsonp=jsonp&csrf=" + getCsrf(cookie);
			System.out.println("post:" + csrf);
			Connection.Response cr=Jsoup.connect(url).method(Connection.Method.POST).cookies(Network.cookieToMap(cookie))
				.ignoreContentType(true)
				.header("Referer", "https://www.bilibili.com/anime")
				/*	.data("rid", aid)
				 .data("type", 2)
				 .data("add_media_ids", flist.data.list.get(0).fav_box + "21")
				 .data("del_media_ids", "")
				 .data("csrf", getCsrf(cookie))*/

				.requestBody(csrf)
				.execute();
			System.out.println("cookie:" + cookie);
			System.out.println("csrf:" + getCsrf(cookie));

			System.out.println(cr.body());
			String bilibiliMainPost = cr.body();// Network.bilibiliPost(url, cookie, "rid", aid, "type", 2, "add_media_ids", flist.data.list.get(0).fav_box + "21", "del_media_ids", "", "csrf", getCsrf(cookie));
			JSONObject result = new JSONObject(bilibiliMainPost);
			MainActivity.instance.showToast(result.optInt("code") == 0 ?"收藏成功": bilibiliMainPost);
		} catch (JSONException | NullPointerException | IOException e) {
			e.printStackTrace();
		}	
//			//https://api.bilibili.com/medialist/gateway/base/created?pn=1&ps=100&type=2&rid=55340268&up_mid=64483321
//			//https://api.bilibili.com/medialist/gateway/coll/resource/deal
//			//rid=55340268&type=2&add_media_ids=101411121&del_media_ids=&jsonp=jsonp&csrf=14f4956b04e6775a3a32ca47a30b5d54
//			/*String favoriteJson=Network.getSourceCode("https://api.bilibili.com/medialist/gateway/base/created?pn=1&ps=100&type=2&rid=55340268&up_mid=64483321",cookie);
//			 JsonObject fjobj=new JsonParser().parse(favoriteJson).getAsJsonObject().get("data").getAsJsonObject();
//			 JsonArray fja=fjobj.get("list").getAsJsonArray();
//			 long add_media_id=fja.get(0).getAsJsonObject().get("id").getAsLong();
//
//			 */
//			Connection connection = Jsoup.connect("https://api.bilibili.com/medialist/gateway/coll/resource/deal");
//			connection.userAgent(MainActivity.instance.userAgent)
//                .headers(mainHead)
//                .ignoreContentType(true)
//                .referrer("https://www.bilibili.com/video/av" + AID)
//                .cookies(Network.cookieToMap(cookie))
//                .method(Connection.Method.POST)
//                .data("rid", String.valueOf(AID))
//                .data("multiply", String.valueOf(count))
//                .data("select_like", "0")
//                .data("cross_domain", "true")
//                .data("csrf", getCsrf(cookie));
//			Connection.Response response=null;
//			try {
//				response = connection.execute();
//			} catch (IOException e) {
//				MainActivity.instance.showToast("连接出错");
//				return;
//			}
//			if (response.statusCode() != 200) {
//				MainActivity.instance.showToast(String.valueOf(response.statusCode()));
//			}
//			JsonParser parser = new JsonParser();
//			JsonObject obj = parser.parse(response.body()).getAsJsonObject();
//			MainActivity.instance.showToast(obj.get("message").getAsString());
	}

	public static String sendCvCoin(int count, long CvId, String cookie) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/web-interface/coin/add", cookie, REFERER, "https://www.bilibili.com/read/cv" + CvId, "aid", CvId, "multiply", count, "upid", String.valueOf(Bilibili.getCvInfo(CvId).data.mid), "avtype", 2, "csrf", getCsrf(cookie));
	}

	public static String sendAvCoin(int count, long AID, String cookie) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/web-interface/coin/add", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "aid", AID, "multiply", count, "select_like", 0, "cross_domain", "true", "csrf", getCsrf(cookie));
	}

	public static String sendVideoJudge(String content, long AID, String cookie) {		
		return Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/add", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "message", content, "jsonp", "jsonp", "csrf", getCsrf(cookie));
	}

	public static String sendVideoJudge(String content, long AID, long rootId, long parentId, String cookie) {		
		return Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/add", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "root", rootId, "parent", parentId, "message", content, "jsonp", "jsonp", "csrf", getCsrf(cookie));
	}

	public static String sendLikeReply(long AID, long rpid, boolean deLike, String cookie) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/action", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "rpid", rpid, "action", deLike ?0: 1, "jsonp", "jsonp", "csrf", getCsrf(cookie));
	}

	public static String sendDisikeReply(long AID, long rpid, boolean deDislike, String cookie) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/hate", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "rpid", rpid, "action", deDislike ?0: 1, "jsonp", "jsonp", "csrf", getCsrf(cookie));
	}

	public static String deleteReply(long AID, long rpid, String cookie) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/del", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "rpid", rpid, "jsonp", "jsonp", "csrf", getCsrf(cookie));
	}

	public static String sendCvLike(long cvID, String cookie) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/article/like", cookie, REFERER, "https://www.bilibili.com/read/cv" + cvID, "id", cvID, "type", 1, "jsonp", "jsonp", "csrf", getCsrf(cookie));
	}

	public static String sendAvLike(long AID, String cookie) {
		return Network.bilibiliMainPost("https://api.bilibili.com/x/web-interface/archive/like", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "aid", AID, "like", 1, "csrf", getCsrf(cookie));
	}

	public static String sendLiveDanmaku(String msg, String cookie, long roomId) {
		String csrf = getCsrf(cookie);
		return Network.bilibiliLivePost("http://api.live.bilibili.com/msg/send", cookie, REFERER, "https://live.bilibili.com/" + roomId, "color", 0xffffff, "fontsize", 25, "mode", 1, "msg", msg, "rnd", System.currentTimeMillis() / 1000, "roomid", roomId, "bubble", 0, "csrf_token", csrf, "csrf", csrf);
	}
}
