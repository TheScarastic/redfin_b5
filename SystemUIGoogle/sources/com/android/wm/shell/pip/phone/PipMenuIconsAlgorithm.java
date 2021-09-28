package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
/* loaded from: classes2.dex */
public class PipMenuIconsAlgorithm {
    protected View mDismissButton;
    protected View mDragHandle;
    protected View mSettingsButton;
    protected ViewGroup mTopEndContainer;
    protected ViewGroup mViewRoot;

    public void onBoundsChanged(Rect rect) {
    }

    /* access modifiers changed from: protected */
    public PipMenuIconsAlgorithm(Context context) {
    }

    public void bindViews(ViewGroup viewGroup, ViewGroup viewGroup2, View view, View view2, View view3) {
        this.mViewRoot = viewGroup;
        this.mTopEndContainer = viewGroup2;
        this.mDragHandle = view;
        this.mSettingsButton = view2;
        this.mDismissButton = view3;
        bindInitialViewState();
    }

    protected static void setLayoutGravity(View view, int i) {
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            layoutParams.gravity = i;
            view.setLayoutParams(layoutParams);
        }
    }

    private void bindInitialViewState() {
        ViewGroup viewGroup;
        View view;
        if (this.mViewRoot == null || (viewGroup = this.mTopEndContainer) == null || this.mDragHandle == null || (view = this.mSettingsButton) == null || this.mDismissButton == null) {
            Log.e("PipMenuIconsAlgorithm", "One of the required views is null.");
            return;
        }
        viewGroup.removeView(view);
        this.mViewRoot.addView(this.mSettingsButton);
        setLayoutGravity(this.mDragHandle, 8388659);
        setLayoutGravity(this.mSettingsButton, 8388659);
    }
}
