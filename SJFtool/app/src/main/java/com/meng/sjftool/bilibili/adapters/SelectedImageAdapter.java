package com.meng.sjftool.bilibili.adapters;

import android.graphics.*;
import android.media.*;
import android.view.*;
import android.widget.*;
import com.meng.sjftool.*;
import java.io.*;
import java.util.*;

public class SelectedImageAdapter extends BaseAdapter {
    private ArrayList<File> files = new ArrayList<>();

    public SelectedImageAdapter() {

	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public void addFile(File f) {
		files.add(f);
		notifyDataSetChanged();
	}

	public void removeFile(int index) {
		files.remove(index);
		notifyDataSetChanged();
	}
	
    public int getCount() {
        return files.size();
    }

    public Object getItem(int position) {
        return files.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = MainActivity.instance.getLayoutInflater().inflate(R.layout.account_adapter, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.account_adapterTextView_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.account_adapterImageView_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File file = files.get(position);
		holder.text.setText(file.getName());
		holder.icon.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getAbsolutePath()), 48, 48));
        return convertView;
    }

    private class ViewHolder {
        TextView text;
        ImageView icon;
    }
}

