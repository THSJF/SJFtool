package com.meng.sjftool;

import android.view.*;
import android.widget.*;
import com.meng.sjftool.libs.*;

public class DrawerAdapter extends BaseExpandableListAdapter {

	private String[] title = {
		"bilibili","barcode","app"
	};

	private String[][] items = {
		{ "输入ID", "动态","头衔","管理账号","AVBV转换" },
		{ "qr" },
		{ "设置", "退出" }
	};

	public DrawerAdapter() {

	}

	//得到子item需要关联的数据
	@Override
	public String getChild(int groupPosition, int childPosition) {
		return items[groupPosition][childPosition];
	}

	//得到子item的ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return items[groupPosition][childPosition].hashCode() & 0xFFFFFFFFL;
	}

	//设置子item的组件
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = MainActivity.instance.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			convertView.setPadding(AndroidContent.sp2px(55), 0, 0, 0);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();	
		}
		holder.tvName.setText(items[groupPosition][childPosition]);
		return convertView;
	}

	//获取当前父item下的子item的个数
	@Override
	public int getChildrenCount(int groupPosition) {
		return items[groupPosition].length;
	}

	//获取当前父item的数据
	@Override
	public String[] getGroup(int groupPosition) {
		return items[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return items.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return items[groupPosition].hashCode() & 0xFFFFFFFFL;
	}
	//设置父item组件
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = MainActivity.instance.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			convertView.setPadding(AndroidContent.sp2px(35), 0, 0, 0);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();	
		}  
		holder.tvName.setText(title[groupPosition]);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

    private class ViewHolder {
        private TextView tvName;
    }
}
