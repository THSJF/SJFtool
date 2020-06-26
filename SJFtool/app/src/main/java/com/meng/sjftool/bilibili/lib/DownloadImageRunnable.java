package com.meng.sjftool.bilibili.lib;

import android.graphics.*;
import android.widget.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.result.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.net.*;

public class DownloadImageRunnable implements Runnable {

	public static final int QQGroup=0;
    public static final int QQUser=1;
    public static final int BilibiliUser=2;
    private ImageView imageView;
    private int headType;
    private long id;
    private File imageFile;


    public DownloadImageRunnable(ImageView imageView, long id, int headType) {
        this.imageView = imageView;
        this.headType = headType;
        this.id = id;
    }

    @Override
    public void run() {
        switch (headType) {
            case QQGroup:
                imageFile = new File(MainActivity.instance.mainDic + "group/" + id + ".jpg");
                if (imageFile.exists()) {
                    return;
                }
                downloadFile("http://p.qlogo.cn/gh/" + id + "/" + id + "/100/");
                break;
            case QQUser:
                imageFile = new File(MainActivity.instance.mainDic + "user/" + id + ".jpg");
                if (imageFile.exists()) {
                    return;
                }
                downloadFile("http://q2.qlogo.cn/headimg_dl?bs=" + id + "&dst_uin=" + id + "&dst_uin=" + id + "&;dst_uin=" + id + "&spec=100&url_enc=0&referer=bu_interface&term_type=PC");
                break;
            case BilibiliUser:
                imageFile = new File(MainActivity.instance.mainDic + "bilibili/" + id + ".jpg");
                if (imageFile.exists()) {
                    return;
                }
                downloadFile(getBilibiliHeadUrl(id));
                break;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    private String getBilibiliHeadUrl(long uid) {
        try {
            URL url = new URL("https://api.bilibili.com/x/space/acc/info?mid=" + uid + "&jsonp=jsonp");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            UserInfo bilibiliPersonInfoJavaBean = GSON.fromJson(stringBuilder.toString(), UserInfo.class);
            return bilibiliPersonInfoJavaBean.data.face;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void downloadFile(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
            InputStream is = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(imageFile);
            byte buf[] = new byte[4096];
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            is.close();
            connection.disconnect();
			final Bitmap bmp=BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            MainActivity.instance.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						imageView.setImageBitmap(bmp);
					}
				});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

