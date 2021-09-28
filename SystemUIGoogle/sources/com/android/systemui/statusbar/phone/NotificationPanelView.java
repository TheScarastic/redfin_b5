package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class NotificationPanelView extends PanelView {
    private final Paint mAlphaPaint;
    private int mCurrentPanelAlpha;
    private boolean mDozing;
    private RtlChangeListener mRtlChangeListener;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface RtlChangeListener {
        void onRtlPropertielsChanged(int i);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    public NotificationPanelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mAlphaPaint = paint;
        setWillNotDraw(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        setBackgroundColor(0);
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) {
        RtlChangeListener rtlChangeListener = this.mRtlChangeListener;
        if (rtlChangeListener != null) {
            rtlChangeListener.onRtlPropertielsChanged(i);
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mCurrentPanelAlpha != 255) {
            canvas.drawRect(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), this.mAlphaPaint);
        }
    }

    /* access modifiers changed from: package-private */
    public float getCurrentPanelAlpha() {
        return (float) this.mCurrentPanelAlpha;
    }

    /* access modifiers changed from: package-private */
    public void setPanelAlphaInternal(float f) {
        int i = (int) f;
        this.mCurrentPanelAlpha = i;
        this.mAlphaPaint.setARGB(i, 255, 255, 255);
        invalidate();
    }

    public void setDozing(boolean z) {
        this.mDozing = z;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return !this.mDozing;
    }

    /* access modifiers changed from: package-private */
    public void setRtlChangeListener(RtlChangeListener rtlChangeListener) {
        this.mRtlChangeListener = rtlChangeListener;
    }

    public TapAgainView getTapAgainView() {
        return (TapAgainView) findViewById(R$id.shade_falsing_tap_again);
    }
}
