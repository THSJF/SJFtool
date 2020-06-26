package com.meng.sjftool.bilibili.fragment;

import android.app.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import com.meng.sjftool.*;
import com.meng.sjftool.bilibili.lib.*;
import java.util.regex.*;

public class AvBvConvertFragment extends Fragment {

	private TextView tvAv,tvBv;
	private EditText etAv,etBv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.av_bv, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		tvAv = (TextView) view.findViewById(R.id.av_bvTextViewav);
		tvBv = (TextView) view.findViewById(R.id.av_bvTextViewbv);
		etAv = (EditText) view.findViewById(R.id.av_bvEditTextav);
		etBv = (EditText) view.findViewById(R.id.av_bvEditTextbv);
		etAv.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					// TODO: Implement this method
				}

				@Override
				public void afterTextChanged(Editable p1) {
					String toString = p1.toString().toLowerCase();
					long avId=getAVId(toString.startsWith("av") ?toString: "av" + toString);
					if (avId != -1) {
						tvBv.setText(AvBvConverter.getInstance().encode(avId));
					} else {
						tvBv.setText("");
					}
				}
			});
		etBv.addTextChangedListener(new TextWatcher(){

				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
					// TODO: Implement this method
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					// TODO: Implement this method
				}

				@Override
				public void afterTextChanged(Editable p1) {
					String bvId=getBVId(p1.toString());
					if (bvId != null) {
						tvAv.setText("av" + AvBvConverter.getInstance().decode(bvId));
					} else {
						tvAv.setText("");
					}
				}
			});
    }

	private long getAVId(String s) {  
		Matcher matcher = Pattern.compile(MainActivity.regAv).matcher(s);  
		if (matcher.find()) {  
			return Long.parseLong(matcher.group(1));
		}
		return -1;
	}

	private String getBVId(String s) {  
		Matcher matcher = Pattern.compile(MainActivity.regBv).matcher(s);  
		if (matcher.find()) {  
			return matcher.group(1);
		}
		return null;
	}
}
