/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.view;

import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;
import android.view.*;
import java.lang.reflect.*;
import java.util.*;


public final class ViewCompat {
    private static final String TAG = "ViewCompat";
 	public static final int OVER_SCROLL_NEVER = 2;
    private static final long FAKE_FRAME_TIME = 10;
	public static final int LAYER_TYPE_NONE = 0;
    public static final int LAYER_TYPE_HARDWARE = 2;
	public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
	public static final int MEASURED_STATE_TOO_SMALL = 0x01000000;
    public static final int SCROLL_AXIS_NONE = 0;

    interface ViewCompatImpl {
        public boolean canScrollHorizontally(View v, int direction);
        public boolean canScrollVertically(View v, int direction);
        public int getOverScrollMode(View v);
        public void setOverScrollMode(View v, int mode);
		public boolean hasTransientState(View view);
        public void setHasTransientState(View view, boolean hasTransientState);
        public void postInvalidateOnAnimation(View view);
        public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom);
        public void postOnAnimation(View view, Runnable action);
        public void postOnAnimationDelayed(View view, Runnable action, long delayMillis);
        public float getAlpha(View view);
        public void setLayerType(View view, int layerType, Paint paint);
        public int getLayerType(View view);
        public int getLabelFor(View view);
        public void setLabelFor(View view, int id);
        public void setLayerPaint(View view, Paint paint);
        public int getLayoutDirection(View view);
        public void setLayoutDirection(View view, int layoutDirection);
        public ViewParent getParentForAccessibility(View view);
        public boolean isOpaque(View view);
        public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState);
        public int getMeasuredWidthAndState(View view);
        public int getMeasuredHeightAndState(View view);
        public int getMeasuredState(View view);
        public int getPaddingStart(View view);
        public int getPaddingEnd(View view);
        public void setPaddingRelative(View view, int start, int top, int end, int bottom);
        public void dispatchStartTemporaryDetach(View view);
        public void dispatchFinishTemporaryDetach(View view);
        public float getX(View view);
        public float getY(View view);
        public float getRotation(View view);
        public float getRotationX(View view);
        public float getRotationY(View view);
        public float getScaleX(View view);
        public float getScaleY(View view);
        public float getTranslationX(View view);
        public float getTranslationY(View view);
        public int getMinimumWidth(View view);
        public int getMinimumHeight(View view);
        public ViewPropertyAnimatorCompat animate(View view);
        public void setRotation(View view, float value);
        public void setRotationX(View view, float value);
        public void setRotationY(View view, float value);
        public void setScaleX(View view, float value);
        public void setScaleY(View view, float value);
        public void setTranslationX(View view, float value);
        public void setTranslationY(View view, float value);
        public void setX(View view, float value);
        public void setY(View view, float value);
        public void setAlpha(View view, float value);
        public void setPivotX(View view, float value);
        public void setPivotY(View view, float value);
        public float getPivotX(View view);
        public float getPivotY(View view);
        public void setElevation(View view, float elevation);
        public float getElevation(View view);
        public void setTranslationZ(View view, float translationZ);
        public float getTranslationZ(View view);
        public void setClipBounds(View view, Rect clipBounds);
        public Rect getClipBounds(View view);
        public void setTransitionName(View view, String transitionName);
        public String getTransitionName(View view);
        public int getWindowSystemUiVisibility(View view);
        public void requestApplyInsets(View view);
        public void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean enabled);
        public boolean getFitsSystemWindows(View view);
        public boolean hasOverlappingRendering(View view);
        void setFitsSystemWindows(View view, boolean fitSystemWindows);
        void jumpDrawablesToCurrentState(View v);
        void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener listener);
        WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets);
        WindowInsetsCompat dispatchApplyWindowInsets(View v, WindowInsetsCompat insets);
        void setSaveFromParentEnabled(View view, boolean enabled);
        void setActivated(View view, boolean activated);
        boolean isPaddingRelative(View view);
        ColorStateList getBackgroundTintList(View view);
        void setBackgroundTintList(View view, ColorStateList tintList);
        PorterDuff.Mode getBackgroundTintMode(View view);
        void setBackgroundTintMode(View view, PorterDuff.Mode mode);
        void setNestedScrollingEnabled(View view, boolean enabled);
        boolean isNestedScrollingEnabled(View view);
        boolean startNestedScroll(View view, int axes);
        void stopNestedScroll(View view);
        boolean hasNestedScrollingParent(View view);
        boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow);
        boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow);
        boolean dispatchNestedFling(View view, float velocityX, float velocityY, boolean consumed);
        boolean dispatchNestedPreFling(View view, float velocityX, float velocityY);
        boolean isLaidOut(View view);
        int combineMeasuredStates(int curState, int newState);
        public float getZ(View view);
        public boolean isAttachedToWindow(View view);
        public boolean hasOnClickListeners(View view);
        public void setScrollIndicators(View view, int indicators);
        public void setScrollIndicators(View view, int indicators, int mask);
        public int getScrollIndicators(View view);
        public void offsetTopAndBottom(View view, int offset);
        public void offsetLeftAndRight(View view, int offset);
    }

    static class BaseViewCompatImpl implements ViewCompatImpl {

		@Override
		public void setOverScrollMode(View v, int mode) {
		}

		@Override
		public void setHasTransientState(View view, boolean hasTransientState) {
		}

		@Override
		public void setLayerType(View view, int layerType, Paint paint) {
		}

		@Override
		public void setLabelFor(View view, int id) {
		}

		@Override
		public void setLayerPaint(View view, Paint paint) {
		}

        private Method mDispatchStartTemporaryDetach;
        private Method mDispatchFinishTemporaryDetach;
        private boolean mTempDetachBound;
        WeakHashMap<View, ViewPropertyAnimatorCompat> mViewPropertyAnimatorCompatMap = null;

        public boolean canScrollHorizontally(View v, int direction) {
            return (v instanceof ScrollingView) && canScrollingViewScrollHorizontally((ScrollingView) v, direction);
        }
        public boolean canScrollVertically(View v, int direction) {
            return (v instanceof ScrollingView) && canScrollingViewScrollVertically((ScrollingView) v, direction);
        }
        public int getOverScrollMode(View v) {
            return OVER_SCROLL_NEVER;
        }
        public boolean hasTransientState(View view) {
            return false;
        }
        public void postInvalidateOnAnimation(View view) {
            view.invalidate();
        }
        public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
            view.invalidate(left, top, right, bottom);
        }
        public void postOnAnimation(View view, Runnable action) {
            view.postDelayed(action, getFrameTime());
        }
        public void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
            view.postDelayed(action, getFrameTime() + delayMillis);
        }
        long getFrameTime() {
            return FAKE_FRAME_TIME;
        }
        public float getAlpha(View view) {
            return 1.0f;
        }
		public int getLayerType(View view) {
            return LAYER_TYPE_NONE;
        }
        public int getLabelFor(View view) {
            return 0;
        }
		@Override
        public int getLayoutDirection(View view) {
            return LAYOUT_DIRECTION_LTR;
        }
        @Override
        public void setLayoutDirection(View view, int layoutDirection) {

        }
        @Override
        public ViewParent getParentForAccessibility(View view) {
            return view.getParent();
        }
        @Override
        public boolean isOpaque(View view) {
            final Drawable bg = view.getBackground();
            if (bg != null) {
                return bg.getOpacity() == PixelFormat.OPAQUE;
            }
            return false;
        }
        public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
            return View.resolveSize(size, measureSpec);
        }
        @Override
        public int getMeasuredWidthAndState(View view) {
            return view.getMeasuredWidth();
        }
        @Override
        public int getMeasuredHeightAndState(View view) {
            return view.getMeasuredHeight();
        }
        @Override
        public int getMeasuredState(View view) {
            return 0;
        }
        @Override
        public int getPaddingStart(View view) {
            return view.getPaddingLeft();
        }
        @Override
        public int getPaddingEnd(View view) {
            return view.getPaddingRight();
        }
        @Override
        public void setPaddingRelative(View view, int start, int top, int end, int bottom) {
            view.setPadding(start, top, end, bottom);
        }
        @Override
        public void dispatchStartTemporaryDetach(View view) {
            if (!mTempDetachBound) {
                bindTempDetach();
            }
            if (mDispatchStartTemporaryDetach != null) {
                try {
                    mDispatchStartTemporaryDetach.invoke(view);
                } catch (Exception e) {
                    Log.d(TAG, "Error calling dispatchStartTemporaryDetach", e);
                }
            } else {
                view.onStartTemporaryDetach();
            }
        }
        @Override
        public void dispatchFinishTemporaryDetach(View view) {
            if (!mTempDetachBound) {
                bindTempDetach();
            }
            if (mDispatchFinishTemporaryDetach != null) {
                try {
                    mDispatchFinishTemporaryDetach.invoke(view);
                } catch (Exception e) {
                    Log.d(TAG, "Error calling dispatchFinishTemporaryDetach", e);
                }
            } else {
                view.onFinishTemporaryDetach();
            }
        }
        @Override
        public boolean hasOverlappingRendering(View view) {
            return true;
        }
        private void bindTempDetach() {
            try {
                mDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach");
                mDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach");
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Couldn't find method", e);
            }
            mTempDetachBound = true;
        }
        @Override
        public float getTranslationX(View view) {
            return 0;
        }
        @Override
        public float getTranslationY(View view) {
            return 0;
        }
        @Override
        public float getX(View view) {
            return 0;
        }
        @Override
        public float getY(View view) {
            return 0;
        }
        @Override
        public float getRotation(View view) {
            return 0;
        }
        @Override
        public float getRotationX(View view) {
            return 0;
        }
        @Override
        public float getRotationY(View view) {
            return 0;
        }
        @Override
        public float getScaleX(View view) {
            return 0;
        }
        @Override
        public float getScaleY(View view) {
            return 0;
        }
        @Override
        public int getMinimumWidth(View view) {
            return ViewCompatBase.getMinimumWidth(view);
        }
        @Override
        public int getMinimumHeight(View view) {
            return ViewCompatBase.getMinimumHeight(view);
        }
        @Override
        public ViewPropertyAnimatorCompat animate(View view) {
            return new ViewPropertyAnimatorCompat(view);
        }
        @Override
        public void setRotation(View view, float value) { }
        @Override
        public void setTranslationX(View view, float value) { }
        @Override
        public void setTranslationY(View view, float value) { }
        @Override
        public void setAlpha(View view, float value) { }
        @Override
        public void setRotationX(View view, float value) { }
        @Override
        public void setRotationY(View view, float value) { }
        @Override
        public void setScaleX(View view, float value) { }
        @Override
        public void setScaleY(View view, float value) { }
        @Override
        public void setX(View view, float value) { }
        @Override
        public void setY(View view, float value) { }
        @Override
        public void setPivotX(View view, float value) { }
        @Override
        public void setPivotY(View view, float value) { }
        @Override
        public float getPivotX(View view) {
            return 0;
        }
        @Override
        public float getPivotY(View view) {
            return 0;
        }
        @Override
        public void setTransitionName(View view, String transitionName) { }
        @Override
        public String getTransitionName(View view) {
            return null;
        }
        @Override
        public int getWindowSystemUiVisibility(View view) {
            return 0;
        }
        @Override
        public void requestApplyInsets(View view) { }
        @Override
        public void setElevation(View view, float elevation) { }
        @Override
        public float getElevation(View view) {
            return 0f;
        }
        @Override
        public void setTranslationZ(View view, float translationZ) { }
        @Override
        public float getTranslationZ(View view) {
            return 0f;
        }
        @Override
        public void setClipBounds(View view, Rect clipBounds) { }
        @Override
        public Rect getClipBounds(View view) {
            return null;
        }
        @Override
        public void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean enabled) { }
        @Override
        public boolean getFitsSystemWindows(View view) {
            return false;
        }
        @Override
        public void setFitsSystemWindows(View view, boolean fitSystemWindows) { }
        @Override
        public void jumpDrawablesToCurrentState(View view) {
            // Do nothing; API didn't exist.
        }
        @Override
        public void setOnApplyWindowInsetsListener(View view,
												   OnApplyWindowInsetsListener listener) { }
        @Override
        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return insets;
        }
        @Override
        public WindowInsetsCompat dispatchApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return insets;
        }
        @Override
        public void setSaveFromParentEnabled(View v, boolean enabled) { }
        @Override
        public void setActivated(View view, boolean activated) { }
        @Override
        public boolean isPaddingRelative(View view) {
            return false;
        }

        public void setNestedScrollingEnabled(View view, boolean enabled) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild) view).setNestedScrollingEnabled(enabled);
            }
        }
        @Override
        public boolean isNestedScrollingEnabled(View view) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).isNestedScrollingEnabled();
            }
            return false;
        }
        @Override
        public ColorStateList getBackgroundTintList(View view) {
            return ViewCompatBase.getBackgroundTintList(view);
        }
        @Override
        public void setBackgroundTintList(View view, ColorStateList tintList) {
            ViewCompatBase.setBackgroundTintList(view, tintList);
        }
        @Override
        public void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
            ViewCompatBase.setBackgroundTintMode(view, mode);
        }
        @Override
        public PorterDuff.Mode getBackgroundTintMode(View view) {
            return ViewCompatBase.getBackgroundTintMode(view);
        }

        private boolean canScrollingViewScrollHorizontally(ScrollingView view, int direction) {
            final int offset = view.computeHorizontalScrollOffset();
            final int range = view.computeHorizontalScrollRange() -
				view.computeHorizontalScrollExtent();
            if (range == 0) return false;
            if (direction < 0) {
                return offset > 0;
            } else {
                return offset < range - 1;
            }
        }

        private boolean canScrollingViewScrollVertically(ScrollingView view, int direction) {
            final int offset = view.computeVerticalScrollOffset();
            final int range = view.computeVerticalScrollRange() -
				view.computeVerticalScrollExtent();
            if (range == 0) return false;
            if (direction < 0) {
                return offset > 0;
            } else {
                return offset < range - 1;
            }
        }

        public boolean startNestedScroll(View view, int axes) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).startNestedScroll(axes);
            }
            return false;
        }
        @Override
        public void stopNestedScroll(View view) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild) view).stopNestedScroll();
            }
        }
        @Override
        public boolean hasNestedScrollingParent(View view) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).hasNestedScrollingParent();
            }
            return false;
        }
        @Override
        public boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed,
											int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).dispatchNestedScroll(dxConsumed, dyConsumed,
																		  dxUnconsumed, dyUnconsumed, offsetInWindow);
            }
            return false;
        }
        @Override
        public boolean dispatchNestedPreScroll(View view, int dx, int dy,
											   int[] consumed, int[] offsetInWindow) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).dispatchNestedPreScroll(dx, dy, consumed,
																			 offsetInWindow);
            }
            return false;
        }
        @Override
        public boolean dispatchNestedFling(View view, float velocityX, float velocityY,
										   boolean consumed) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).dispatchNestedFling(velocityX, velocityY,
																		 consumed);
            }
            return false;
        }
        @Override
        public boolean dispatchNestedPreFling(View view, float velocityX, float velocityY) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).dispatchNestedPreFling(velocityX, velocityY);
            }
            return false;
        }
        @Override
        public boolean isLaidOut(View view) {
            return ViewCompatBase.isLaidOut(view);
        }
        @Override
        public int combineMeasuredStates(int curState, int newState) {
            return curState | newState;
        }
        @Override
        public float getZ(View view) {
            return getTranslationZ(view) + getElevation(view);
        }
        @Override
        public boolean isAttachedToWindow(View view) {
            return ViewCompatBase.isAttachedToWindow(view);
        }
        @Override
        public boolean hasOnClickListeners(View view) {
            return false;
        }
        @Override
        public int getScrollIndicators(View view) {
            return 0;
        }
        @Override
        public void setScrollIndicators(View view, int indicators) { }
        @Override
        public void setScrollIndicators(View view, int indicators, int mask) { }
        @Override
        public void offsetLeftAndRight(View view, int offset) {
            ViewCompatBase.offsetLeftAndRight(view, offset);
        }
        @Override
        public void offsetTopAndBottom(View view, int offset) {
            ViewCompatBase.offsetTopAndBottom(view, offset);
        }
    }

    static class EclairMr1ViewCompatImpl extends BaseViewCompatImpl {
        @Override
        public boolean isOpaque(View view) {
            return ViewCompatEclairMr1.isOpaque(view);
        }
        @Override
        public void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean enabled) {
            ViewCompatEclairMr1.setChildrenDrawingOrderEnabled(viewGroup, enabled);
        }
    }

    static class GBViewCompatImpl extends EclairMr1ViewCompatImpl {
        @Override
        public int getOverScrollMode(View v) {
            return ViewCompatGingerbread.getOverScrollMode(v);
        }
        @Override
        public void setOverScrollMode(View v, int mode) {
            ViewCompatGingerbread.setOverScrollMode(v, mode);
        }
    }

    static class HCViewCompatImpl extends GBViewCompatImpl {
        @Override
        long getFrameTime() {
            return ViewCompatHC.getFrameTime();
        }
        @Override
        public float getAlpha(View view) {
            return ViewCompatHC.getAlpha(view);
        }
        @Override
        public void setLayerType(View view, int layerType, Paint paint) {
            ViewCompatHC.setLayerType(view, layerType, paint);
        }
        @Override
        public int getLayerType(View view)  {
            return ViewCompatHC.getLayerType(view);
        }
        @Override
        public void setLayerPaint(View view, Paint paint) {
            // Make sure the paint is correct; this will be cheap if it's the same
            // instance as was used to call setLayerType earlier.
            setLayerType(view, getLayerType(view), paint);
            // This is expensive, but the only way to accomplish this before JB-MR1.
            view.invalidate();
        }
        @Override
        public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
            return ViewCompatHC.resolveSizeAndState(size, measureSpec, childMeasuredState);
        }
        @Override
        public int getMeasuredWidthAndState(View view) {
            return ViewCompatHC.getMeasuredWidthAndState(view);
        }
        @Override
        public int getMeasuredHeightAndState(View view) {
            return ViewCompatHC.getMeasuredHeightAndState(view);
        }
        @Override
        public int getMeasuredState(View view) {
            return ViewCompatHC.getMeasuredState(view);
        }
        @Override
        public float getTranslationX(View view) {
            return ViewCompatHC.getTranslationX(view);
        }
        @Override
        public float getTranslationY(View view) {
            return ViewCompatHC.getTranslationY(view);
        }
        @Override
        public void setTranslationX(View view, float value) {
            ViewCompatHC.setTranslationX(view, value);
        }
        @Override
        public void setTranslationY(View view, float value) {
            ViewCompatHC.setTranslationY(view, value);
        }
        @Override
        public void setAlpha(View view, float value) {
            ViewCompatHC.setAlpha(view, value);
        }
        @Override
        public void setX(View view, float value) {
            ViewCompatHC.setX(view, value);
        }
        @Override
        public void setY(View view, float value) {
            ViewCompatHC.setY(view, value);
        }
        @Override
        public void setRotation(View view, float value) {
            ViewCompatHC.setRotation(view, value);
        }
        @Override
        public void setRotationX(View view, float value) {
            ViewCompatHC.setRotationX(view, value);
        }
        @Override
        public void setRotationY(View view, float value) {
            ViewCompatHC.setRotationY(view, value);
        }
        @Override
        public void setScaleX(View view, float value) {
            ViewCompatHC.setScaleX(view, value);
        }
        @Override
        public void setScaleY(View view, float value) {
            ViewCompatHC.setScaleY(view, value);
        }
        @Override
        public void setPivotX(View view, float value) {
            ViewCompatHC.setPivotX(view, value);
        }
        @Override
        public void setPivotY(View view, float value) {
            ViewCompatHC.setPivotY(view, value);
        }
        @Override
        public float getX(View view) {
            return ViewCompatHC.getX(view);
        }
        @Override
        public float getY(View view) {
            return ViewCompatHC.getY(view);
        }
        @Override
        public float getRotation(View view) {
            return ViewCompatHC.getRotation(view);
        }
        @Override
        public float getRotationX(View view) {
            return ViewCompatHC.getRotationX(view);
        }
        @Override
        public float getRotationY(View view) {
            return ViewCompatHC.getRotationY(view);
        }
        @Override
        public float getScaleX(View view) {
            return ViewCompatHC.getScaleX(view);
        }
        @Override
        public float getScaleY(View view) {
            return ViewCompatHC.getScaleY(view);
        }
        @Override
        public float getPivotX(View view) {
            return ViewCompatHC.getPivotX(view);
        }
        @Override
        public float getPivotY(View view) {
            return ViewCompatHC.getPivotY(view);
        }
        @Override
        public void jumpDrawablesToCurrentState(View view) {
            ViewCompatHC.jumpDrawablesToCurrentState(view);
        }
        @Override
        public void setSaveFromParentEnabled(View view, boolean enabled) {
            ViewCompatHC.setSaveFromParentEnabled(view, enabled);
        }
        @Override
        public void setActivated(View view, boolean activated) {
            ViewCompatHC.setActivated(view, activated);
        }
        @Override
        public int combineMeasuredStates(int curState, int newState) {
            return ViewCompatHC.combineMeasuredStates(curState, newState);
        }
        @Override
        public void offsetLeftAndRight(View view, int offset) {
            ViewCompatHC.offsetLeftAndRight(view, offset);
        }
        @Override
        public void offsetTopAndBottom(View view, int offset) {
            ViewCompatHC.offsetTopAndBottom(view, offset);
        }
    }

    static class ICSViewCompatImpl extends HCViewCompatImpl {
        static Field mAccessibilityDelegateField;
        static boolean accessibilityDelegateCheckFailed = false;
        @Override
        public boolean canScrollHorizontally(View v, int direction) {
            return ViewCompatICS.canScrollHorizontally(v, direction);
        }
        @Override
        public boolean canScrollVertically(View v, int direction) {
            return ViewCompatICS.canScrollVertically(v, direction);
        }

        @Override
        public ViewPropertyAnimatorCompat animate(View view) {
            if (mViewPropertyAnimatorCompatMap == null) {
                mViewPropertyAnimatorCompatMap =
					new WeakHashMap<View, ViewPropertyAnimatorCompat>();
            }
            ViewPropertyAnimatorCompat vpa = mViewPropertyAnimatorCompatMap.get(view);
            if (vpa == null) {
                vpa = new ViewPropertyAnimatorCompat(view);
                mViewPropertyAnimatorCompatMap.put(view, vpa);
            }
            return vpa;
        }
        @Override
        public void setFitsSystemWindows(View view, boolean fitSystemWindows) {
            ViewCompatICS.setFitsSystemWindows(view, fitSystemWindows);
        }
    }

    static class ICSMr1ViewCompatImpl extends ICSViewCompatImpl {
        @Override
        public boolean hasOnClickListeners(View view) {
            return ViewCompatICSMr1.hasOnClickListeners(view);
        }
    }

    static class JBViewCompatImpl extends ICSMr1ViewCompatImpl {
        @Override
        public boolean hasTransientState(View view) {
            return ViewCompatJB.hasTransientState(view);
        }
        @Override
        public void setHasTransientState(View view, boolean hasTransientState) {
            ViewCompatJB.setHasTransientState(view, hasTransientState);
        }
        @Override
        public void postInvalidateOnAnimation(View view) {
            ViewCompatJB.postInvalidateOnAnimation(view);
        }
        @Override
        public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
            ViewCompatJB.postInvalidateOnAnimation(view, left, top, right, bottom);
        }
        @Override
        public void postOnAnimation(View view, Runnable action) {
            ViewCompatJB.postOnAnimation(view, action);
        }
        @Override
        public void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
            ViewCompatJB.postOnAnimationDelayed(view, action, delayMillis);
        }

		@Override
        public int getMinimumWidth(View view) {
            return ViewCompatJB.getMinimumWidth(view);
        }
        @Override
        public int getMinimumHeight(View view) {
            return ViewCompatJB.getMinimumHeight(view);
        }
        @Override
        public void requestApplyInsets(View view) {
            ViewCompatJB.requestApplyInsets(view);
        }
        @Override
        public boolean getFitsSystemWindows(View view) {
            return ViewCompatJB.getFitsSystemWindows(view);
        }
        @Override
        public boolean hasOverlappingRendering(View view) {
            return ViewCompatJB.hasOverlappingRendering(view);
        }
    }

    static class JbMr1ViewCompatImpl extends JBViewCompatImpl {

        @Override
        public int getLabelFor(View view) {
            return ViewCompatJellybeanMr1.getLabelFor(view);
        }
        @Override
        public void setLabelFor(View view, int id) {
            ViewCompatJellybeanMr1.setLabelFor(view, id);
        }
        @Override
        public void setLayerPaint(View view, Paint paint) {
            ViewCompatJellybeanMr1.setLayerPaint(view, paint);
        }
        @Override
        public int getLayoutDirection(View view) {
            return ViewCompatJellybeanMr1.getLayoutDirection(view);
        }
        @Override
        public void setLayoutDirection(View view, int layoutDirection) {
            ViewCompatJellybeanMr1.setLayoutDirection(view, layoutDirection);
        }
        @Override
        public int getPaddingStart(View view) {
            return ViewCompatJellybeanMr1.getPaddingStart(view);
        }
        @Override
        public int getPaddingEnd(View view) {
            return ViewCompatJellybeanMr1.getPaddingEnd(view);
        }
        @Override
        public void setPaddingRelative(View view, int start, int top, int end, int bottom) {
            ViewCompatJellybeanMr1.setPaddingRelative(view, start, top, end, bottom);
        }
        @Override
        public int getWindowSystemUiVisibility(View view) {
            return ViewCompatJellybeanMr1.getWindowSystemUiVisibility(view);
        }
        @Override
        public boolean isPaddingRelative(View view) {
            return ViewCompatJellybeanMr1.isPaddingRelative(view);
        }
    }

    static class JbMr2ViewCompatImpl extends JbMr1ViewCompatImpl {
        @Override
        public void setClipBounds(View view, Rect clipBounds) {
            ViewCompatJellybeanMr2.setClipBounds(view, clipBounds);
        }
        @Override
        public Rect getClipBounds(View view) {
            return ViewCompatJellybeanMr2.getClipBounds(view);
        }
    }

    static class KitKatViewCompatImpl extends JbMr2ViewCompatImpl {

        @Override
        public boolean isLaidOut(View view) {
            return ViewCompatKitKat.isLaidOut(view);
        }
        @Override
        public boolean isAttachedToWindow(View view) {
            return ViewCompatKitKat.isAttachedToWindow(view);
        }
    }

    static class LollipopViewCompatImpl extends KitKatViewCompatImpl {
        @Override
        public void setTransitionName(View view, String transitionName) {
            ViewCompatLollipop.setTransitionName(view, transitionName);
        }
        @Override
        public String getTransitionName(View view) {
            return ViewCompatLollipop.getTransitionName(view);
        }
        @Override
        public void requestApplyInsets(View view) {
            ViewCompatLollipop.requestApplyInsets(view);
        }
        @Override
        public void setElevation(View view, float elevation) {
            ViewCompatLollipop.setElevation(view, elevation);
        }
        @Override
        public float getElevation(View view) {
            return ViewCompatLollipop.getElevation(view);
        }
        @Override
        public void setTranslationZ(View view, float translationZ) {
            ViewCompatLollipop.setTranslationZ(view, translationZ);
        }
        @Override
        public float getTranslationZ(View view) {
            return ViewCompatLollipop.getTranslationZ(view);
        }
        @Override
        public void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener listener) {
            ViewCompatLollipop.setOnApplyWindowInsetsListener(view, listener);
        }
        @Override
        public void setNestedScrollingEnabled(View view, boolean enabled) {
            ViewCompatLollipop.setNestedScrollingEnabled(view, enabled);
        }
        @Override
        public boolean isNestedScrollingEnabled(View view) {
            return ViewCompatLollipop.isNestedScrollingEnabled(view);
        }
        @Override
        public boolean startNestedScroll(View view, int axes) {
            return ViewCompatLollipop.startNestedScroll(view, axes);
        }
        @Override
        public void stopNestedScroll(View view) {
            ViewCompatLollipop.stopNestedScroll(view);
        }
        @Override
        public boolean hasNestedScrollingParent(View view) {
            return ViewCompatLollipop.hasNestedScrollingParent(view);
        }
        @Override
        public boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            return ViewCompatLollipop.dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }
        @Override
        public boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
            return ViewCompatLollipop.dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
        }
        @Override
        public boolean dispatchNestedFling(View view, float velocityX, float velocityY, boolean consumed) {
            return ViewCompatLollipop.dispatchNestedFling(view, velocityX, velocityY, consumed);
        }
        @Override
        public boolean dispatchNestedPreFling(View view, float velocityX, float velocityY) {
            return ViewCompatLollipop.dispatchNestedPreFling(view, velocityX, velocityY);
        }
		@Override
        public ColorStateList getBackgroundTintList(View view) {
            return ViewCompatLollipop.getBackgroundTintList(view);
        }
        @Override
        public void setBackgroundTintList(View view, ColorStateList tintList) {
            ViewCompatLollipop.setBackgroundTintList(view, tintList);
        }
        @Override
        public void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
            ViewCompatLollipop.setBackgroundTintMode(view, mode);
        }
        @Override
        public PorterDuff.Mode getBackgroundTintMode(View view) {
            return ViewCompatLollipop.getBackgroundTintMode(view);
        }
        @Override
        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return ViewCompatLollipop.onApplyWindowInsets(v, insets);
        }
        @Override
        public WindowInsetsCompat dispatchApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return ViewCompatLollipop.dispatchApplyWindowInsets(v, insets);
        }
        @Override
        public float getZ(View view) {
            return ViewCompatLollipop.getZ(view);
        }
        @Override
        public void offsetLeftAndRight(View view, int offset) {
            ViewCompatLollipop.offsetLeftAndRight(view, offset);
        }
        @Override
        public void offsetTopAndBottom(View view, int offset) {
            ViewCompatLollipop.offsetTopAndBottom(view, offset);
        }
    }

    static class MarshmallowViewCompatImpl extends LollipopViewCompatImpl {
        @Override
        public void setScrollIndicators(View view, int indicators) {
            ViewCompatMarshmallow.setScrollIndicators(view, indicators);
        }
        @Override
        public void setScrollIndicators(View view, int indicators, int mask) {
            ViewCompatMarshmallow.setScrollIndicators(view, indicators, mask);
        }
        @Override
        public int getScrollIndicators(View view) {
            return ViewCompatMarshmallow.getScrollIndicators(view);
        }
        @Override
        public void offsetLeftAndRight(View view, int offset) {
            ViewCompatMarshmallow.offsetLeftAndRight(view, offset);
        }
        @Override
        public void offsetTopAndBottom(View view, int offset) {
            ViewCompatMarshmallow.offsetTopAndBottom(view, offset);
        }
    }

    static final ViewCompatImpl IMPL;
    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new MarshmallowViewCompatImpl();
        } else if (version >= 21) {
            IMPL = new LollipopViewCompatImpl();
        } else if (version >= 19) {
            IMPL = new KitKatViewCompatImpl();
        } else if (version >= 17) {
            IMPL = new JbMr1ViewCompatImpl();
        } else if (version >= 16) {
            IMPL = new JBViewCompatImpl();
        } else if (version >= 15) {
            IMPL = new ICSMr1ViewCompatImpl();
        } else if (version >= 14) {
            IMPL = new ICSViewCompatImpl();
        } else if (version >= 11) {
            IMPL = new HCViewCompatImpl();
        } else if (version >= 9) {
            IMPL = new GBViewCompatImpl();
        } else if (version >= 7) {
            IMPL = new EclairMr1ViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }
    public static boolean canScrollHorizontally(View v, int direction) {
        return IMPL.canScrollHorizontally(v, direction);
    }
    public static boolean canScrollVertically(View v, int direction) {
        return IMPL.canScrollVertically(v, direction);
    }
    public static int getOverScrollMode(View v) {
        return IMPL.getOverScrollMode(v);
    }
    public static void setOverScrollMode(View v, int overScrollMode) {
        IMPL.setOverScrollMode(v, overScrollMode);
    }
	public static boolean hasTransientState(View view) {
        return IMPL.hasTransientState(view);
    }
    public static void setHasTransientState(View view, boolean hasTransientState) {
        IMPL.setHasTransientState(view, hasTransientState);
    }
    public static void postInvalidateOnAnimation(View view) {
        IMPL.postInvalidateOnAnimation(view);
    }
    public static void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
        IMPL.postInvalidateOnAnimation(view, left, top, right, bottom);
    }
    public static void postOnAnimation(View view, Runnable action) {
        IMPL.postOnAnimation(view, action);
    }
    public static void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
        IMPL.postOnAnimationDelayed(view, action, delayMillis);
    }    
    public static float getAlpha(View view) {
        return IMPL.getAlpha(view);
    }
    public static void setLayerType(View view, int layerType, Paint paint) {
        IMPL.setLayerType(view, layerType, paint);
    }
    public static int getLayerType(View view) {
        return IMPL.getLayerType(view);
    }
    public static int getLabelFor(View view) {
        return IMPL.getLabelFor(view);
    }
    public static void setLabelFor(View view, int labeledId) {
        IMPL.setLabelFor(view, labeledId);
    }
    public static void setLayerPaint(View view, Paint paint) {
        IMPL.setLayerPaint(view, paint);
    }
    public static int getLayoutDirection(View view) {
        return IMPL.getLayoutDirection(view);
    }
    public static void setLayoutDirection(View view, int layoutDirection) {
        IMPL.setLayoutDirection(view, layoutDirection);
    }
    public static ViewParent getParentForAccessibility(View view) {
        return IMPL.getParentForAccessibility(view);
    }
    public static boolean isOpaque(View view) {
        return IMPL.isOpaque(view);
    }
    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        return IMPL.resolveSizeAndState(size, measureSpec, childMeasuredState);
    }
    public static int getMeasuredWidthAndState(View view) {
        return IMPL.getMeasuredWidthAndState(view);
    }
    public static int getMeasuredHeightAndState(View view) {
        return IMPL.getMeasuredHeightAndState(view);
    }
    public static int getMeasuredState(View view) {
        return IMPL.getMeasuredState(view);
    }
    public static int combineMeasuredStates(int curState, int newState) {
        return IMPL.combineMeasuredStates(curState, newState);
    }
    public static int getPaddingStart(View view) {
        return IMPL.getPaddingStart(view);
    }
    public static int getPaddingEnd(View view) {
        return IMPL.getPaddingEnd(view);
    }
    public static void setPaddingRelative(View view, int start, int top, int end, int bottom) {
        IMPL.setPaddingRelative(view, start, top, end, bottom);
    }
    public static void dispatchStartTemporaryDetach(View view) {
        IMPL.dispatchStartTemporaryDetach(view);
    }
    public static void dispatchFinishTemporaryDetach(View view) {
        IMPL.dispatchFinishTemporaryDetach(view);
    }
    public static float getTranslationX(View view) {
        return IMPL.getTranslationX(view);
    }
    public static float getTranslationY(View view) {
        return IMPL.getTranslationY(view);
    }
    public static int getMinimumWidth(View view) {
        return IMPL.getMinimumWidth(view);
    }
    public static int getMinimumHeight(View view) {
        return IMPL.getMinimumHeight(view);
    }
    public static ViewPropertyAnimatorCompat animate(View view) {
        return IMPL.animate(view);
    }
    public static void setTranslationX(View view, float value) {
        IMPL.setTranslationX(view, value);
    }
    public static void setTranslationY(View view, float value) {
        IMPL.setTranslationY(view, value);
    }
    public static void setAlpha(View view, float value) {
        IMPL.setAlpha(view, value);
    }
    public static void setX(View view, float value) {
        IMPL.setX(view, value);
    }
    public static void setY(View view, float value) {
        IMPL.setY(view, value);
    }
    public static void setRotation(View view, float value) {
        IMPL.setRotation(view, value);
    }
    public static void setRotationX(View view, float value) {
        IMPL.setRotationX(view, value);
    }
    public static void setRotationY(View view, float value) {
        IMPL.setRotationY(view, value);
    }
    public static void setScaleX(View view, float value) {
        IMPL.setScaleX(view, value);
    }
    public static void setScaleY(View view, float value) {
        IMPL.setScaleY(view, value);
    }
    public static float getPivotX(View view) {
        return IMPL.getPivotX(view);
    }
    public static void setPivotX(View view, float value) {
        IMPL.setPivotX(view, value);
    }
    public static float getPivotY(View view) {
        return IMPL.getPivotY(view);
    }
    public static void setPivotY(View view, float value) {
        IMPL.setPivotY(view, value);
    }
    public static float getRotation(View view) {
        return IMPL.getRotation(view);
    }
    public static float getRotationX(View view) {
        return IMPL.getRotationX(view);
    }
    public static float getRotationY(View view) {
        return IMPL.getRotationY(view);
    }
    public static float getScaleX(View view) {
        return IMPL.getScaleX(view);
    }
    public static float getScaleY(View view) {
        return IMPL.getScaleY(view);
    }
    public static float getX(View view) {
        return IMPL.getX(view);
    }
    public static float getY(View view) {
        return IMPL.getY(view);
    }
    public static void setElevation(View view, float elevation) {
        IMPL.setElevation(view, elevation);
    }
    public static float getElevation(View view) {
        return IMPL.getElevation(view);
    }
    public static void setTranslationZ(View view, float translationZ) {
        IMPL.setTranslationZ(view, translationZ);
    }
    public static float getTranslationZ(View view) {
        return IMPL.getTranslationZ(view);
    }
    public static void setTransitionName(View view, String transitionName) {
        IMPL.setTransitionName(view, transitionName);
    }
    public static String getTransitionName(View view) {
        return IMPL.getTransitionName(view);
    }
    public static int getWindowSystemUiVisibility(View view) {
        return IMPL.getWindowSystemUiVisibility(view);
    }
    public static void requestApplyInsets(View view) {
        IMPL.requestApplyInsets(view);
    }
    public static void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean enabled) {
		IMPL.setChildrenDrawingOrderEnabled(viewGroup, enabled);
    }
    public static boolean getFitsSystemWindows(View v) {
        return IMPL.getFitsSystemWindows(v);
    }
    public static void setFitsSystemWindows(View view, boolean fitSystemWindows) {
        IMPL.setFitsSystemWindows(view, fitSystemWindows);
    }
    public static void jumpDrawablesToCurrentState(View v) {
        IMPL.jumpDrawablesToCurrentState(v);
    }
    public static void setOnApplyWindowInsetsListener(View v, OnApplyWindowInsetsListener listener) {
        IMPL.setOnApplyWindowInsetsListener(v, listener);
    }
    public static WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
        return IMPL.onApplyWindowInsets(view, insets);
    }
    public static WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat insets) {
        return IMPL.dispatchApplyWindowInsets(view, insets);
    }
    public static void setSaveFromParentEnabled(View v, boolean enabled) {
        IMPL.setSaveFromParentEnabled(v, enabled);
    }
    public static void setActivated(View view, boolean activated) {
        IMPL.setActivated(view, activated);
    }
    public static boolean hasOverlappingRendering(View view) {
        return IMPL.hasOverlappingRendering(view);
    }
    public static boolean isPaddingRelative(View view) {
        return IMPL.isPaddingRelative(view);
    }
    public static ColorStateList getBackgroundTintList(View view) {
        return IMPL.getBackgroundTintList(view);
    }
    public static void setBackgroundTintList(View view, ColorStateList tintList) {
        IMPL.setBackgroundTintList(view, tintList);
    }
    public static PorterDuff.Mode getBackgroundTintMode(View view) {
        return IMPL.getBackgroundTintMode(view);
    }
    public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        IMPL.setBackgroundTintMode(view, mode);
    }
    public static void setNestedScrollingEnabled(View view, boolean enabled) {
        IMPL.setNestedScrollingEnabled(view, enabled);
    }
    public static boolean isNestedScrollingEnabled(View view) {
        return IMPL.isNestedScrollingEnabled(view);
    }
    public static boolean startNestedScroll(View view, int axes) {
        return IMPL.startNestedScroll(view, axes);
    }
    public static void stopNestedScroll(View view) {
        IMPL.stopNestedScroll(view);
    }
    public static boolean hasNestedScrollingParent(View view) {
        return IMPL.hasNestedScrollingParent(view);
    }
    public static boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return IMPL.dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }
    public static boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return IMPL.dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
    }
    public static boolean dispatchNestedFling(View view, float velocityX, float velocityY, boolean consumed) {
        return IMPL.dispatchNestedFling(view, velocityX, velocityY, consumed);
    }
    public static boolean dispatchNestedPreFling(View view, float velocityX, float velocityY) {
        return IMPL.dispatchNestedPreFling(view, velocityX, velocityY);
    }
    public static boolean isLaidOut(View view) {
        return IMPL.isLaidOut(view);
    }
    public static float getZ(View view) {
        return IMPL.getZ(view);
    }
    public static void offsetTopAndBottom(View view, int offset) {
        IMPL.offsetTopAndBottom(view, offset);
    }
    public static void offsetLeftAndRight(View view, int offset) {
        IMPL.offsetLeftAndRight(view, offset);
    }
    public static void setClipBounds(View view, Rect clipBounds) {
        IMPL.setClipBounds(view, clipBounds);
    }
    public static Rect getClipBounds(View view) {
        return IMPL.getClipBounds(view);
    }
    public static boolean isAttachedToWindow(View view) {
        return IMPL.isAttachedToWindow(view);
    }
    public static boolean hasOnClickListeners(View view) {
        return IMPL.hasOnClickListeners(view);
    }
    public static void setScrollIndicators(View view, int indicators) {
        IMPL.setScrollIndicators(view, indicators);
    }
    public static void setScrollIndicators(View view, int indicators, int mask) {
        IMPL.setScrollIndicators(view, indicators, mask);
    }
    public static int getScrollIndicators(View view) {
        return IMPL.getScrollIndicators(view);
    }
    private ViewCompat() {}
}
