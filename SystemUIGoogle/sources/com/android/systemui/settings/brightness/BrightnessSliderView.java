package com.android.systemui.settings.brightness;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import com.android.settingslib.RestrictedLockUtils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class BrightnessSliderView extends FrameLayout {
    private DispatchTouchEventListener mListener;
    private Gefingerpoken mOnInterceptListener;
    private Drawable mProgressDrawable;
    private float mScale;
    private ToggleSeekBar mSlider;

    /* access modifiers changed from: package-private */
    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface DispatchTouchEventListener {
        boolean onDispatchTouchEvent(MotionEvent motionEvent);
    }

    public BrightnessSliderView(Context context) {
        this(context, null);
    }

    public BrightnessSliderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mScale = 1.0f;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        ToggleSeekBar toggleSeekBar = (ToggleSeekBar) requireViewById(R$id.slider);
        this.mSlider = toggleSeekBar;
        toggleSeekBar.setAccessibilityLabel(getContentDescription().toString());
        try {
            this.mProgressDrawable = ((LayerDrawable) ((DrawableWrapper) ((LayerDrawable) this.mSlider.getProgressDrawable()).findDrawableByLayerId(16908301)).getDrawable()).findDrawableByLayerId(R$id.slider_foreground);
        } catch (Exception unused) {
        }
    }

    public void setOnDispatchTouchEventListener(DispatchTouchEventListener dispatchTouchEventListener) {
        this.mListener = dispatchTouchEventListener;
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        DispatchTouchEventListener dispatchTouchEventListener = this.mListener;
        if (dispatchTouchEventListener != null) {
            dispatchTouchEventListener.onDispatchTouchEvent(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewParent, android.view.ViewGroup
    public void requestDisallowInterceptTouchEvent(boolean z) {
        ViewParent viewParent = ((FrameLayout) this).mParent;
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(z);
        }
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mSlider.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public void setEnforcedAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        this.mSlider.setEnabled(enforcedAdmin == null);
        this.mSlider.setEnforcedAdmin(enforcedAdmin);
    }

    public int getMax() {
        return this.mSlider.getMax();
    }

    public void setMax(int i) {
        this.mSlider.setMax(i);
    }

    public void setValue(int i) {
        this.mSlider.setProgress(i);
    }

    public int getValue() {
        return this.mSlider.getProgress();
    }

    public void setOnInterceptListener(Gefingerpoken gefingerpoken) {
        this.mOnInterceptListener = gefingerpoken;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.mOnInterceptListener;
        if (gefingerpoken != null) {
            return gefingerpoken.onInterceptTouchEvent(motionEvent);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        applySliderScale();
    }

    public void setSliderScaleY(float f) {
        if (f != this.mScale) {
            this.mScale = f;
            applySliderScale();
        }
    }

    private void applySliderScale() {
        Drawable drawable = this.mProgressDrawable;
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            int intrinsicHeight = (int) (((float) this.mProgressDrawable.getIntrinsicHeight()) * this.mScale);
            int intrinsicHeight2 = (this.mProgressDrawable.getIntrinsicHeight() - intrinsicHeight) / 2;
            this.mProgressDrawable.setBounds(bounds.left, intrinsicHeight2, bounds.right, intrinsicHeight + intrinsicHeight2);
        }
    }

    public float getSliderScaleY() {
        return this.mScale;
    }
}
