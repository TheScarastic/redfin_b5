package com.android.systemui.dagger;

import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
/* loaded from: classes.dex */
public class GlobalModule {
    public DisplayMetrics provideDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /* access modifiers changed from: package-private */
    public static UiEventLogger provideUiEventLogger() {
        return new UiEventLoggerImpl();
    }

    /* access modifiers changed from: package-private */
    public static boolean provideIsTestHarness() {
        return ActivityManager.isRunningInUserTestHarness();
    }
}
