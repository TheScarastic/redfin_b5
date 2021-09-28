package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.Range;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.SeekBar;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import java.util.List;
/* loaded from: classes.dex */
public class CropView extends View {
    private int mActivePointerId;
    private final Paint mContainerBackgroundPaint;
    private RectF mCrop;
    private CropInteractionListener mCropInteractionListener;
    private final float mCropTouchMargin;
    private CropBoundary mCurrentDraggingBoundary;
    private float mEntranceInterpolation;
    private final ExploreByTouchHelper mExploreByTouchHelper;
    private int mExtraBottomPadding;
    private int mExtraTopPadding;
    private final Paint mHandlePaint;
    private int mImageWidth;
    private Range<Float> mMotionRange;
    private float mMovementStartValue;
    private final Paint mShadePaint;
    private float mStartingX;
    private float mStartingY;

    /* loaded from: classes.dex */
    public enum CropBoundary {
        NONE,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    /* loaded from: classes.dex */
    public interface CropInteractionListener {
        void onCropDragComplete();

        void onCropDragMoved(CropBoundary cropBoundary, float f, int i, float f2, float f3);

        void onCropDragStarted(CropBoundary cropBoundary, float f, int i, float f2, float f3);
    }

    public CropView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CropView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCrop = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.mCurrentDraggingBoundary = CropBoundary.NONE;
        this.mEntranceInterpolation = 1.0f;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.CropView, 0, 0);
        Paint paint = new Paint();
        this.mShadePaint = paint;
        paint.setColor(ColorUtils.setAlphaComponent(obtainStyledAttributes.getColor(R$styleable.CropView_scrimColor, 0), obtainStyledAttributes.getInteger(R$styleable.CropView_scrimAlpha, 255)));
        Paint paint2 = new Paint();
        this.mContainerBackgroundPaint = paint2;
        paint2.setColor(obtainStyledAttributes.getColor(R$styleable.CropView_containerBackgroundColor, 0));
        Paint paint3 = new Paint();
        this.mHandlePaint = paint3;
        paint3.setColor(obtainStyledAttributes.getColor(R$styleable.CropView_handleColor, -16777216));
        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setStrokeWidth((float) obtainStyledAttributes.getDimensionPixelSize(R$styleable.CropView_handleThickness, 20));
        obtainStyledAttributes.recycle();
        this.mCropTouchMargin = getResources().getDisplayMetrics().density * 24.0f;
        AccessibilityHelper accessibilityHelper = new AccessibilityHelper();
        this.mExploreByTouchHelper = accessibilityHelper;
        ViewCompat.setAccessibilityDelegate(this, accessibilityHelper);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mCrop = this.mCrop;
        return savedState;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCrop = savedState.mCrop;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float lerp = MathUtils.lerp(this.mCrop.top, 0.0f, this.mEntranceInterpolation);
        float lerp2 = MathUtils.lerp(this.mCrop.bottom, 1.0f, this.mEntranceInterpolation);
        drawShade(canvas, 0.0f, lerp, 1.0f, this.mCrop.top);
        drawShade(canvas, 0.0f, this.mCrop.bottom, 1.0f, lerp2);
        RectF rectF = this.mCrop;
        drawShade(canvas, 0.0f, rectF.top, rectF.left, rectF.bottom);
        RectF rectF2 = this.mCrop;
        drawShade(canvas, rectF2.right, rectF2.top, 1.0f, rectF2.bottom);
        drawContainerBackground(canvas, 0.0f, 0.0f, 1.0f, lerp);
        drawContainerBackground(canvas, 0.0f, lerp2, 1.0f, 1.0f);
        this.mHandlePaint.setAlpha((int) (this.mEntranceInterpolation * 255.0f));
        drawHorizontalHandle(canvas, this.mCrop.top, true);
        drawHorizontalHandle(canvas, this.mCrop.bottom, false);
        drawVerticalHandle(canvas, this.mCrop.left, true);
        drawVerticalHandle(canvas, this.mCrop.right, false);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f;
        float f2;
        int fractionToVerticalPixels = fractionToVerticalPixels(this.mCrop.top);
        int fractionToVerticalPixels2 = fractionToVerticalPixels(this.mCrop.bottom);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked != 5) {
                            if (actionMasked == 6 && this.mActivePointerId == motionEvent.getPointerId(motionEvent.getActionIndex()) && this.mCurrentDraggingBoundary != CropBoundary.NONE) {
                                updateListener(1, motionEvent.getX(motionEvent.getActionIndex()));
                                return true;
                            }
                        } else if (this.mActivePointerId == motionEvent.getPointerId(motionEvent.getActionIndex()) && this.mCurrentDraggingBoundary != CropBoundary.NONE) {
                            updateListener(0, motionEvent.getX(motionEvent.getActionIndex()));
                            return true;
                        }
                    }
                } else if (this.mCurrentDraggingBoundary != CropBoundary.NONE) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex >= 0) {
                        if (isVertical(this.mCurrentDraggingBoundary)) {
                            f2 = motionEvent.getY(findPointerIndex);
                            f = this.mStartingY;
                        } else {
                            f2 = motionEvent.getX(findPointerIndex);
                            f = this.mStartingX;
                        }
                        setBoundaryPosition(this.mCurrentDraggingBoundary, this.mMotionRange.clamp(Float.valueOf(this.mMovementStartValue + pixelDistanceToFraction((float) ((int) (f2 - f)), this.mCurrentDraggingBoundary))).floatValue());
                        updateListener(2, motionEvent.getX(findPointerIndex));
                        invalidate();
                    }
                    return true;
                }
                return super.onTouchEvent(motionEvent);
            }
            if (this.mCurrentDraggingBoundary != CropBoundary.NONE) {
                int i = this.mActivePointerId;
                if (i == motionEvent.getPointerId(i)) {
                    updateListener(1, motionEvent.getX(0));
                    return true;
                }
            }
            return super.onTouchEvent(motionEvent);
        }
        CropBoundary nearestBoundary = nearestBoundary(motionEvent, fractionToVerticalPixels, fractionToVerticalPixels2, fractionToHorizontalPixels(this.mCrop.left), fractionToHorizontalPixels(this.mCrop.right));
        this.mCurrentDraggingBoundary = nearestBoundary;
        if (nearestBoundary != CropBoundary.NONE) {
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mStartingY = motionEvent.getY();
            this.mStartingX = motionEvent.getX();
            this.mMovementStartValue = getBoundaryPosition(this.mCurrentDraggingBoundary);
            updateListener(0, motionEvent.getX());
            this.mMotionRange = getAllowedValues(this.mCurrentDraggingBoundary);
        }
        return true;
    }

    @Override // android.view.View
    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return this.mExploreByTouchHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return this.mExploreByTouchHelper.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.view.View
    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        this.mExploreByTouchHelper.onFocusChanged(z, i, rect);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.screenshot.CropView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary;

        static {
            int[] iArr = new int[CropBoundary.values().length];
            $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary = iArr;
            try {
                iArr[CropBoundary.TOP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[CropBoundary.BOTTOM.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[CropBoundary.LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[CropBoundary.RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[CropBoundary.NONE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public void setBoundaryPosition(CropBoundary cropBoundary, float f) {
        float floatValue = ((Float) getAllowedValues(cropBoundary).clamp(Float.valueOf(f))).floatValue();
        int i = AnonymousClass1.$SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[cropBoundary.ordinal()];
        if (i == 1) {
            this.mCrop.top = floatValue;
        } else if (i == 2) {
            this.mCrop.bottom = floatValue;
        } else if (i == 3) {
            this.mCrop.left = floatValue;
        } else if (i == 4) {
            this.mCrop.right = floatValue;
        } else if (i == 5) {
            Log.w("CropView", "No boundary selected");
        }
        invalidate();
    }

    /* access modifiers changed from: private */
    public float getBoundaryPosition(CropBoundary cropBoundary) {
        int i = AnonymousClass1.$SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[cropBoundary.ordinal()];
        if (i == 1) {
            return this.mCrop.top;
        }
        if (i == 2) {
            return this.mCrop.bottom;
        }
        if (i == 3) {
            return this.mCrop.left;
        }
        if (i != 4) {
            return 0.0f;
        }
        return this.mCrop.right;
    }

    /* access modifiers changed from: private */
    public static boolean isVertical(CropBoundary cropBoundary) {
        return cropBoundary == CropBoundary.TOP || cropBoundary == CropBoundary.BOTTOM;
    }

    public void animateEntrance() {
        this.mEntranceInterpolation = 0.0f;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.CropView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                CropView.$r8$lambda$Eu3lnVV9CoXVrdC1xPAUci9nHYI(CropView.this, valueAnimator2);
            }
        });
        valueAnimator.setFloatValues(0.0f, 1.0f);
        valueAnimator.setDuration(750L);
        valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
        valueAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateEntrance$1(ValueAnimator valueAnimator) {
        this.mEntranceInterpolation = valueAnimator.getAnimatedFraction();
        invalidate();
    }

    public void setExtraPadding(int i, int i2) {
        this.mExtraTopPadding = i;
        this.mExtraBottomPadding = i2;
        invalidate();
    }

    public void setImageWidth(int i) {
        this.mImageWidth = i;
        invalidate();
    }

    public Rect getCropBoundaries(int i, int i2) {
        RectF rectF = this.mCrop;
        float f = (float) i;
        float f2 = (float) i2;
        return new Rect((int) (rectF.left * f), (int) (rectF.top * f2), (int) (rectF.right * f), (int) (rectF.bottom * f2));
    }

    public void setCropInteractionListener(CropInteractionListener cropInteractionListener) {
        this.mCropInteractionListener = cropInteractionListener;
    }

    private Range getAllowedValues(CropBoundary cropBoundary) {
        int i = AnonymousClass1.$SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[cropBoundary.ordinal()];
        if (i == 1) {
            return new Range(Float.valueOf(0.0f), Float.valueOf(this.mCrop.bottom - pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.BOTTOM)));
        }
        if (i == 2) {
            return new Range(Float.valueOf(this.mCrop.top + pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.TOP)), Float.valueOf(1.0f));
        }
        if (i == 3) {
            return new Range(Float.valueOf(0.0f), Float.valueOf(this.mCrop.right - pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.RIGHT)));
        }
        if (i != 4) {
            return null;
        }
        return new Range(Float.valueOf(this.mCrop.left + pixelDistanceToFraction(this.mCropTouchMargin, CropBoundary.LEFT)), Float.valueOf(1.0f));
    }

    private void updateListener(int i, float f) {
        if (this.mCropInteractionListener != null && isVertical(this.mCurrentDraggingBoundary)) {
            float boundaryPosition = getBoundaryPosition(this.mCurrentDraggingBoundary);
            if (i == 0) {
                CropInteractionListener cropInteractionListener = this.mCropInteractionListener;
                CropBoundary cropBoundary = this.mCurrentDraggingBoundary;
                int fractionToVerticalPixels = fractionToVerticalPixels(boundaryPosition);
                RectF rectF = this.mCrop;
                cropInteractionListener.onCropDragStarted(cropBoundary, boundaryPosition, fractionToVerticalPixels, (rectF.left + rectF.right) / 2.0f, f);
            } else if (i == 1) {
                this.mCropInteractionListener.onCropDragComplete();
            } else if (i == 2) {
                CropInteractionListener cropInteractionListener2 = this.mCropInteractionListener;
                CropBoundary cropBoundary2 = this.mCurrentDraggingBoundary;
                int fractionToVerticalPixels2 = fractionToVerticalPixels(boundaryPosition);
                RectF rectF2 = this.mCrop;
                cropInteractionListener2.onCropDragMoved(cropBoundary2, boundaryPosition, fractionToVerticalPixels2, (rectF2.left + rectF2.right) / 2.0f, f);
            }
        }
    }

    private void drawShade(Canvas canvas, float f, float f2, float f3, float f4) {
        canvas.drawRect((float) fractionToHorizontalPixels(f), (float) fractionToVerticalPixels(f2), (float) fractionToHorizontalPixels(f3), (float) fractionToVerticalPixels(f4), this.mShadePaint);
    }

    private void drawContainerBackground(Canvas canvas, float f, float f2, float f3, float f4) {
        canvas.drawRect((float) fractionToHorizontalPixels(f), (float) fractionToVerticalPixels(f2), (float) fractionToHorizontalPixels(f3), (float) fractionToVerticalPixels(f4), this.mContainerBackgroundPaint);
    }

    private void drawHorizontalHandle(Canvas canvas, float f, boolean z) {
        float fractionToVerticalPixels = (float) fractionToVerticalPixels(f);
        canvas.drawLine((float) fractionToHorizontalPixels(this.mCrop.left), fractionToVerticalPixels, (float) fractionToHorizontalPixels(this.mCrop.right), fractionToVerticalPixels, this.mHandlePaint);
        float f2 = getResources().getDisplayMetrics().density * 8.0f;
        float fractionToHorizontalPixels = (float) ((fractionToHorizontalPixels(this.mCrop.left) + fractionToHorizontalPixels(this.mCrop.right)) / 2);
        canvas.drawArc(fractionToHorizontalPixels - f2, fractionToVerticalPixels - f2, fractionToHorizontalPixels + f2, fractionToVerticalPixels + f2, z ? 180.0f : 0.0f, 180.0f, true, this.mHandlePaint);
    }

    private void drawVerticalHandle(Canvas canvas, float f, boolean z) {
        float fractionToHorizontalPixels = (float) fractionToHorizontalPixels(f);
        canvas.drawLine(fractionToHorizontalPixels, (float) fractionToVerticalPixels(this.mCrop.top), fractionToHorizontalPixels, (float) fractionToVerticalPixels(this.mCrop.bottom), this.mHandlePaint);
        float f2 = getResources().getDisplayMetrics().density * 8.0f;
        float f3 = fractionToHorizontalPixels - f2;
        float fractionToVerticalPixels = (float) ((fractionToVerticalPixels(getBoundaryPosition(CropBoundary.TOP)) + fractionToVerticalPixels(getBoundaryPosition(CropBoundary.BOTTOM))) / 2);
        canvas.drawArc(f3, fractionToVerticalPixels - f2, fractionToHorizontalPixels + f2, fractionToVerticalPixels + f2, z ? 90.0f : 270.0f, 180.0f, true, this.mHandlePaint);
    }

    /* access modifiers changed from: private */
    public int fractionToVerticalPixels(float f) {
        return (int) (((float) this.mExtraTopPadding) + (f * ((float) getImageHeight())));
    }

    /* access modifiers changed from: private */
    public int fractionToHorizontalPixels(float f) {
        int width = getWidth();
        int i = this.mImageWidth;
        return (int) (((float) ((width - i) / 2)) + (f * ((float) i)));
    }

    private int getImageHeight() {
        return (getHeight() - this.mExtraTopPadding) - this.mExtraBottomPadding;
    }

    /* access modifiers changed from: private */
    public float pixelDistanceToFraction(float f, CropBoundary cropBoundary) {
        int i;
        if (isVertical(cropBoundary)) {
            i = getImageHeight();
        } else {
            i = this.mImageWidth;
        }
        return f / ((float) i);
    }

    private CropBoundary nearestBoundary(MotionEvent motionEvent, int i, int i2, int i3, int i4) {
        float f = (float) i;
        if (Math.abs(motionEvent.getY() - f) < this.mCropTouchMargin) {
            return CropBoundary.TOP;
        }
        float f2 = (float) i2;
        if (Math.abs(motionEvent.getY() - f2) < this.mCropTouchMargin) {
            return CropBoundary.BOTTOM;
        }
        if (motionEvent.getY() > f || motionEvent.getY() < f2) {
            if (Math.abs(motionEvent.getX() - ((float) i3)) < this.mCropTouchMargin) {
                return CropBoundary.LEFT;
            }
            if (Math.abs(motionEvent.getX() - ((float) i4)) < this.mCropTouchMargin) {
                return CropBoundary.RIGHT;
            }
        }
        return CropBoundary.NONE;
    }

    /* loaded from: classes.dex */
    private class AccessibilityHelper extends ExploreByTouchHelper {
        AccessibilityHelper() {
            super(CropView.this);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f, float f2) {
            CropView cropView = CropView.this;
            if (Math.abs(f2 - ((float) cropView.fractionToVerticalPixels(cropView.mCrop.top))) < CropView.this.mCropTouchMargin) {
                return 1;
            }
            CropView cropView2 = CropView.this;
            if (Math.abs(f2 - ((float) cropView2.fractionToVerticalPixels(cropView2.mCrop.bottom))) < CropView.this.mCropTouchMargin) {
                return 2;
            }
            CropView cropView3 = CropView.this;
            if (f2 <= ((float) cropView3.fractionToVerticalPixels(cropView3.mCrop.top))) {
                return -1;
            }
            CropView cropView4 = CropView.this;
            if (f2 >= ((float) cropView4.fractionToVerticalPixels(cropView4.mCrop.bottom))) {
                return -1;
            }
            CropView cropView5 = CropView.this;
            if (Math.abs(f - ((float) cropView5.fractionToHorizontalPixels(cropView5.mCrop.left))) < CropView.this.mCropTouchMargin) {
                return 3;
            }
            CropView cropView6 = CropView.this;
            return Math.abs(f - ((float) cropView6.fractionToHorizontalPixels(cropView6.mCrop.right))) < CropView.this.mCropTouchMargin ? 4 : -1;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            list.add(1);
            list.add(3);
            list.add(4);
            list.add(2);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateEventForVirtualView(int i, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setContentDescription(getBoundaryContentDescription(viewIdToBoundary(i)));
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            CropBoundary viewIdToBoundary = viewIdToBoundary(i);
            accessibilityNodeInfoCompat.setContentDescription(getBoundaryContentDescription(viewIdToBoundary));
            setNodePosition(getNodeRect(viewIdToBoundary), accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(SeekBar.class.getName());
            accessibilityNodeInfoCompat.addAction(4096);
            accessibilityNodeInfoCompat.addAction(8192);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            if (i2 != 4096 && i2 != 8192) {
                return false;
            }
            CropBoundary viewIdToBoundary = viewIdToBoundary(i);
            CropView cropView = CropView.this;
            float pixelDistanceToFraction = cropView.pixelDistanceToFraction(cropView.mCropTouchMargin, viewIdToBoundary);
            if (i2 == 4096) {
                pixelDistanceToFraction = -pixelDistanceToFraction;
            }
            CropView cropView2 = CropView.this;
            cropView2.setBoundaryPosition(viewIdToBoundary, pixelDistanceToFraction + cropView2.getBoundaryPosition(viewIdToBoundary));
            invalidateVirtualView(i);
            sendEventForVirtualView(i, 4);
            return true;
        }

        private CharSequence getBoundaryContentDescription(CropBoundary cropBoundary) {
            int i;
            int i2 = AnonymousClass1.$SwitchMap$com$android$systemui$screenshot$CropView$CropBoundary[cropBoundary.ordinal()];
            if (i2 == 1) {
                i = R$string.screenshot_top_boundary_pct;
            } else if (i2 == 2) {
                i = R$string.screenshot_bottom_boundary_pct;
            } else if (i2 == 3) {
                i = R$string.screenshot_left_boundary_pct;
            } else if (i2 != 4) {
                return "";
            } else {
                i = R$string.screenshot_right_boundary_pct;
            }
            return CropView.this.getResources().getString(i, Integer.valueOf(Math.round(CropView.this.getBoundaryPosition(cropBoundary) * 100.0f)));
        }

        private CropBoundary viewIdToBoundary(int i) {
            if (i == 1) {
                return CropBoundary.TOP;
            }
            if (i == 2) {
                return CropBoundary.BOTTOM;
            }
            if (i == 3) {
                return CropBoundary.LEFT;
            }
            if (i != 4) {
                return CropBoundary.NONE;
            }
            return CropBoundary.RIGHT;
        }

        private Rect getNodeRect(CropBoundary cropBoundary) {
            if (CropView.isVertical(cropBoundary)) {
                CropView cropView = CropView.this;
                float fractionToVerticalPixels = (float) cropView.fractionToVerticalPixels(cropView.getBoundaryPosition(cropBoundary));
                Rect rect = new Rect(0, (int) (fractionToVerticalPixels - CropView.this.mCropTouchMargin), CropView.this.getWidth(), (int) (fractionToVerticalPixels + CropView.this.mCropTouchMargin));
                int i = rect.top;
                if (i >= 0) {
                    return rect;
                }
                rect.offset(0, -i);
                return rect;
            }
            CropView cropView2 = CropView.this;
            float fractionToHorizontalPixels = (float) cropView2.fractionToHorizontalPixels(cropView2.getBoundaryPosition(cropBoundary));
            CropView cropView3 = CropView.this;
            CropView cropView4 = CropView.this;
            return new Rect((int) (fractionToHorizontalPixels - CropView.this.mCropTouchMargin), (int) (((float) cropView3.fractionToVerticalPixels(cropView3.mCrop.top)) + CropView.this.mCropTouchMargin), (int) (fractionToHorizontalPixels + CropView.this.mCropTouchMargin), (int) (((float) cropView4.fractionToVerticalPixels(cropView4.mCrop.bottom)) - CropView.this.mCropTouchMargin));
        }

        private void setNodePosition(Rect rect, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            int[] iArr = new int[2];
            CropView.this.getLocationOnScreen(iArr);
            rect.offset(iArr[0], iArr[1]);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.android.systemui.screenshot.CropView.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        RectF mCrop;

        /* synthetic */ SavedState(Parcel parcel, AnonymousClass1 r2) {
            this(parcel);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mCrop = (RectF) parcel.readParcelable(ClassLoader.getSystemClassLoader());
        }

        @Override // android.view.View.BaseSavedState, android.os.Parcelable, android.view.AbsSavedState
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeParcelable(this.mCrop, 0);
        }
    }
}
