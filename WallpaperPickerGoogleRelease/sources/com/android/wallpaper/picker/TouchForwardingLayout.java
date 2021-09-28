package com.android.wallpaper.picker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class TouchForwardingLayout extends FrameLayout {
    public boolean mForwardingEnabled;
    public View mView;

    public TouchForwardingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        View view = this.mView;
        if (view == null || !this.mForwardingEnabled) {
            return true;
        }
        view.dispatchTouchEvent(motionEvent);
        return true;
    }
}
