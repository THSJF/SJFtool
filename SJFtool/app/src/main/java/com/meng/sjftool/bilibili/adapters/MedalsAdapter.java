package com.meng.sjftool.bilibili.adapters;

import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.bilibili.result.*;
import java.io.*;

public class MedalsAdapter extends BaseAdapter {

	private Medals medals;
    public MedalsAdapter(Medals medals) {
        this.medals = medals;
    }

    public int getCount() {
        return medals.data.fansMedalList.size();
    }

    public Medals.FansMedal getItem(int position) {
        return medals.data.fansMedalList.get(position);
    }

    public long getItemId(int position) {
        return medals.data.fansMedalList.get(position).hashCode();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
        if (convertView == null) {
            convertView = MainActivity.instance.getLayoutInflater().inflate(R.layout.medal_list_item, null);
            holder = new ViewHolder();
            holder.tvMedalName = (TextView) convertView.findViewById(R.id.medal_list_itemTextView_medalName);
			holder.tvMasterName = (TextView) convertView.findViewById(R.id.medal_list_itemTextView_masterName);
			holder.tvLevel = (TextView) convertView.findViewById(R.id.medal_list_itemTextView_medal_level);
			holder.tvLevelLimit = (TextView) convertView.findViewById(R.id.medal_list_itemTextView_level_limit);
			holder.tvTodayLimit = (TextView) convertView.findViewById(R.id.medal_list_itemTextView_today_limit);
			holder.ivHead = (ImageView) convertView.findViewById(R.id.medal_list_itemImageView_head);
			convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
		final Medals.FansMedal mfm=medals.data.fansMedalList.get(position);
		holder.tvMedalName.setText(mfm.medal_name);
		holder.tvMedalName.setTextColor(mfm.color | 0xFF000000);
		holder.tvMasterName.setText(mfm.target_name);
		holder.tvLevel.setText("lv." + mfm.level);
		holder.tvTodayLimit.setText(String.format("今日%d/%d", mfm.today_feed, mfm.day_limit));
		holder.tvLevelLimit.setText(String.format("当前等级%d/%d", mfm.intimacy, mfm.next_intimacy));
		File bilibiliImageFile = new File(MainActivity.instance.mainDic + "bilibili/" + mfm.target_id + ".jpg");
        if (bilibiliImageFile.exists()) {
            holder.ivHead.setImageBitmap(BitmapFactory.decodeFile(bilibiliImageFile.getAbsolutePath()));
        } else {
            if (MainActivity.onWifi) {
                MainActivity.instance.threadPool.execute(new DownloadImageRunnable(holder.ivHead, mfm.target_id, DownloadImageRunnable.BilibiliUser));
            } else {
                holder.ivHead.setImageResource(R.drawable.stat_sys_download_anim0);
                holder.ivHead.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							MainActivity.instance.threadPool.execute(new DownloadImageRunnable(holder.ivHead, mfm.target_id, DownloadImageRunnable.BilibiliUser));
						}
					});
            }
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView ivHead;
        private TextView tvMedalName;
		private TextView tvMasterName;
		private TextView tvLevel;
		private TextView tvTodayLimit;
		private TextView tvLevelLimit;
    }
}
