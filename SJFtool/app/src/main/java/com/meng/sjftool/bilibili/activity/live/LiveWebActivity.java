package com.meng.sjftool.bilibili.activity.live;

import android.app.*;
import android.content.*;
import android.os.*;
import android.webkit.*;
import com.meng.sjftool.*;

public class LiveWebActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView=new WebView(this);
        setContentView(webView);
        Intent intent=getIntent();

		WebSettings settings=webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true); // 关键点
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setSupportZoom(true); // 支持缩放
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
		settings.setUserAgentString(MainActivity.instance.userAgent);
		webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}

				@Override
				public void onPageFinished(WebView view, final String url) {
					super.onPageFinished(view, url);
				} 
			});
		CookieSyncManager.createInstance(getApplicationContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
		cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie("https://www.bilibili.com/", intent.getStringExtra("cookies"));//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
        webView.loadUrl(intent.getStringExtra("url"));
    }
}
