package com.android.wallpaper.backup;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.android.wallpaper.compat.BuildCompat;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperPreferences;
@SuppressLint({"ServiceCast"})
/* loaded from: classes.dex */
public class MissingHashCodeGenerator extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED") && BuildCompat.isAtLeastN() && WallpaperManager.getInstance(context).isWallpaperSupported()) {
            WallpaperPreferences preferences = InjectorProvider.getInjector().getPreferences(context);
            if (preferences.getHomeWallpaperHashCode() == 0 || preferences.getLockWallpaperHashCode() == 0) {
                int i = MissingHashCodeGeneratorJobService.$r8$clinit;
                ((JobScheduler) context.getSystemService("jobscheduler")).schedule(new JobInfo.Builder(2, new ComponentName(context, MissingHashCodeGeneratorJobService.class)).setMinimumLatency(0).setPersisted(true).build());
            }
        }
    }
}
