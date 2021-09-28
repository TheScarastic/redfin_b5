package com.google.android.apps.wallpaper.backdrop;

import android.content.Context;
/* loaded from: classes.dex */
public abstract class BackdropTaskScheduler {
    public static BackdropTaskScheduler sInstance;

    public static BackdropTaskScheduler getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new JobSchedulerBackdropTaskScheduler(context);
        }
        return sInstance;
    }

    public abstract void scheduleOneOffTask(int i, int i2);
}
