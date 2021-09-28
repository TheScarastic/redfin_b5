package com.android.systemui.recents;

import android.content.Context;
import android.content.res.Configuration;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public interface RecentsImplementation {
    default void cancelPreloadRecentApps() {
    }

    default void dump(PrintWriter printWriter) {
    }

    default void hideRecentApps(boolean z, boolean z2) {
    }

    default void onAppTransitionFinished() {
    }

    default void onBootCompleted() {
    }

    default void onConfigurationChanged(Configuration configuration) {
    }

    default void onStart(Context context) {
    }

    default void preloadRecentApps() {
    }

    default void showRecentApps(boolean z) {
    }

    default void toggleRecentApps() {
    }
}
