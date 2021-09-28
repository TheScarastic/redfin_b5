package com.android.systemui.settings.dagger;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.settings.UserTrackerImpl;
/* loaded from: classes.dex */
public abstract class SettingsModule {
    /* access modifiers changed from: package-private */
    public static UserTracker provideUserTracker(Context context, UserManager userManager, DumpManager dumpManager, Handler handler) {
        int currentUser = ActivityManager.getCurrentUser();
        UserTrackerImpl userTrackerImpl = new UserTrackerImpl(context, userManager, dumpManager, handler);
        userTrackerImpl.initialize(currentUser);
        return userTrackerImpl;
    }
}
