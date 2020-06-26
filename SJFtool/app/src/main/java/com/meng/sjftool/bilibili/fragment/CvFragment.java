package com.meng.sjftool.bilibili.fragment;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.github.clans.fab.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.enums.*;
import com.meng.sjftool.bilibili.lib.*;
import com.meng.sjftool.bilibili.result.*;

public class CvFragment extends BaseIdFragment implements View.OnClickListener,View.OnLongClickListener {

	private Button editPre,preset;
	private ImageButton send;
	private FloatingActionMenu menuGroup;
	private FloatingActionButton fabZan;
    private FloatingActionButton fabCoin1;
	private FloatingActionButton fabCoin2;
    private FloatingActionButton fabFavorite;
	private LinearLayout llInput;
	
	private EditText et;
	private TextView info;
	private Spinner selectAccount;
	private CvInfo cvInfo;
	private ImageView ivPreview;
	private Bitmap preview;

	public CvFragment(IDType type, long id) {
		super(type, id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.cv_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		send = (ImageButton) view.findViewById(R.id.cv_fragmentButton_send);
		//editPre = (Button) view.findViewById(R.id.live_fragmentButton_edit_pre);
		preset = (Button) view.findViewById(R.id.cv_fragmentButton_preset);
		menuGroup = (FloatingActionMenu) view.findViewById(R.id.cv_float_menu);
		fabZan = (FloatingActionButton) view.findViewById(R.id.cv_fragmentButton_zan);
		fabCoin1 = (FloatingActionButton) view.findViewById(R.id.cv_fragmentButton_coin1);
		fabCoin2 = (FloatingActionButton) view.findViewById(R.id.cv_fragmentButton_coin2);
		fabFavorite = (FloatingActionButton) view.findViewById(R.id.cv_fragmentButton_favorite);
		llInput = (LinearLayout) view.findViewById(R.id.cv_fragmentLinearLayout_input);
		llInput.setVisibility(View.GONE);
		llInput.setBackgroundColor(0xffffffff);
		fabFavorite.setEnabled(false);
		et = (EditText) view.findViewById(R.id.cv_fragmentEditText_msg);
		ivPreview = (ImageView) view.findViewById(R.id.cv_fragmentImageView);  
		info = (TextView) view.findViewById(R.id.cv_fragmentTextView_info);
		selectAccount = (Spinner) view.findViewById(R.id.cv_fragmentSpinner);
		preset.setOnClickListener(this);
		fabZan.setOnClickListener(this);
		fabCoin1.setOnClickListener(this);
		fabCoin2.setOnClickListener(this);
		fabFavorite.setOnClickListener(this);
		send.setOnClickListener(this);
		//editPre.setOnClickListener(this);
		selectAccount.setAdapter(spinnerAccountAdapter);
		ivPreview.setOnLongClickListener(this);
		final Animation animShow = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_slide_in_from_right);
		final Animation animHide = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_slide_out_to_right);
		menuGroup.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
				@Override
				public void onMenuToggle(boolean opened) {
					if (opened) {
						llInput.startAnimation(animShow);
						llInput.setVisibility(View.VISIBLE);
					} else {
						llInput.startAnimation(animHide);
						llInput.setVisibility(View.GONE);
					}
				}
			});
		
		MainActivity.instance.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					cvInfo = Bilibili.getCvInfo(id);	
					if (cvInfo.code != 0) {
						MainActivity.instance.showToast(cvInfo.message);
						return;
					}
					getActivity().runOnUiThread(new Runnable(){

							@Override
							public void run() {
								info.setText(cvInfo.toString());
								MainActivity.instance.renameFragment(type.toString() + id, cvInfo.data.title);
							}
						});
					byte[] img = NetworkCacher.getNetPicture(cvInfo.data.banner_url);
					if (img == null) {
						MainActivity.instance.showToast("封面图获取失败");
						return;
					}
					preview = BitmapFactory.decodeByteArray(img, 0, img.length);
					getActivity().runOnUiThread(new Runnable(){

							@Override
							public void run() {
								ivPreview.setImageBitmap(preview);
							}

						});
				}
			});
	}

	@Override
	public boolean onLongClick(View p1) {
		try {
			saveBitmap(type.toString() + id, preview);
			MainActivity.instance.showToast("图片已保存至" + MainActivity.instance.mainDic + type + id + ".png");
		} catch (Exception e) {}
		return true;
	}

	@Override
	public void onClick(final View p1) {
		switch (p1.getId()) {
			case R.id.cv_fragmentButton_preset:
				ListView naiSentenseListview = new ListView(getActivity());
				naiSentenseListview.setAdapter(sencencesAdapter);
				naiSentenseListview.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
							sendBili((String) selectAccount.getSelectedItem(), SendCvJudge, (String)p1.getAdapter().getItem(p3));
						}
					});
				new AlertDialog.Builder(getActivity()).setView(naiSentenseListview).setTitle("选择预设语句").setNegativeButton("返回", null).show();
				break;
			case R.id.cv_fragmentButton_send:
				sendBili((String) selectAccount.getSelectedItem(), SendCvJudge, et.getText().toString());
				break;
			case R.id.cv_fragmentButton_zan:
				sendBili((String) selectAccount.getSelectedItem(), LikeArtical, "");
				break;
			case R.id.cv_fragmentButton_coin1:
				sendBili((String) selectAccount.getSelectedItem(), CvCoin1, "");
				break;
			case R.id.cv_fragmentButton_coin2:
				sendBili((String) selectAccount.getSelectedItem(), CvCoin2, "");
				break;
			case R.id.cv_fragmentButton_favorite:
				sendBili((String) selectAccount.getSelectedItem(), Favorite, "");
				break;
		}
	}
}
