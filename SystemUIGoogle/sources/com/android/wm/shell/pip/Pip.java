package com.android.wm.shell.pip;

import android.content.res.Configuration;
import android.graphics.Rect;
import java.io.PrintWriter;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public interface Pip {
    default IPip createExternalInterface() {
        return null;
    }

    default void dump(PrintWriter printWriter) {
    }

    default void hidePipMenu(Runnable runnable, Runnable runnable2) {
    }

    default void onConfigurationChanged(Configuration configuration) {
    }

    default void onDensityOrFontScaleChanged() {
    }

    default void onOverlayChanged() {
    }

    default void onSystemUiStateChanged(boolean z, int i) {
    }

    default void registerSessionListenerForCurrentUser() {
    }

    default void setPinnedStackAnimationType(int i) {
    }

    default void setPipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
    }

    default void showPictureInPictureMenu() {
    }
}
