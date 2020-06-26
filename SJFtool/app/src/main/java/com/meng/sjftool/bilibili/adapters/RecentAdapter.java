package com.meng.sjftool.bilibili.adapters;

import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.google.gson.reflect.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.enums.*;
import com.meng.sjftool.bilibili.fragment.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.util.*;

public class RecentAdapter extends BaseAdapter {

    private ArrayList<Recent> recents = new ArrayList<>();

	private String jsonPath;

	public RecentAdapter() {
		jsonPath = MainActivity.instance.getFilesDir() + "/recent.json";
		File f = new File(jsonPath);
        if (!f.exists()) {
            saveConfig();
		}
		recents = GSON.fromJson(FileTool.readString(jsonPath), new TypeToken<ArrayList<Recent>>(){}.getType());
		if (recents == null) {
			recents = new ArrayList<>();
		}
	}

	public void add(IDType type, long id, String name) {
		for (int i=0;i < recents.size();++i) {
			Recent r=recents.get(i);
			if (r.id == id && r.type == type) {
				return;
			}
		}
		recents.add(0, new Recent(type, id, name));
		notifyDataSetChanged();
		saveConfig();
	}

	public void rename(String origin, String newName) {
		for (int i=0;i < recents.size();++i) {
			Recent r=recents.get(i);
			if (r.name.equals(origin)) {
				recents.remove(i);
				r.name = newName;
				recents.add(0, r);
				break;
			}
		}
		notifyDataSetChanged();
		saveConfig();
	}

	public void toFirst(String name) {
		for (int i=0;i < recents.size();++i) {
			if (recents.get(i).name.equals(name)) {
				recents.add(0, recents.remove(i));
				break;
			}
		}
		notifyDataSetChanged();
		saveConfig();
	}

	public void remove(String id) {
		for (int i=0;i < recents.size();++i) {
			if (recents.get(i).name.equals(id)) {
				recents.remove(i);
				break;
			}
		}
		notifyDataSetChanged();
		saveConfig();
	}

    public int getCount() {
        return recents.size();
    }

    public Object getItem(int position) {
        return recents.get(position);
    }

    public long getItemId(int position) {
        return recents.get(position).hashCode();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = MainActivity.instance.getLayoutInflater().inflate(R.layout.recent_list_item, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.recent_list_itemTextView);
            holder.close = (ImageButton) convertView.findViewById(R.id.recent_list_itemImageButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String s = recents.get(position).name;
        holder.tvName.setText(s);
		holder.tvName.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					Recent r = recents.get(position);
					toFirst(r.name);		
					switch (r.type) {
						case Video:
							MainActivity.instance.showFragment(AvFragment.class, r.type, r.id);
							break;
						case Article:
							MainActivity.instance.showFragment(CvFragment.class, r.type, r.id);
							break;
						case Live:
							MainActivity.instance.showFragment(LiveFragment.class, r.type, r.id);
							break;
						case UID:
							MainActivity.instance.showFragment(UidFragment.class, r.type, r.id);
							break;
						case Medal:
							MainActivity.instance.showFragment(MedalFragment.class, r.type, r.id);
							break;
						case AVBV:
							MainActivity.instance.showFragment(AvBvConvertFragment.class, IDType.AVBV);
							break;
						case Accounts:
							MainActivity.instance.showFragment(ManagerFragment.class, IDType.Accounts);
							break;
						case Settings:
							MainActivity.instance.showFragment(SettingsFragment.class, IDType.Settings);
							break;
						case Dynamic:
							MainActivity.instance.showFragment(DynamicFragment.class, IDType.Dynamic);
					}
				}
			});
		holder.close.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					MainActivity.instance.removeFragment(s);
					if (recents.size() == 0) {
						return;
					}
					Recent r = recents.get(0);		
					switch (r.type) {
						case Video:
							MainActivity.instance.showFragment(AvFragment.class, r.type, r.id);
							break;
						case Article:
							MainActivity.instance.showFragment(CvFragment.class, r.type, r.id);
							break;
						case Live:
							MainActivity.instance.showFragment(LiveFragment.class, r.type, r.id);
							break;
						case UID:
							MainActivity.instance.showFragment(UidFragment.class, r.type, r.id);
							break;
						case Medal:
							MainActivity.instance.showFragment(MedalFragment.class, r.type, r.id);
							break;
						case AVBV:
							MainActivity.instance.showFragment(AvBvConvertFragment.class,IDType.AVBV);
							break;
						case Accounts:
							MainActivity.instance.showFragment(ManagerFragment.class, IDType.Accounts);
							break;
						case Settings:
							MainActivity.instance.showFragment(SettingsFragment.class, IDType.Settings);
							break;
						case Dynamic:
							MainActivity.instance.showFragment(DynamicFragment.class, IDType.Dynamic);
					}
				}
			});
        return convertView;
    }

    private class ViewHolder {
        private TextView tvName;
        private ImageButton close;
    }

	private void saveConfig() {
        try {
			FileOutputStream fos = new FileOutputStream(new File(jsonPath));
			OutputStreamWriter writer = new OutputStreamWriter(fos, "utf-8");
            writer.write(GSON.toJson(recents));
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
