package com.meng.sjftool.libs;

import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.webkit.*;
import com.meng.sjftool.*;
import java.io.*;

public class AndroidContent {

	public static void runJs(Context c, String js, ValueCallback<String> callBack) {
		WebView wv = new WebView(c);
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
