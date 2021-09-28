package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.systemui.R$layout;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class OverlayUiHost {
    private boolean mAttached = false;
    private boolean mFocusable = false;
    private WindowManager.LayoutParams mLayoutParams;
    private final AssistUIView mRoot;
    private final WindowManager mWindowManager;

    public OverlayUiHost(Context context, TouchOutsideHandler touchOutsideHandler) {
        AssistUIView assistUIView = (AssistUIView) LayoutInflater.from(context).inflate(R$layout.assist_ui, (ViewGroup) null, false);
        this.mRoot = assistUIView;
        assistUIView.setTouchOutside(touchOutsideHandler);
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    public ViewGroup getParent() {
        return this.mRoot;
    }

    /* access modifiers changed from: package-private */
    public void setAssistState(boolean z, boolean z2) {
        if (z && !this.mAttached) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 0, 0, 2024, 262952, -3);
            this.mLayoutParams = layoutParams;
            this.mFocusable = z2;
            layoutParams.gravity = 80;
            layoutParams.privateFlags = 64;
            layoutParams.setFitInsetsTypes(0);
            this.mLayoutParams.setTitle("Assist");
            this.mWindowManager.addView(this.mRoot, this.mLayoutParams);
            this.mAttached = true;
        } else if (!z && this.mAttached) {
            this.mWindowManager.removeViewImmediate(this.mRoot);
            this.mAttached = false;
        } else if (z && this.mFocusable != z2) {
            this.mWindowManager.updateViewLayout(this.mRoot, this.mLayoutParams);
            this.mFocusable = z2;
        }
    }
}
