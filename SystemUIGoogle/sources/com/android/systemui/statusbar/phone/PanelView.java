package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.statusbar.phone.PanelViewController;
/* loaded from: classes.dex */
public abstract class PanelView extends FrameLayout {
    public static final String TAG = PanelView.class.getSimpleName();
    private OnConfigurationChangedListener mOnConfigurationChangedListener;
    private PanelViewController.TouchHandler mTouchHandler;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnConfigurationChangedListener {
        void onConfigurationChanged(Configuration configuration);
    }

    public PanelView(Context context) {
        super(context);
    }

    public PanelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PanelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PanelView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setOnTouchListener(PanelViewController.TouchHandler touchHandler) {
        super.setOnTouchListener((View.OnTouchListener) touchHandler);
        this.mTouchHandler = touchHandler;
    }

    public void setOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.mOnConfigurationChangedListener = onConfigurationChangedListener;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mTouchHandler.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View, android.view.ViewGroup
    public void dispatchConfigurationChanged(Configuration configuration) {
        super.dispatchConfigurationChanged(configuration);
        this.mOnConfigurationChangedListener.onConfigurationChanged(configuration);
    }
}
