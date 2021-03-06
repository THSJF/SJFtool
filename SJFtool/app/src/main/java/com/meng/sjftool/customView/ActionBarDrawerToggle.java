package com.meng.sjftool.customView;

import android.app.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.view.*;
import android.widget.*;
import com.meng.sjftool.*;
import java.lang.reflect.*;

public class ActionBarDrawerToggle extends android.support.v4.app.ActionBarDrawerToggle {

    protected Activity mActivity;
    protected DrawerLayout mDrawerLayout;

    protected int mOpenDrawerContentDescRes;
    protected int mCloseDrawerContentDescRes;
    protected DrawerArrowDrawable mDrawerImage;
    protected boolean animateEnabled;

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
	}

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, DrawerArrowDrawable drawerImage, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, R.drawable.ic_drawer, openDrawerContentDescRes, closeDrawerContentDescRes);
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mOpenDrawerContentDescRes = openDrawerContentDescRes;
        mCloseDrawerContentDescRes = closeDrawerContentDescRes;
        mDrawerImage = drawerImage;
        animateEnabled = true;
	}

    public void syncState() {
        if (mDrawerImage == null) {
            super.syncState();
            return;
		}
        if (animateEnabled) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerImage.setProgress(1.f);
			} else {
                mDrawerImage.setProgress(0.f);
			}
		}
        setActionBarUpIndicator();
        setActionBarDescription();
	}

    public void setDrawerIndicatorEnabled(boolean enable) {
        if (mDrawerImage == null) {
            super.setDrawerIndicatorEnabled(enable);
            return;
		}
        setActionBarUpIndicator();
        setActionBarDescription();
	}

    public boolean isDrawerIndicatorEnabled() {
        if (mDrawerImage == null) {
            return super.isDrawerIndicatorEnabled();
		}
        return true;
	}

    public void onConfigurationChanged(Configuration newConfig) {
        if (mDrawerImage == null) {
            super.onConfigurationChanged(newConfig);
            return;
		}
        syncState();
	}

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
	}

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (mDrawerImage == null) {
            super.onDrawerSlide(drawerView, slideOffset);
            return;
		}
        if (animateEnabled) {
            mDrawerImage.setVerticalMirror(!mDrawerLayout.isDrawerOpen(GravityCompat.START));
            mDrawerImage.setProgress(slideOffset);
		}
	}

    @Override
    public void onDrawerOpened(View drawerView) {
        if (mDrawerImage == null) {
            super.onDrawerOpened(drawerView);
            return;
		}
        if (animateEnabled) {
            mDrawerImage.setProgress(1.f);
		}
        setActionBarDescription();
		mActivity.invalidateOptionsMenu();
	}

    @Override
    public void onDrawerClosed(View drawerView) {
        if (mDrawerImage == null) {
            super.onDrawerClosed(drawerView);
            return;
		}
        if (animateEnabled) {
            mDrawerImage.setProgress(0.f);
		}
        setActionBarDescription();
		mActivity.invalidateOptionsMenu();
	}

    protected void setActionBarUpIndicator() {
        if (mActivity != null) {
            try {
                Method setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator",
																				Drawable.class);
                setHomeAsUpIndicator.invoke(mActivity.getActionBar(), mDrawerImage);
                return;
			} catch (Exception e) {
			}

            final View home = mActivity.findViewById(android.R.id.home);
            if (home == null) {
                return;
			}

            final ViewGroup parent = (ViewGroup) home.getParent();
            final int childCount = parent.getChildCount();
            if (childCount != 2) {
                return;
			}

            final View first = parent.getChildAt(0);
            final View second = parent.getChildAt(1);
            final View up = first.getId() == android.R.id.home ? second : first;

            if (up instanceof ImageView) {
                ImageView upV = (ImageView) up;
                upV.setImageDrawable(mDrawerImage);
			}
		}
	}

    protected void setActionBarDescription() {
        if (mActivity != null && mActivity.getActionBar() != null) {
            try {
                Method setHomeActionContentDescription = ActionBar.class.getDeclaredMethod(
					"setHomeActionContentDescription", Integer.TYPE);
                setHomeActionContentDescription.invoke(mActivity.getActionBar(),
													   mDrawerLayout.isDrawerOpen(GravityCompat.START) ? mOpenDrawerContentDescRes : mCloseDrawerContentDescRes);
                if (Build.VERSION.SDK_INT <= 19) {
                    mActivity.getActionBar().setSubtitle(mActivity.getActionBar().getSubtitle());
				}
			} catch (Exception e) {
			}
		}
	}

    public void setAnimateEnabled(boolean enabled) {
        animateEnabled = enabled;
	}

    public boolean isAnimateEnabled() {
        return animateEnabled;
	}

}
