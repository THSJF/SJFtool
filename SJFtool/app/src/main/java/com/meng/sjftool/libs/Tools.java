package com.meng.sjftool.libs;

import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.webkit.*;
import com.google.gson.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.bilibili.result.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.jsoup.*;

public class Tools {
	public static Map<String, String> liveHead = new HashMap<>();
    public static Map<String, String> mainHead = new HashMap<>();
	public static String REFERER = "Referer";

	static{
		liveHead.put("Host", "api.live.bilibili.com");
        liveHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
        liveHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        liveHead.put("Connection", "keep-alive");
        liveHead.put("Origin", "https://live.bilibili.com");

        mainHead.put("Host", "api.bilibili.com");
        mainHead.put("Accept", "application/json, text/javascript, */*; q=0.01");
        mainHead.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        mainHead.put("Connection", "keep-alive");
        mainHead.put("Origin", "https://www.bilibili.com");
	}

	public static class AndroidContent {

		public static void runJs(Context c, String js, ValueCallback<String> callBack) {
			WebView wv=new WebView(c);
			wv.getSettings().setJavaScriptEnabled(true);
			wv.evaluateJavascript(js.startsWith("javascript:") ?js: "javascript:" + js, callBack);
		}

        public static int px2dp(float pxValue) { 
            return (int)(pxValue / MainActivity.instance.getResources().getDisplayMetrics().density + 0.5f); 
        } 

        public static int dp2px(float dipValue) { 
            return (int)(dipValue * MainActivity.instance.getResources().getDisplayMetrics().density + 0.5f); 
		} 

        public static int px2sp(float pxValue) { 
            return (int)(pxValue / MainActivity.instance.getResources().getDisplayMetrics().scaledDensity + 0.5f); 
        }

		public static int sp2px(float spValue) { 
			float fontScale = MainActivity.instance.getResources().getDisplayMetrics().scaledDensity; 
            return (int)(spValue * fontScale + 0.5f); 
        }

