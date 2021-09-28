package com.android.systemui.shared.recents.view;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.AppTransitionAnimationSpec;
/* loaded from: classes.dex */
public class AppTransitionAnimationSpecCompat {
    private Bitmap mBuffer;
    private Rect mRect;
    private int mTaskId;

    public AppTransitionAnimationSpecCompat(int i, Bitmap bitmap, Rect rect) {
        this.mTaskId = i;
        this.mBuffer = bitmap;
        this.mRect = rect;
    }

    public AppTransitionAnimationSpec toAppTransitionAnimationSpec() {
        int i = this.mTaskId;
        Bitmap bitmap = this.mBuffer;
        return new AppTransitionAnimationSpec(i, bitmap != null ? bitmap.getHardwareBuffer() : null, this.mRect);
    }
}
