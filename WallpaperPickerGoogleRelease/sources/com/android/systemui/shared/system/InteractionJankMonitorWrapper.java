package com.android.systemui.shared.system;

import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class InteractionJankMonitorWrapper {
    public static final int CUJ_ALL_APPS_SCROLL = 26;
    public static final int CUJ_APP_CLOSE_TO_HOME = 9;
    public static final int CUJ_APP_CLOSE_TO_PIP = 10;
    public static final int CUJ_APP_LAUNCH_FROM_ICON = 8;
    public static final int CUJ_APP_LAUNCH_FROM_RECENTS = 7;
    public static final int CUJ_APP_LAUNCH_FROM_WIDGET = 27;
    public static final int CUJ_OPEN_ALL_APPS = 25;
    public static final int CUJ_QUICK_SWITCH = 11;
    private static final String TAG = "JankMonitorWrapper";

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface CujType {
    }

    public static void begin(View view, int i) {
        InteractionJankMonitor.getInstance().begin(view, i);
    }

    public static void cancel(int i) {
        InteractionJankMonitor.getInstance().cancel(i);
    }

    public static void end(int i) {
        InteractionJankMonitor.getInstance().end(i);
    }

    public static void begin(View view, int i, long j) {
        InteractionJankMonitor.getInstance().begin(new InteractionJankMonitor.Configuration.Builder(i).setView(view).setTimeout(j));
    }
}