		public static void copyToClipboard(String s) {
			((ClipboardManager)MainActivity.instance.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text", s));
			MainActivity.instance.showToast("已复制到剪贴板");
		}

		public static byte[] readAssets(String fileName) {
			byte[] buffer = null;
			try {
				InputStream in =MainActivity.instance.getResources().getAssets().open(fileName);
				int lenght = in.available();
				buffer = new byte[lenght];
				in.read(buffer);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return buffer;
		}

		public static String readAssetsString(String fileName) {
			return new String(readAssets(fileName));
		}

		public static String absolutePathFromUri(Context context, Uri uri) {
			final boolean isKitKat=Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
			if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
				if (isExternalStorageDocument(uri)) {
					final String docId=DocumentsContract.getDocumentId(uri);
					final String[] split=docId.split(":");
					if ("primary".equalsIgnoreCase(split[0])) {
						return Environment.getExternalStorageDirectory() + "/" + split[1];
					}
				} else if (isDownloadsDocument(uri)) {
					final String id=DocumentsContract.getDocumentId(uri);
					final Uri contentUri=ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
					return getDataColumn(context, contentUri, null, null);
				} else if (isMediaDocument(uri)) {
					final String docId=DocumentsContract.getDocumentId(uri);
					final String[] split=docId.split(":");
					Uri contentUri=null;
					if ("image".equals(split[0])) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(split[0])) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(split[0])) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}
					final String selection="_id=?";
					final String[] selectionArgs=new String[]{
						split[1]
					};
					return getDataColumn(context, contentUri, selection, selectionArgs);
				}
			} else if ("content".equalsIgnoreCase(uri.getScheme())) {
				return getDataColumn(context, uri, null, null);
			} else if ("file".equalsIgnoreCase(uri.getScheme())) {
				return uri.getPath();
			}
			return null;
		}

		private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
			Cursor cursor=null;
			final String column="_data";
			final String[] projection={
				column
			};
			try {
				cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
				if (cursor != null && cursor.moveToFirst()) {
					return cursor.getString(cursor.getColumnIndexOrThrow(column));
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
			return null;
		}

		private static boolean isExternalStorageDocument(Uri uri) {
			return "com.android.externalstorage.documents".equals(uri.getAuthority());
		}

		private static boolean isDownloadsDocument(Uri uri) {
			return "com.android.providers.downloads.documents".equals(uri.getAuthority());
		}

		private static boolean isMediaDocument(Uri uri) {
			return "com.android.providers.media.documents".equals(uri.getAuthority());
		}
	}

	public static class BilibiliTool {

		public static CvInfo getCvInfo(long cvId) {
			return GSON.fromJson(Tools.Network.httpGet("http://api.bilibili.com/x/article/viewinfo?id=" + cvId + "&mobi_app=pc&jsonp=jsonp"), CvInfo.class);
		}

		public static VideoReply getVideoJudge(long aid) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.bilibili.com/x/v2/reply?jsonp=jsonp&pn=1&type=1&sort=1&oid=" + aid), VideoReply.class);
		}

		public static VideoReply getVideoJudge(long aid, long root) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.bilibili.com/x/v2/reply/reply?jsonp=jsonp&pn=1&type=1&sort=1&oid=" + aid + "&ps=10&root=" + root + "&_=" + System.currentTimeMillis()), VideoReply.class);
		}

		public static MyInfo getMyInfo(String cookie) {
			return GSON.fromJson(Tools.Network.httpGet("http://api.bilibili.com/x/space/myinfo?jsonp=jsonp", cookie), MyInfo.class);
		}

		public static UserInfo getUserInfo(long id) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.bilibili.com/x/space/acc/info?mid=" + id + "&jsonp=jsonp", AccountManager.get(0).cookie), UserInfo.class);
		}

		public static UidToRoom getUidToRoom(long uid) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=" + uid), UidToRoom.class);
		}

		public static RoomToUid getRoomToUid(long roomId) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.live.bilibili.com/live_user/v1/UserInfo/get_anchor_in_room?roomid=" + roomId), RoomToUid.class);
		}

		public static LivePart getLivePart() {
			return GSON.fromJson(Tools.Network.httpGet("https://api.live.bilibili.com/room/v1/Area/getList"), LivePart.class);
		}

		public static String getRoomInfo(long roomId) {
			return Tools.Network.httpGet("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom?room_id=" + roomId);
		}

		public static Relation getRelation(long uid) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.bilibili.com/x/relation/stat?vmid=" + uid + "&jsonp=jsonp"), Relation.class);
		}

		public static Upstat getUpstat(long uid) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.bilibili.com/x/space/upstat?mid=" + uid + "&jsonp=jsonp"), Upstat.class);
		}

		public static String getFollowing(String cookie, long uid, int page, int pageSize) {
			return Tools.Network.httpGet("https://api.bilibili.com/x/relation/followings?vmid=" + uid + "&pn=1&ps=" + pageSize + "&order=desc&jsonp=jsonp", cookie);
		}

		public static FollowingLiving getFollowingLiving(String cookie, int page, int pageSize) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.live.bilibili.com/relation/v1/feed/feed_list?page=" + page + "&pagesize=" + pageSize, cookie), FollowingLiving.class);
		}

		public static Medals getMedal(String cookie) {
			String v = "https://api.live.bilibili.com/i/api/medal?page=%d&pagesize=10";
			Medals mb = GSON.fromJson(Tools.Network.httpGet(String.format(v, 1), cookie), Medals.class);
			for (int i = mb.data.pageinfo.curPage;i < mb.data.pageinfo.totalpages;++i) {
				Medals tm = GSON.fromJson(Tools.Network.httpGet(String.format(v, i), cookie), Medals.class);
				mb.data.fansMedalList.addAll(tm.data.fansMedalList);
			}
			return mb;
		}

		public static GiftBag getGiftBag(String cookie) {
			return GSON.fromJson(Tools.Network.httpGet("https://api.live.bilibili.com/xlive/web-room/v1/gift/bag_list?t=" + System.currentTimeMillis(), cookie), GiftBag.class);
		}

		public static String getMedalRank(String cookie, long uid, long roomId) {
			return Tools.Network.httpGet("https://api.live.bilibili.com/rankdb/v1/RoomRank/webMedalRank?roomid=" + roomId + "&ruid=" + uid, cookie);
		}

		public static String allSearch(String keyword, int page) {
			return Tools.Network.httpGet("https://api.bilibili.com/x/web-interface/search/all/v2?__refresh__=true&highlight=1&single_column=0&jsonp=jsonp&keyword=" + keyword + "&page=" + page);
		}

		public static String userSearch(String keyword, int page) {
			return Tools.Network.httpGet("https://api.bilibili.com/x/web-interface/search/type?search_type=bili_user&changing=mid&__refresh__=true&highlight=1&single_column=0&jsonp=jsonp&keyword=" + keyword + "&page=" + page);
		}

		public static String videoSearch(String keyword, int page) {
			return Tools.Network.httpGet("https://api.bilibili.com/x/web-interface/search/type?search_type=video&__refresh__=true&highlight=1&single_column=0&jsonp=jsonp&keyword=" + keyword + "&page=" + page);
		}

		public static String photoSearch(String keyword, int page) {
			return Tools.Network.httpGet("https://api.bilibili.com/x/web-interface/search/type?search_type=photo&__refresh__=true&highlight=1&single_column=0&jsonp=jsonp&keyword=" + keyword + "&page=" + page);
		}

		public static String getUserDynamicList(long uid, int offsetDynamicID) {
			return Tools.Network.httpGet("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?offset_dynamic_id=" + offsetDynamicID  + "&host_uid=" + uid);
		}

		public static String sendDynamic(String content, String cookie) {
			return Tools.Network.bilibiliPost("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/create", cookie, "dynamic_id", 0, "type", 4, "rid", 0, "content", content, "extension", "{\"from\":{\"emoji_type\":1}}", "at_uids", "", "ctrl", "[]", "csrf_token", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static DynamicWithPictureResult sendDynamic(String content, String cookie, ArrayList<File> pics) {
			HashSet<UploadPicResult> bset=new HashSet<>(); 
			try {
				for (File pic:pics) {
					Connection.Response response = Jsoup.connect("https://api.vc.bilibili.com/api/v1/drawImage/upload").timeout(60000).method(Connection.Method.POST).userAgent(MainActivity.instance.userAgent).ignoreContentType(true).cookies(Tools.Network.cookieToMap(cookie)).data("file_up", pic.getName(), new FileInputStream(pic)).data("biz", "draw").data("category", "daily").execute();
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
				String result=Tools.Network.bilibiliPost("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/create_draw", cookie, "biz", 3, "category", 3, "type", 0, "pictures", GSON.toJson(bset), "title", "", "tags", "", "description", "", "content", content, "setting", "{\"copy_forbidden\":0,\"cachedTime\":0}", "from", "create.dynamic.web", "extension", "{\"from\":{\"emoji_type\":1}}", "at_uids", "", "at_control", "[]", "csrf_token", Tools.BilibiliTool.getCsrf(cookie));
				pics.clear();
				return GSON.fromJson(result, DynamicWithPictureResult.class);
			} catch (Exception e) {
				return null;
			}
		}

		public static String sendArticalJudge(long cvId, String msg, String cookie) {
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/add", cookie, "Referer", "https://www.bilibili.com/", "type", 12, "message", msg, "plat", 1, "jsonp", "jsonp", "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static StartLive startLive(long roomID, String partID, String cookie) {
			if (partID == null) {
				partID = "235";
				MainActivity.instance.showToast("没有发现这个分区，已自动选择\"单机-其他分区\"");
			}
			String csrf = Tools.BilibiliTool.getCsrf(cookie);
			return GSON.fromJson(Tools.Network.bilibiliLivePost("https://api.live.bilibili.com/room/v1/Room/startLive", cookie, "Referer", "https://link.bilibili.com/p/center/index", "room_id", roomID, "platform", "pc", "area_v2", partID, "csrf_token", csrf, "csrf", csrf), StartLive.class);
		}

		public static StopLive stopLive(int roomID, String cookie) {
			String csrf = Tools.BilibiliTool.getCsrf(cookie);
			return GSON.fromJson(Tools.Network.bilibiliLivePost("https://api.live.bilibili.com/room/v1/Room/stopLive", cookie, "Referer", "https://link.bilibili.com/p/center/index", "room_id", roomID, "csrf_token", csrf, "csrf", csrf), StopLive.class);
		}

		public static String renameLive(int roomID, String newName, String cookie) {
			String csrf = Tools.BilibiliTool.getCsrf(cookie);
			return Tools.Network.bilibiliLivePost("https://api.live.bilibili.com/room/v1/Room/update", cookie, "Referer", "https://link.bilibili.com/p/center/index", "room_id", roomID, "title", newName, "csrf_token", csrf, "csrf", csrf);
		}

		public static LiveStream getLiveStream(long roomid, String cookie) {
			return GSON.fromJson(NetworkCacher.getNetJson("https://api.live.bilibili.com/live_stream/v1/StreamList/get_stream_by_roomId?room_id=" + roomid, cookie, "https://link.bilibili.com/p/center/index", NetworkCacher.Mode.CachePrefer), LiveStream.class);
		}

		public static String sendLiveSign(String cookie) {
			return Tools.Network.httpGet("https://api.live.bilibili.com/sign/doSign", cookie, "https://live.bilibili.com/" + new Random().nextInt(10_000_000));
		}

		public static String sendHotStrip(long myUid, long roomMasterUid, long roomID, int count, String cookie) {
			String csrf = Tools.BilibiliTool.getCsrf(cookie);
			return Tools.Network.bilibiliLivePost("http://api.live.bilibili.com/gift/v2/gift/send", cookie, "Referer" , "https://live.bilibili.com/" + roomID, "uid", myUid, "gift_id", 1, "ruid", roomMasterUid, "gift_num", count, "coin_type", "silver", "bag_id", 0, "platform", "pc", "biz_code", "live", "biz_id", roomID, "rnd", System.currentTimeMillis() / 1000, "metadata", "", "price", 0, "csrf_token", csrf, "csrf", csrf, "visit_id", "");
		}

		public static String followUser(String cookie, long UID) {
			String firstStep=Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/relation/modify?cross_domain=true", cookie, REFERER, "https://www.bilibili.com/video/av" + new Random().nextInt(47957369), "fid", UID, "act", 1, "re_src", 122, "csrf", Tools.BilibiliTool.getCsrf(cookie));
			if (new JsonParser().parse(firstStep).getAsJsonObject().get("code").getAsInt() != 0) {
				return "关注失败";
			}
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/relation/tags/addUsers?cross_domain=true", cookie, REFERER, "https://www.bilibili.com/video/av" + new Random().nextInt(47957369), "fids", UID, "tagids", 0, "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String getCsrf(String cookie) {
			return Tools.Network.cookieToMap(cookie).get("bili_jct");
		}

		public static String getCsrf(AccountInfo ai) {
			return getCsrf(ai.cookie);
		}

		//收藏,未完成
//		public static void sendFavorite(int count, long AID, String cookie) {
//
//			//https://api.bilibili.com/medialist/gateway/base/created?pn=1&ps=100&type=2&rid=55340268&up_mid=64483321
//			//https://api.bilibili.com/medialist/gateway/coll/resource/deal
//			//rid=55340268&type=2&add_media_ids=101411121&del_media_ids=&jsonp=jsonp&csrf=14f4956b04e6775a3a32ca47a30b5d54
//			/*String favoriteJson=Tools.Network.getSourceCode("https://api.bilibili.com/medialist/gateway/base/created?pn=1&ps=100&type=2&rid=55340268&up_mid=64483321",cookie);
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
//                .cookies(Tools.Network.cookieToMap(cookie))
//                .method(Connection.Method.POST)
//                .data("rid", String.valueOf(AID))
//                .data("multiply", String.valueOf(count))
//                .data("select_like", "0")
//                .data("cross_domain", "true")
//                .data("csrf", Tools.BilibiliTool.getCsrf(cookie));
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
//		}

		public static String sendCvCoin(int count, long CvId, String cookie) {
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/web-interface/coin/add", cookie, REFERER, "https://www.bilibili.com/read/cv" + CvId, "aid", CvId, "multiply", count, "upid", String.valueOf(Tools.BilibiliTool.getCvInfo(CvId).data.mid), "avtype", 2, "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String sendAvCoin(int count, long AID, String cookie) {
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/web-interface/coin/add", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "aid", AID, "multiply", count, "select_like", 0, "cross_domain", "true", "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String sendVideoJudge(String content, long AID, String cookie) {		
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/add", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "message", content, "jsonp", "jsonp", "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String sendVideoJudge(String content, long AID, long rootId, long parentId, String cookie) {		
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/add", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "root", rootId, "parent", parentId, "message", content, "jsonp", "jsonp", "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String sendLikeReply(long AID, long rpid, boolean deLike, String cookie) {
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/action", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "rpid", rpid, "action", deLike ?0: 1, "jsonp", "jsonp", "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String sendDisikeReply(long AID, long rpid, boolean deDislike, String cookie) {
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/hate", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "rpid", rpid, "action", deDislike ?0: 1, "jsonp", "jsonp", "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String deleteReply(long AID, long rpid, String cookie) {
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/v2/reply/del", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "oid", AID, "type", 1, "rpid", rpid, "jsonp", "jsonp", "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String sendCvLike(long cvID, String cookie) {
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/article/like", cookie, REFERER, "https://www.bilibili.com/read/cv" + cvID, "id", cvID, "type", 1, "jsonp", "jsonp", "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String sendAvLike(long AID, String cookie) {
			return Tools.Network.bilibiliMainPost("https://api.bilibili.com/x/web-interface/archive/like", cookie, REFERER, "https://www.bilibili.com/video/av" + AID, "aid", AID, "like", 1, "csrf", Tools.BilibiliTool.getCsrf(cookie));
		}

		public static String sendLiveDanmaku(String msg, String cookie, long roomId) {
			String csrf = Tools.BilibiliTool.getCsrf(cookie);
			return Tools.Network.bilibiliLivePost("http://api.live.bilibili.com/msg/send", cookie, REFERER, "https://live.bilibili.com/" + roomId, "color", 0xffffff, "fontsize", 25, "mode", 1, "msg", msg, "rnd", System.currentTimeMillis() / 1000, "roomid", roomId, "bubble", 0, "csrf_token", csrf, "csrf", csrf);
		}
	}

	public static class Network {

		private static String bilibiliPost(String url, String cookie, Map<String,String> headers, Object... params) {
			Connection connection = Jsoup.connect(url);
			connection.userAgent(MainActivity.instance.userAgent);
			if (headers != null) {
				connection.headers(headers);
			}
			connection.ignoreContentType(true)
                .cookies(Tools.Network.cookieToMap(cookie))
                .method(Connection.Method.POST)
			    .data(params);
			Connection.Response response=null;
			try {
				response = connection.execute();
			} catch (IOException e) {
				MainActivity.instance.showToast("连接出错");
				return null;
			}
			if (response.statusCode() != 200) {
				MainActivity.instance.showToast(String.valueOf(response.statusCode()));
			}
			Log.network(Connection.Method.POST, url, formatJson(response.body()), params);
			return response.body();
		}

		public static String bilibiliPost(String url, String cookie, Object... params) {
			return bilibiliPost(url, cookie, null, params);
		}

		public static String bilibiliMainPost(String url, String cookie, Object... params) {
			return bilibiliPost(url, cookie, mainHead, params);
		}

		public static String bilibiliLivePost(String url, String cookie, Object... params) {
			return bilibiliPost(url, cookie, liveHead, params);
		}

		public static Map<String, String> cookieToMap(String value) {
			Map<String, String> map = new HashMap<>();
			String[] values = value.split("; ");
			for (String val : values) {
				String[] vals = val.split("=");
				if (vals.length == 2) {
					map.put(vals[0], vals[1]);
				} else if (vals.length == 1) {
					map.put(vals[0], "");
				}
			}
			return map;
		}

		public static String getRealUrl(String surl) throws Exception {
			URL url = new URL(surl);
			URLConnection conn = url.openConnection();
			conn.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			String nurl = conn.getURL().toString();
			in.close();
			return nurl;
		}

		public static String httpGet(String url) {
			return httpGet(url, null, null);
		}

		public static String httpGet(String url, String cookie) {
			return httpGet(url, cookie, null);
		}

		public static String httpGet(String url, String cookie, String refer) {
			Connection.Response response = null;
			Connection connection;
			try {
				connection = Jsoup.connect(url);
				if (cookie != null) {
					connection.cookies(cookieToMap(cookie));
				}
				if (refer != null) {
					connection.referrer(refer);
				}
				connection.userAgent(MainActivity.instance.userAgent);
				connection.ignoreContentType(true).method(Connection.Method.GET);
				response = connection.execute();
				if (response.statusCode() != 200) {
					MainActivity.instance.showToast(String.valueOf(response.statusCode()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//	if (!url.contains("/room/v1/Area/getList")) {
			Log.network(Connection.Method.GET, url, formatJson(response.body()));
			//	}
			return response.body();
		}

		public static String formatJson(String content) {
			if (content == null) {
				return "{}";
			}
			StringBuilder sb = new StringBuilder();
			int index = 0;
			int count = 0;
			while (index < content.length()) {
				char ch = content.charAt(index);
				if (ch == '{' || ch == '[') {
					sb.append(ch);
					sb.append('\n');
					count++;
					for (int i = 0; i < count; i++) {                   
						sb.append('\t');
					}
				} else if (ch == '}' || ch == ']') {
					sb.append('\n');
					count--;
					for (int i = 0; i < count; i++) {                   
						sb.append('\t');
					}
					sb.append(ch);
				} else if (ch == ',') {
					sb.append(ch);
					sb.append('\n');
					for (int i = 0; i < count; i++) {                   
						sb.append('\t');
					}
				} else {
					sb.append(ch);              
				}
				index++;
			}
			return sb.toString();
		}
		/**
		 * 把格式化的json紧凑
		 * @param content
		 * @return
		 */
		public static String compactJson(String content) {
			String regEx="[\t\n]"; 
			Pattern p = Pattern.compile(regEx); 
			Matcher m = p.matcher(content);
			return m.replaceAll("").trim();
		}
	}

	public static class FileTool {
		public static void deleteFiles(File folder) {
			File[] fs = folder.listFiles();
			for (File f : fs) {
				if (f.isDirectory()) {
					deleteFiles(f);
					f.delete();
				} else {
					f.delete();
				}
			}
		}

		public static void fileCopy(String src, String des) {
			try {
				BufferedInputStream bis = null;
				bis = new BufferedInputStream(new FileInputStream(src));
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
				int i = -1;
				byte[] bt = new byte[2014];
				while ((i = bis.read(bt)) != -1) {
					bos.write(bt, 0, i);
				}
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static String readString(String fileName) {
			return readString(new File(fileName));
		}

		public static String readString(File f) {
			try {      
				long filelength = f.length();
				byte[] filecontent = new byte[(int) filelength];
				FileInputStream in = new FileInputStream(f);
				in.read(filecontent);
				in.close();
				return new String(filecontent, StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public static byte[] readBytes(File f) {
			byte[] filecontent=null;
			try {
				long filelength = f.length();
				filecontent = new byte[(int) filelength];
				FileInputStream in = new FileInputStream(f);
				in.read(filecontent);
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return filecontent;
		}
		public static byte[] readBytes(String path) {
			return readBytes(new File(path));
		}
	}

	public static class Time {
		public static String getTime() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
		public static String getTime(long timeStamp) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeStamp));
		}
		public static String getDate() {
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		public static String getDate(long timeStamp) {
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStamp));
		}
	}
}

