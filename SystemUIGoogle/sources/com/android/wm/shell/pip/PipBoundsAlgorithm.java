package com.android.wm.shell.pip;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.TypedValue;
import android.view.Gravity;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.pip.PipBoundsState;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class PipBoundsAlgorithm {
    private static final String TAG = "PipBoundsAlgorithm";
    private float mDefaultAspectRatio;
    private int mDefaultMinSize;
    private float mDefaultSizePercent;
    private int mDefaultStackGravity;
    private float mMaxAspectRatio;
    private float mMaxAspectRatioForMinSize;
    private float mMinAspectRatio;
    private float mMinAspectRatioForMinSize;
    private int mOverridableMinSize;
    private final PipBoundsState mPipBoundsState;
    private Point mScreenEdgeInsets;
    private final PipSnapAlgorithm mSnapAlgorithm;

    public PipBoundsAlgorithm(Context context, PipBoundsState pipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        this.mPipBoundsState = pipBoundsState;
        this.mSnapAlgorithm = pipSnapAlgorithm;
        reloadResources(context);
        pipBoundsState.setAspectRatio(this.mDefaultAspectRatio);
        pipBoundsState.setMinEdgeSize(this.mDefaultMinSize);
    }

    private void reloadResources(Context context) {
        Point point;
        Resources resources = context.getResources();
        this.mDefaultAspectRatio = resources.getFloat(17105088);
        this.mDefaultStackGravity = resources.getInteger(17694786);
        this.mDefaultMinSize = resources.getDimensionPixelSize(17105178);
        this.mOverridableMinSize = resources.getDimensionPixelSize(17105434);
        String string = resources.getString(17039904);
        Size parseSize = !string.isEmpty() ? Size.parseSize(string) : null;
        if (parseSize == null) {
            point = new Point();
        } else {
            point = new Point(dpToPx((float) parseSize.getWidth(), resources.getDisplayMetrics()), dpToPx((float) parseSize.getHeight(), resources.getDisplayMetrics()));
        }
        this.mScreenEdgeInsets = point;
        this.mMinAspectRatio = resources.getFloat(17105091);
        this.mMaxAspectRatio = resources.getFloat(17105090);
        this.mDefaultSizePercent = resources.getFloat(17105089);
        float f = resources.getFloat(17105087);
        this.mMaxAspectRatioForMinSize = f;
        this.mMinAspectRatioForMinSize = 1.0f / f;
    }

    public PipSnapAlgorithm getSnapAlgorithm() {
        return this.mSnapAlgorithm;
    }

    public void onConfigurationChanged(Context context) {
        reloadResources(context);
    }

    public Rect getNormalBounds() {
        return transformBoundsToAspectRatioIfValid(getDefaultBounds(), this.mPipBoundsState.getAspectRatio(), false, false);
    }

    public Rect getDefaultBounds() {
        return getDefaultBounds(-1.0f, null);
    }

    public Rect getEntryDestinationBounds() {
        Rect rect;
        PipBoundsState.PipReentryState reentryState = this.mPipBoundsState.getReentryState();
        if (reentryState != null) {
            rect = getDefaultBounds(reentryState.getSnapFraction(), reentryState.getSize());
        } else {
            rect = getDefaultBounds();
        }
        return transformBoundsToAspectRatioIfValid(rect, this.mPipBoundsState.getAspectRatio(), false, (reentryState == null || reentryState.getSize() == null) ? false : true);
    }

    public Rect getAdjustedDestinationBounds(Rect rect, float f) {
        return transformBoundsToAspectRatioIfValid(rect, f, true, false);
    }

    public Size getMinimalSize(ActivityInfo activityInfo) {
        ActivityInfo.WindowLayout windowLayout;
        if (activityInfo == null || (windowLayout = activityInfo.windowLayout) == null || windowLayout.minWidth <= 0 || windowLayout.minHeight <= 0) {
            return null;
        }
        return new Size(Math.max(windowLayout.minWidth, this.mOverridableMinSize), Math.max(windowLayout.minHeight, this.mOverridableMinSize));
    }

    public static Rect getValidSourceHintRect(PictureInPictureParams pictureInPictureParams, Rect rect) {
        Rect sourceRectHint = (pictureInPictureParams == null || !pictureInPictureParams.hasSourceBoundsHint()) ? null : pictureInPictureParams.getSourceRectHint();
        if (sourceRectHint == null || !rect.contains(sourceRectHint)) {
            return null;
        }
        return sourceRectHint;
    }

    public float getDefaultAspectRatio() {
        return this.mDefaultAspectRatio;
    }

    public float getAspectRatioOrDefault(PictureInPictureParams pictureInPictureParams) {
        if (pictureInPictureParams == null || !pictureInPictureParams.hasSetAspectRatio()) {
            return getDefaultAspectRatio();
        }
        return pictureInPictureParams.getAspectRatio();
    }

    private boolean isValidPictureInPictureAspectRatio(float f) {
        return Float.compare(this.mMinAspectRatio, f) <= 0 && Float.compare(f, this.mMaxAspectRatio) <= 0;
    }

    private Rect transformBoundsToAspectRatioIfValid(Rect rect, float f, boolean z, boolean z2) {
        Rect rect2 = new Rect(rect);
        if (isValidPictureInPictureAspectRatio(f)) {
            transformBoundsToAspectRatio(rect2, f, z, z2);
        }
        return rect2;
    }

    public void transformBoundsToAspectRatio(Rect rect, float f, boolean z, boolean z2) {
        Size size;
        int i;
        float snapFraction = this.mSnapAlgorithm.getSnapFraction(rect, getMovementBounds(rect), this.mPipBoundsState.getStashedState());
        Size overrideMinSize = this.mPipBoundsState.getOverrideMinSize();
        if (z || z2) {
            if (overrideMinSize == null) {
                i = this.mDefaultMinSize;
            } else {
                i = this.mPipBoundsState.getOverrideMinEdgeSize();
            }
            if (z) {
                i = this.mPipBoundsState.getMinEdgeSize();
            }
            size = getSizeForAspectRatio(new Size(rect.width(), rect.height()), f, (float) i);
        } else if (overrideMinSize != null) {
            size = adjustSizeToAspectRatio(overrideMinSize, f);
        } else {
            DisplayLayout displayLayout = this.mPipBoundsState.getDisplayLayout();
            size = getSizeForAspectRatio(f, (float) this.mDefaultMinSize, displayLayout.width(), displayLayout.height());
        }
        int centerX = (int) (((float) rect.centerX()) - (((float) size.getWidth()) / 2.0f));
        int centerY = (int) (((float) rect.centerY()) - (((float) size.getHeight()) / 2.0f));
        rect.set(centerX, centerY, size.getWidth() + centerX, size.getHeight() + centerY);
        this.mSnapAlgorithm.applySnapFraction(rect, getMovementBounds(rect), snapFraction);
    }

    private Size adjustSizeToAspectRatio(Size size, float f) {
        if (((float) size.getWidth()) / ((float) size.getHeight()) > f) {
            return new Size(size.getWidth(), (int) (((float) size.getWidth()) / f));
        }
        return new Size((int) (((float) size.getHeight()) * f), size.getHeight());
    }

    private Rect getDefaultBounds(float f, Size size) {
        Size size2;
        Rect rect = new Rect();
        int i = (f > -1.0f ? 1 : (f == -1.0f ? 0 : -1));
        int i2 = 0;
        if (i == 0 || size == null) {
            Rect rect2 = new Rect();
            getInsetBounds(rect2);
            DisplayLayout displayLayout = this.mPipBoundsState.getDisplayLayout();
            Size overrideMinSize = this.mPipBoundsState.getOverrideMinSize();
            if (overrideMinSize != null) {
                size2 = adjustSizeToAspectRatio(overrideMinSize, this.mDefaultAspectRatio);
            } else {
                size2 = getSizeForAspectRatio(this.mDefaultAspectRatio, (float) this.mDefaultMinSize, displayLayout.width(), displayLayout.height());
            }
            if (i != 0) {
                rect.set(0, 0, size2.getWidth(), size2.getHeight());
                this.mSnapAlgorithm.applySnapFraction(rect, getMovementBounds(rect), f);
            } else {
                int i3 = this.mDefaultStackGravity;
                int width = size2.getWidth();
                int height = size2.getHeight();
                int imeHeight = this.mPipBoundsState.isImeShowing() ? this.mPipBoundsState.getImeHeight() : 0;
                if (this.mPipBoundsState.isShelfShowing()) {
                    i2 = this.mPipBoundsState.getShelfHeight();
                }
                Gravity.apply(i3, width, height, rect2, 0, Math.max(imeHeight, i2), rect);
            }
            return rect;
        }
        rect.set(0, 0, size.getWidth(), size.getHeight());
        this.mSnapAlgorithm.applySnapFraction(rect, getMovementBounds(rect), f);
        return rect;
    }

    public void getInsetBounds(Rect rect) {
        DisplayLayout displayLayout = this.mPipBoundsState.getDisplayLayout();
        Rect stableInsets = this.mPipBoundsState.getDisplayLayout().stableInsets();
        int i = stableInsets.left;
        Point point = this.mScreenEdgeInsets;
        rect.set(i + point.x, stableInsets.top + point.y, (displayLayout.width() - stableInsets.right) - this.mScreenEdgeInsets.x, (displayLayout.height() - stableInsets.bottom) - this.mScreenEdgeInsets.y);
    }

    public Rect getMovementBounds(Rect rect) {
        return getMovementBounds(rect, true);
    }

    public Rect getMovementBounds(Rect rect, boolean z) {
        Rect rect2 = new Rect();
        getInsetBounds(rect2);
        getMovementBounds(rect, rect2, rect2, (!z || !this.mPipBoundsState.isImeShowing()) ? 0 : this.mPipBoundsState.getImeHeight());
        return rect2;
    }

    public void getMovementBounds(Rect rect, Rect rect2, Rect rect3, int i) {
        rect3.set(rect2);
        rect3.right = Math.max(rect2.left, rect2.right - rect.width());
        int max = Math.max(rect2.top, rect2.bottom - rect.height());
        rect3.bottom = max;
        rect3.bottom = max - i;
    }

    public float getSnapFraction(Rect rect) {
        return getSnapFraction(rect, getMovementBounds(rect));
    }

    public float getSnapFraction(Rect rect, Rect rect2) {
        return this.mSnapAlgorithm.getSnapFraction(rect, rect2);
    }

    public void applySnapFraction(Rect rect, float f) {
        this.mSnapAlgorithm.applySnapFraction(rect, getMovementBounds(rect), f);
    }

    public int getDefaultMinSize() {
        return this.mDefaultMinSize;
    }

    private int dpToPx(float f, DisplayMetrics displayMetrics) {
        return (int) TypedValue.applyDimension(1, f, displayMetrics);
    }

    public Size getSizeForAspectRatio(float f, float f2, int i, int i2) {
        int i3;
        int i4;
        int max = (int) Math.max(f2, ((float) Math.min(i, i2)) * this.mDefaultSizePercent);
        if (f > this.mMinAspectRatioForMinSize) {
            float f3 = this.mMaxAspectRatioForMinSize;
            if (f <= f3) {
                float f4 = (float) max;
                float length = PointF.length(f3 * f4, f4);
                max = (int) Math.round(Math.sqrt((double) ((length * length) / ((f * f) + 1.0f))));
                i4 = Math.round(((float) max) * f);
                max = i4;
                i3 = max;
                return new Size(max, i3);
            }
        }
        if (f <= 1.0f) {
            i3 = Math.round(((float) max) / f);
            return new Size(max, i3);
        }
        i4 = Math.round(((float) max) * f);
        max = i4;
        i3 = max;
        return new Size(max, i3);
    }

    public Size getSizeForAspectRatio(Size size, float f, float f2) {
        int i;
        int max = (int) Math.max(f2, (float) Math.min(size.getWidth(), size.getHeight()));
        if (f <= 1.0f) {
            i = Math.round(((float) max) / f);
        } else {
            max = Math.round(((float) max) * f);
            i = max;
        }
        return new Size(max, i);
    }

    public Rect adjustNormalBoundsToFitMenu(Rect rect, Size size) {
        int i;
        int i2;
        if (size == null) {
            return rect;
        }
        if (rect.width() >= size.getWidth() && rect.height() >= size.getHeight()) {
            return rect;
        }
        Rect rect2 = new Rect();
        boolean z = size.getWidth() > rect.width();
        boolean z2 = size.getHeight() > rect.height();
        if (!z || !z2) {
            if (z) {
                i2 = size.getWidth();
                i = Math.round(((float) i2) / this.mPipBoundsState.getAspectRatio());
            } else {
                i = size.getHeight();
                i2 = Math.round(((float) i) * this.mPipBoundsState.getAspectRatio());
            }
        } else if (((float) size.getWidth()) / ((float) rect.width()) > ((float) size.getHeight()) / ((float) rect.height())) {
            i2 = size.getWidth();
            i = Math.round(((float) i2) / this.mPipBoundsState.getAspectRatio());
        } else {
            i = size.getHeight();
            i2 = Math.round(((float) i) * this.mPipBoundsState.getAspectRatio());
        }
        rect2.set(0, 0, i2, i);
        transformBoundsToAspectRatio(rect2, this.mPipBoundsState.getAspectRatio(), true, true);
        return rect2;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        printWriter.println(str2 + "mDefaultAspectRatio=" + this.mDefaultAspectRatio);
        printWriter.println(str2 + "mMinAspectRatio=" + this.mMinAspectRatio);
        printWriter.println(str2 + "mMaxAspectRatio=" + this.mMaxAspectRatio);
        printWriter.println(str2 + "mDefaultStackGravity=" + this.mDefaultStackGravity);
        printWriter.println(str2 + "mSnapAlgorithm" + this.mSnapAlgorithm);
    }
}
