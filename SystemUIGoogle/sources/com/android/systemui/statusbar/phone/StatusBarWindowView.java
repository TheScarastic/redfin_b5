package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Insets;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.android.systemui.ScreenDecorations;
/* loaded from: classes.dex */
public class StatusBarWindowView extends FrameLayout {
    private int mLeftInset = 0;
    private int mRightInset = 0;
    private int mTopInset = 0;
    private float mTouchDownY = 0.0f;

    public StatusBarWindowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
        this.mLeftInset = insetsIgnoringVisibility.left;
        this.mRightInset = insetsIgnoringVisibility.right;
        this.mTopInset = 0;
        DisplayCutout displayCutout = getRootWindowInsets().getDisplayCutout();
        if (displayCutout != null) {
            this.mTopInset = displayCutout.getWaterfallInsets().top;
        }
        applyMargins();
        return windowInsets;
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && motionEvent.getRawY() > ((float) getHeight())) {
            this.mTouchDownY = motionEvent.getRawY();
            motionEvent.setLocation(motionEvent.getRawX(), (float) this.mTopInset);
        } else if (motionEvent.getAction() == 2 && this.mTouchDownY != 0.0f) {
            motionEvent.setLocation(motionEvent.getRawX(), (((float) this.mTopInset) + motionEvent.getRawY()) - this.mTouchDownY);
        } else if (motionEvent.getAction() == 1) {
            this.mTouchDownY = 0.0f;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    private void applyMargins() {
        FrameLayout.LayoutParams layoutParams;
        int i;
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if ((childAt.getLayoutParams() instanceof FrameLayout.LayoutParams) && !((layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams()).rightMargin == (i = this.mRightInset) && layoutParams.leftMargin == this.mLeftInset && layoutParams.topMargin == this.mTopInset)) {
                layoutParams.rightMargin = i;
                layoutParams.leftMargin = this.mLeftInset;
                layoutParams.topMargin = this.mTopInset;
                childAt.requestLayout();
            }
        }
    }

    public static Pair<Integer, Integer> paddingNeededForCutoutAndRoundedCorner(DisplayCutout displayCutout, Pair<Integer, Integer> pair, int i) {
        if (displayCutout == null) {
            return new Pair<>(Integer.valueOf(i), Integer.valueOf(i));
        }
        int safeInsetLeft = displayCutout.getSafeInsetLeft();
        int safeInsetRight = displayCutout.getSafeInsetRight();
        if (pair != null) {
            safeInsetLeft = Math.max(safeInsetLeft, ((Integer) pair.first).intValue());
            safeInsetRight = Math.max(safeInsetRight, ((Integer) pair.second).intValue());
        }
        return new Pair<>(Integer.valueOf(Math.max(safeInsetLeft, i)), Integer.valueOf(Math.max(safeInsetRight, i)));
    }

    public static Pair<Integer, Integer> cornerCutoutMargins(DisplayCutout displayCutout, Display display) {
        return statusBarCornerCutoutMargins(displayCutout, display, 0, 0);
    }

    public static Pair<Integer, Integer> statusBarCornerCutoutMargins(DisplayCutout displayCutout, Display display, int i, int i2) {
        if (displayCutout == null) {
            return null;
        }
        Point point = new Point();
        display.getRealSize(point);
        Rect rect = new Rect();
        if (i == 0) {
            ScreenDecorations.DisplayCutoutView.boundsFromDirection(displayCutout, 48, rect);
        } else if (i == 1) {
            ScreenDecorations.DisplayCutoutView.boundsFromDirection(displayCutout, 3, rect);
        } else if (i == 2) {
            return null;
        } else {
            if (i == 3) {
                ScreenDecorations.DisplayCutoutView.boundsFromDirection(displayCutout, 5, rect);
            }
        }
        if (i2 >= 0 && rect.top > i2) {
            return null;
        }
        if (rect.left <= 0) {
            return new Pair<>(Integer.valueOf(rect.right), 0);
        }
        if (rect.right >= point.x) {
            return new Pair<>(0, Integer.valueOf(point.x - rect.left));
        }
        return null;
    }
}
