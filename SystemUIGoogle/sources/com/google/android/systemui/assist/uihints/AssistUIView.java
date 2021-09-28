package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
/* loaded from: classes2.dex */
public class AssistUIView extends FrameLayout {
    private TouchOutsideHandler mTouchOutside;

    public AssistUIView(Context context) {
        this(context, null);
    }

    public AssistUIView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AssistUIView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public AssistUIView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setClipChildren(false);
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        TouchOutsideHandler touchOutsideHandler;
        if (motionEvent.getAction() != 4 || (touchOutsideHandler = this.mTouchOutside) == null) {
            return super.dispatchTouchEvent(motionEvent);
        }
        touchOutsideHandler.onTouchOutside();
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setTouchOutside(TouchOutsideHandler touchOutsideHandler) {
        this.mTouchOutside = touchOutsideHandler;
    }
}
