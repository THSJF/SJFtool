package com.meng.sjftool.customView;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import com.meng.sjftool.*;

public class DrawerArrowDrawable extends Drawable {
    private static final float ARROW_HEAD_ANGLE = (float) Math.toRadians(45.0D);
    protected float mBarGap;
    protected float mBarSize;
    protected float mBarThickness;
    protected float mMiddleArrowSize;
    protected final Paint mPaint = new Paint();
    protected final Path mPath = new Path();
    protected float mProgress;
    protected int mSize;
    protected float mVerticalMirror = 1f;
    protected float mTopBottomArrowSize;
    protected Context context;

    public DrawerArrowDrawable(Context context) {
        this.context = context;
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources().getColor(R.color.ldrawer_color));
		mSize = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_drawableSize);
		mBarSize = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_barSize);
		mTopBottomArrowSize = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_topBottomBarArrowSize);
		mBarThickness = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_thickness);
		mBarGap = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_gapBetweenBars);
        mMiddleArrowSize = context.getResources().getDimensionPixelSize(R.dimen.ldrawer_middleBarArrowSize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStrokeWidth(this.mBarThickness);
	}

    protected float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
        return paramFloat1 + paramFloat3 * (paramFloat2 - paramFloat1);
	}

    public void draw(Canvas canvas) {
        Rect localRect = getBounds();
        float f1 = lerp(mBarSize, mTopBottomArrowSize, mProgress);
        float f2 = lerp(mBarSize, mMiddleArrowSize, mProgress);
        float f3 = lerp(0.0F, mBarThickness / 2.0F, mProgress);
        float f4 = lerp(0.0F, ARROW_HEAD_ANGLE, mProgress);
        float f5 = 0.0F;
        float f6 = 180.0F;
        float f7 = lerp(f5, f6, mProgress);
        float f8 = lerp(mBarGap + mBarThickness, 0.0F, mProgress);
        mPath.rewind();
        float f9 = -f2 / 2.0F;
        mPath.moveTo(f9 + f3, 0.0F);
        mPath.rLineTo(f2 - f3, 0.0F);
        float f10 = Math.round(f1 * Math.cos(f4));
        float f11 = Math.round(f1 * Math.sin(f4));
        mPath.moveTo(f9, f8);
        mPath.rLineTo(f10, f11);
        mPath.moveTo(f9, -f8);
        mPath.rLineTo(f10, -f11);
        mPath.moveTo(0.0F, 0.0F);
        mPath.close();
        canvas.save();
		canvas.rotate(180.0F, localRect.centerX(), localRect.centerY());
        canvas.rotate(f7 * mVerticalMirror, localRect.centerX(), localRect.centerY());
        canvas.translate(localRect.centerX(), localRect.centerY());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
	}

    public int getIntrinsicHeight() {
        return mSize;
	}

    public int getIntrinsicWidth() {
        return mSize;
	}

    public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
	}

    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
	}

    public void setVerticalMirror(boolean mVerticalMirror) {
        this.mVerticalMirror = mVerticalMirror ? 1 : -1;
	}

    public void setProgress(float paramFloat) {
		mProgress = paramFloat;
        invalidateSelf();
	}

    public void setColorRes(int resourceId) {
        mPaint.setColor(context.getResources().getColor(resourceId));
	}

	public DrawerArrowDrawable setColorARGB(int ARGB) {
        mPaint.setColor(ARGB);
		return this;
	}

	public DrawerArrowDrawable setColorARGB(int A, int R, int G, int B) {
        mPaint.setColor((A << 24) | (R << 16) | (G << 8) | B);
		return this;
	}
}
