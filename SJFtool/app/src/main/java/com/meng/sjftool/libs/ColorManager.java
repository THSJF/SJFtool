package com.meng.sjftool.libs;

import android.app.*;
import android.content.res.*;
import android.graphics.*;
import com.meng.sjftool.*;

public class ColorManager {
	private final int colorBackground;
	private final int colorText;
	private final int colorDrawerHeader;
	private int themeId;
	//private HashMap<View,ColorType> views = new HashMap<>();

	public ColorManager(int themeId) {
		this.themeId = themeId;
		switch (themeId) {
			case R.style.AppThemeLight:
			case R.style.AppThemeHoloL:
				colorBackground = 0xffeeeeee;
				colorDrawerHeader = 0xff009688;
				break;
			case R.style.AppThemeDark:
			case R.style.AppThemeHoloWallpaper:
				colorBackground = 0x2feeeeee;
				colorDrawerHeader = 0x7f009688;
				break;
			case R.style.AppThemeHolo:
				colorBackground = 0xff111111;
				colorDrawerHeader = 0xff009688;
				break;
			default:
				throw new RuntimeException("unknown theme");
		}
		TypedArray array = MainActivity.instance.getTheme().obtainStyledAttributes(new int[] {android.R.attr.textColorPrimary});
		colorText = array.getColor(0, 0xFF00FF);
		array.recycle();
	}

	//public void addView(View v, ColorType ct) {
	//	views.put(v, ct);
	//}
	
	public int getColorDrawerHeader() {
		return colorDrawerHeader;
	}

	public int getColorText() {
		return colorText;
	}

	public int getColorBackground() {
		return colorBackground;
	}

	public void doRun(Activity a) {
		ActionBar ab=a.getActionBar();
		ab.setDisplayOptions(ab.getDisplayOptions() ^ ActionBar.DISPLAY_HOME_AS_UP);
		switch (themeId) {
            case R.style.AppThemeHoloWallpaper:
            case R.style.AppThemeHoloL:
				a.getWindow().setStatusBarColor(Color.TRANSPARENT);
				break;
			case R.style.AppThemeHolo:

				break;
			case R.style.AppThemeDark:

				break;
			case R.style.AppThemeLight:

				break;
		}
	}
}
