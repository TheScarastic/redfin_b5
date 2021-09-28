package com.android.wm.shell.onehanded;

import android.graphics.Rect;
/* loaded from: classes2.dex */
public interface OneHandedTransitionCallback {
    default void onStartFinished(Rect rect) {
    }

    default void onStartTransition(boolean z) {
    }

    default void onStopFinished(Rect rect) {
    }
}
