package com.google.android.apps.wallpaper.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
/* loaded from: classes.dex */
public class DockedClockReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("com.google.android.apps.wallpaper.SET_DOCKED_CLOCK_FACE".equals(intent.getAction())) {
            Settings.Secure.putString(context.getContentResolver(), "docked_clock_face", intent.getStringExtra("clock_face_name"));
        }
    }
}
