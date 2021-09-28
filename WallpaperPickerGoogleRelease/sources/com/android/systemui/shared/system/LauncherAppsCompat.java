package com.android.systemui.shared.system;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.LauncherApps;
import android.os.Bundle;
import android.os.UserHandle;
/* loaded from: classes.dex */
public abstract class LauncherAppsCompat {
    public static PendingIntent getMainActivityLaunchIntent(LauncherApps launcherApps, ComponentName componentName, Bundle bundle, UserHandle userHandle) {
        return launcherApps.getMainActivityLaunchIntent(componentName, bundle, userHandle);
    }
}
