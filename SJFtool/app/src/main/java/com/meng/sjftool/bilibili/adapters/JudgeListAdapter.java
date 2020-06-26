package com.meng.sjftool.bilibili.adapters;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.google.gson.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.javabean.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.bilibili.result.*;
import com.meng.sjftool.libs.*;
import java.io.*;
import java.util.*;

public class JudgeListAdapter extends BaseExpandableListAdapter {

    private VideoReply videoReply;
	private boolean[] updated;

	public JudgeListAdapter(VideoReply reply) {
		videoReply = reply;
		updated = new boolean[reply.data.replies.size()];
	}

	public void setArticalJudge(VideoReply videoReply) {
		this.videoReply = videoReply;
	}

	//得到子item需要关联的数据
	@Override
	public VideoReply.Reply getChild(int groupPosition, int childPosition) {
		return videoReply.data.replies.get(groupPosition).replies.get(childPosition);
	}

	//得到子item的ID
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return videoReply.data.replies.get(groupPosition).replies.get(childPosition).hashCode() & 0xFFFFFFFFL;
	}

	//设置子item的组件
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final VideoReply.Reply parentReply = videoReply.data.replies.get(groupPosition).replies.get(childPosition);
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MainActivity.instance.getLayoutInflater().inflate(R.layout.video_judge, null);
			convertView.setPadding(Tools.AndroidContent.sp2px(25), 0, 0, 0);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.video_judgeTextView_uname);
			holder.tvTime = (TextView) convertView.findViewById(R.id.video_judgeTextView_time);
			holder.tvContent = (TextView) convertView.findViewById(R.id.video_judgeTextView_content);
			holder.ivHeader = (ImageView) convertView.findViewById(R.id.video_judgeImageView_header);
			holder.btnReply = (Button) convertView.findViewById(R.id.video_judgeButton_reply);
			holder.btnDelete = (Button) convertView.findViewById(R.id.video_judgeButton_delete);
			convertView.setTag(holder);
		} else {
			holder = (JudgeListAdapter.ViewHolder) convertView.getTag();	
		}
		File bHeader = new File(MainActivity.instance.mainDic + "bilibili/" + parentReply.mid + ".jpg");
        if (bHeader.exists()) {
            holder.ivHeader.setImageBitmap(BitmapFactory.decodeFile(bHeader.getAbsolutePath()));
        } else {
            if (MainActivity.onWifi) {
                MainActivity.instance.threadPool.execute(new DownloadImageRunnable(holder.ivHeader, parentReply.mid, DownloadImageRunnable.BilibiliUser));
            } else {
                holder.ivHeader.setImageResource(R.drawable.stat_sys_download_anim0);
                holder.ivHeader.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							MainActivity.instance.threadPool.execute(new DownloadImageRunnable(holder.ivHeader, parentReply.mid, DownloadImageRunnable.BilibiliUser));
							holder.ivHeader.setOnClickListener(null);
						}
					});
            }
        }
		holder.tvTime.setText(Tools.Time.getTime(parentReply.ctime * 1000));
		holder.tvName.setText(parentReply.member.uname);
		holder.tvContent.setText(parentReply.content.message);
		holder.canDelete = AccountManager.getAccount(Long.parseLong(parentReply.member.mid));
		if (holder.canDelete != null) {
			holder.btnDelete.setVisibility(View.VISIBLE);
			holder.btnDelete.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1) {
						new AlertDialog.Builder(MainActivity.instance)
							.setIcon(R.drawable.ic_launcher)
							.setTitle("确定删除吗")
							.setNegativeButton("取消", null)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									MainActivity.instance.threadPool.execute(new Runnable(){

											@Override
											public void run() {
												int result = new JsonParser().parse(Tools.BilibiliTool.deleteReply(parentReply.oid, parentReply.rpid, holder.canDelete.cookie)).getAsJsonObject().get("code").getAsInt();
												MainActivity.instance.showToast(String.valueOf(result));
												if (result != 0) {
													return;
												}
												videoReply.data.replies.get(groupPosition).replies.remove(childPosition);
												MainActivity.instance.runOnUiThread(new Runnable(){

														@Override
														public void run() {
															notifyDataSetChanged();
														}
													});
											}
										});
								}
							}).show();
					}
				});
		} else {
			holder.btnDelete.setVisibility(View.GONE);
		}
		holder.btnReply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					View editSerialView = MainActivity.instance.getLayoutInflater().inflate(R.layout.add_serial, null);
					final EditText et = (EditText) editSerialView.findViewById(R.id.add_serialEditText);
					final Spinner sp = (Spinner) editSerialView.findViewById(R.id.add_serialSpinner);
					sp.setAdapter(AccountManager.getInstance());
					new AlertDialog.Builder(MainActivity.instance).setView(editSerialView).setTitle("选择").setPositiveButton("发送",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								MainActivity.instance.threadPool.execute(new Runnable(){

										@Override
										public void run() {
											VideoReply.Reply rootReply = videoReply.data.replies.get(groupPosition);
											MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendVideoJudge("回复 @" + parentReply.member.uname + " :" + et.getText().toString(), rootReply.oid, rootReply.rpid, parentReply.rpid, ((AccountInfo)sp.getSelectedItem()).cookie)).getAsJsonObject().get("message").getAsString());
											videoReply = Tools.BilibiliTool.getVideoJudge(videoReply.data.replies.get(0).oid);
											MainActivity.instance.runOnUiThread(new Runnable(){

													@Override
													public void run() {
														notifyDataSetChanged();
													}
												});
										}
									});
							}
						}).setNegativeButton("取消", null).show();
				}
			});
		convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
		return convertView;
	}

	//获取当前父item下的子item的个数
	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<VideoReply.Reply> replies=videoReply.data.replies.get(groupPosition).replies;
		return replies == null ?0: replies.size();
	}

	//获取当前父item的数据
	@Override
	public VideoReply.Reply getGroup(int groupPosition) {
		return videoReply.data.replies.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return videoReply.data.replies.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return videoReply.data.replies.get(groupPosition).hashCode() & 0xFFFFFFFFL;
	}
	//设置父item组件
	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final VideoReply.Reply rootReply = videoReply.data.replies.get(groupPosition);
		final ViewHolder holder;
		if (convertView == null) {
			convertView = MainActivity.instance.getLayoutInflater().inflate(R.layout.video_judge, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.video_judgeTextView_uname);
			holder.tvTime = (TextView) convertView.findViewById(R.id.video_judgeTextView_time);
			holder.tvContent = (TextView) convertView.findViewById(R.id.video_judgeTextView_content);
			holder.ivHeader = (ImageView) convertView.findViewById(R.id.video_judgeImageView_header);
			holder.btnReply = (Button) convertView.findViewById(R.id.video_judgeButton_reply);
			holder.btnDelete = (Button) convertView.findViewById(R.id.video_judgeButton_delete);
			convertView.setTag(holder);
		} else {
			holder = (JudgeListAdapter.ViewHolder) convertView.getTag();	
		}
		File bHeader = new File(MainActivity.instance.mainDic + "bilibili/" + rootReply.mid + ".jpg");
        if (bHeader.exists()) {
            holder.ivHeader.setImageBitmap(BitmapFactory.decodeFile(bHeader.getAbsolutePath()));
        } else {
            if (MainActivity.onWifi) {
                MainActivity.instance.threadPool.execute(new DownloadImageRunnable(holder.ivHeader, rootReply.mid, DownloadImageRunnable.BilibiliUser));
            } else {
                holder.ivHeader.setImageResource(R.drawable.stat_sys_download_anim0);
                holder.ivHeader.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							MainActivity.instance.threadPool.execute(new DownloadImageRunnable(holder.ivHeader, rootReply.mid, DownloadImageRunnable.BilibiliUser));
							holder.ivHeader.setOnClickListener(null);
						}
					});
            }
        }
		holder.tvTime.setText(Tools.Time.getTime(rootReply.ctime * 1000));
		holder.tvName.setText(rootReply.member.uname);
		holder.tvContent.setText(rootReply.content.message);
		holder.canDelete = AccountManager.getAccount(Long.parseLong(rootReply.member.mid));
		if (holder.canDelete != null) {
			holder.btnDelete.setVisibility(View.VISIBLE);
			holder.btnDelete.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1) {
						new AlertDialog.Builder(MainActivity.instance).setIcon(R.drawable.ic_launcher).setTitle("确定删除吗").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									MainActivity.instance.threadPool.execute(new Runnable(){

											@Override
											public void run() {
												int result = new JsonParser().parse(Tools.BilibiliTool.deleteReply(rootReply.oid, rootReply.rpid, holder.canDelete.cookie)).getAsJsonObject().get("code").getAsInt();
												MainActivity.instance.showToast(String.valueOf(result));
												if (result != 0) {
													return;
												}
												videoReply.data.replies.remove(groupPosition);
												MainActivity.instance.runOnUiThread(new Runnable(){

														@Override
														public void run() {
															notifyDataSetChanged();
														}
													});
											}
										});
								}
							}).show();
					}
				});
		} else {
			holder.btnDelete.setVisibility(View.GONE);
		}
		ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);//设置宽度和高度
		convertView.setLayoutParams(params);
		holder.btnReply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					View editSerialView = MainActivity.instance.getLayoutInflater().inflate(R.layout.add_serial, null);
					final EditText et = (EditText) editSerialView.findViewById(R.id.add_serialEditText);
					final Spinner sp = (Spinner) editSerialView.findViewById(R.id.add_serialSpinner);
					sp.setAdapter(AccountManager.getInstance());
					new AlertDialog.Builder(MainActivity.instance).setView(editSerialView).setTitle("选择").setPositiveButton("发送",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								MainActivity.instance.threadPool.execute(new Runnable(){

										@Override
										public void run() {
											MainActivity.instance.showToast(new JsonParser().parse(Tools.BilibiliTool.sendVideoJudge(et.getText().toString(), rootReply.oid, rootReply.rpid, rootReply.rpid, ((AccountInfo)sp.getSelectedItem()).cookie)).getAsJsonObject().get("message").getAsString());
											videoReply = Tools.BilibiliTool.getVideoJudge(videoReply.data.replies.get(0).oid);
											MainActivity.instance.runOnUiThread(new Runnable(){

													@Override
													public void run() {
														notifyDataSetChanged();
													}
												});
										}
									});
							}
						}).setNeutralButton("取消", null).show();	
				}
			});
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
	public void onGroupExpanded(final int groupPosition) {
		final VideoReply.Reply root = videoReply.data.replies.get(groupPosition);
		if (root.replies == null || updated[groupPosition]) {
			return;
		}
		MainActivity.instance.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					VideoReply aj = Tools.BilibiliTool.getVideoJudge(root.oid, root.rpid);
					root.replies.clear();
					root.replies.addAll(aj.data.replies);
					MainActivity.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								notifyDataSetChanged();
								updated[groupPosition] = true;
							}
						});
				}
			});
		super.onGroupExpanded(groupPosition);
	}

    private class ViewHolder {
		private AccountInfo canDelete;
        private ImageView ivHeader;
        private TextView tvName;
		private TextView tvTime;
		private TextView tvContent;
        private Button btnReply;
		private Button btnDelete;
    }
}
