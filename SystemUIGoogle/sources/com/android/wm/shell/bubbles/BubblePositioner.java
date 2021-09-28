package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.BubbleStackView;
/* loaded from: classes2.dex */
public class BubblePositioner {
    private int mBubbleBadgeSize;
    private int mBubbleSize;
    private Context mContext;
    private int mDefaultMaxBubbles;
    private int mExpandedViewLargeScreenWidth;
    private int mExpandedViewPadding;
    private Insets mInsets;
    private boolean mIsLargeScreen;
    private int mMaxBubbles;
    private PointF mPinLocation;
    private float mPointerHeight;
    private int mPointerMargin;
    private float mPointerWidth;
    private Rect mPositionRect;
    private PointF mRestingStackPosition;
    private boolean mShowingInTaskbar;
    private int mSpacingBetweenBubbles;
    private int mTaskbarIconSize;
    private int mTaskbarSize;
    private WindowManager mWindowManager;
    private int mRotation = 0;
    private int[] mPaddings = new int[4];
    private int mTaskbarPosition = -1;

    public BubblePositioner(Context context, WindowManager windowManager) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        update();
    }

    public void setRotation(int i) {
        this.mRotation = i;
    }

    public void update() {
        WindowMetrics currentWindowMetrics = this.mWindowManager.getCurrentWindowMetrics();
        if (currentWindowMetrics != null) {
            Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.statusBars() | WindowInsets.Type.displayCutout());
            this.mIsLargeScreen = this.mContext.getResources().getConfiguration().smallestScreenWidthDp >= 600;
            updateInternal(this.mRotation, insetsIgnoringVisibility, currentWindowMetrics.getBounds());
        }
    }

    public void updateForTaskbar(int i, int i2, boolean z, int i3) {
        this.mShowingInTaskbar = z;
        this.mTaskbarIconSize = i;
        this.mTaskbarPosition = i2;
        this.mTaskbarSize = i3;
        update();
    }

    public void updateInternal(int i, Insets insets, Rect rect) {
        this.mRotation = i;
        this.mInsets = insets;
        Rect rect2 = new Rect(rect);
        this.mPositionRect = rect2;
        int i2 = rect2.left;
        Insets insets2 = this.mInsets;
        rect2.left = i2 + insets2.left;
        rect2.top += insets2.top;
        rect2.right -= insets2.right;
        rect2.bottom -= insets2.bottom;
        Resources resources = this.mContext.getResources();
        this.mBubbleSize = resources.getDimensionPixelSize(R.dimen.bubble_size);
        this.mBubbleBadgeSize = resources.getDimensionPixelSize(R.dimen.bubble_badge_size);
        this.mSpacingBetweenBubbles = resources.getDimensionPixelSize(R.dimen.bubble_spacing);
        this.mDefaultMaxBubbles = resources.getInteger(R.integer.bubbles_max_rendered);
        this.mExpandedViewLargeScreenWidth = resources.getDimensionPixelSize(R.dimen.bubble_expanded_view_tablet_width);
        this.mExpandedViewPadding = resources.getDimensionPixelSize(R.dimen.bubble_expanded_view_padding);
        this.mPointerWidth = (float) resources.getDimensionPixelSize(R.dimen.bubble_pointer_width);
        this.mPointerHeight = (float) resources.getDimensionPixelSize(R.dimen.bubble_pointer_height);
        this.mPointerMargin = resources.getDimensionPixelSize(R.dimen.bubble_pointer_margin);
        this.mMaxBubbles = calculateMaxBubbles();
        if (this.mShowingInTaskbar) {
            adjustForTaskbar();
        }
    }

    private int calculateMaxBubbles() {
        int min = Math.min(this.mPositionRect.width(), this.mPositionRect.height()) - (showBubblesVertically() ? 0 : this.mExpandedViewPadding * 2);
        int i = this.mBubbleSize;
        int i2 = (min - i) / (i + this.mSpacingBetweenBubbles);
        int i3 = this.mDefaultMaxBubbles;
        return i2 < i3 ? i2 : i3;
    }

    private void adjustForTaskbar() {
        if (this.mShowingInTaskbar && this.mTaskbarPosition != 2) {
            Insets insetsIgnoringVisibility = this.mWindowManager.getCurrentWindowMetrics().getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
            Insets insets = this.mInsets;
            int i = insets.left;
            int i2 = insets.right;
            int i3 = this.mTaskbarPosition;
            if (i3 == 1) {
                Rect rect = this.mPositionRect;
                int i4 = rect.left;
                int i5 = insetsIgnoringVisibility.left;
                rect.left = i4 - i5;
                i -= i5;
            } else if (i3 == 0) {
                Rect rect2 = this.mPositionRect;
                int i6 = rect2.right;
                int i7 = insetsIgnoringVisibility.right;
                rect2.right = i6 + i7;
                i2 -= i7;
            }
            this.mInsets = Insets.of(i, insets.top, i2, insets.bottom);
        }
    }

    public Rect getAvailableRect() {
        return this.mPositionRect;
    }

    public Insets getInsets() {
        return this.mInsets;
    }

    public boolean isLandscape() {
        int i = this.mRotation;
        return i == 1 || i == 3;
    }

    public boolean isLargeScreen() {
        return this.mIsLargeScreen;
    }

    public boolean showBubblesVertically() {
        return isLandscape() || this.mShowingInTaskbar || this.mIsLargeScreen;
    }

    public int getBubbleSize() {
        int i;
        return (!this.mShowingInTaskbar || (i = this.mTaskbarIconSize) <= 0) ? this.mBubbleSize : i;
    }

    public int getMaxBubbles() {
        return this.mMaxBubbles;
    }

    public int[] getExpandedViewPadding(boolean z, boolean z2) {
        Insets insets = this.mInsets;
        int i = insets.left;
        int i2 = this.mExpandedViewPadding;
        int i3 = i + i2;
        int i4 = insets.right + i2;
        boolean z3 = this.mIsLargeScreen || z2;
        if (showBubblesVertically()) {
            if (!z) {
                i4 = (int) (((float) i4) + (((float) this.mBubbleSize) - this.mPointerHeight));
                i3 += z3 ? (this.mPositionRect.width() - i4) - this.mExpandedViewLargeScreenWidth : 0;
            } else {
                i3 = (int) (((float) i3) + (((float) this.mBubbleSize) - this.mPointerHeight));
                i4 += z3 ? (this.mPositionRect.width() - i3) - this.mExpandedViewLargeScreenWidth : 0;
            }
        }
        int[] iArr = this.mPaddings;
        iArr[0] = i3;
        iArr[1] = showBubblesVertically() ? 0 : this.mPointerMargin;
        int[] iArr2 = this.mPaddings;
        iArr2[2] = i4;
        iArr2[3] = 0;
        return iArr2;
    }

    public float getExpandedViewY() {
        int i = getAvailableRect().top;
        if (showBubblesVertically()) {
            return ((float) i) - this.mPointerWidth;
        }
        return (float) (i + this.mBubbleSize + this.mPointerMargin);
    }

    public void setRestingPosition(PointF pointF) {
        PointF pointF2 = this.mRestingStackPosition;
        if (pointF2 == null) {
            this.mRestingStackPosition = new PointF(pointF);
        } else {
            pointF2.set(pointF);
        }
    }

    public PointF getRestingPosition() {
        PointF pointF = this.mPinLocation;
        if (pointF != null) {
            return pointF;
        }
        PointF pointF2 = this.mRestingStackPosition;
        return pointF2 == null ? getDefaultStartPosition() : pointF2;
    }

    public PointF getDefaultStartPosition() {
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
            z = false;
        }
        return new BubbleStackView.RelativeStackPosition(z, ((float) this.mContext.getResources().getDimensionPixelOffset(R.dimen.bubble_stack_starting_offset_y)) / ((float) this.mPositionRect.height())).getAbsolutePositionInRegion(new RectF(this.mPositionRect));
    }

    public boolean showingInTaskbar() {
        return this.mShowingInTaskbar;
    }

    public int getTaskbarPosition() {
        return this.mTaskbarPosition;
    }

    public int getTaskbarSize() {
        return this.mTaskbarSize;
    }

    public void setPinnedLocation(PointF pointF) {
        this.mPinLocation = pointF;
    }
}
