package com.android.wallpaper.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.wallpaper.compat.BuildCompat;
import com.android.wallpaper.util.DiskBasedLogger;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda1;
/* loaded from: classes.dex */
public class RotationWallpaperUpdateReceiver extends BroadcastReceiver {
    public static final /* synthetic */ int $r8$clinit = 0;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null || ((!intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED") && !intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) || !BuildCompat.isAtLeastN())) {
            DiskBasedLogger.e("RotationWallpaperUpdateReceiver", "Unexpected action or Android version!", context);
            throw new IllegalStateException("Unexpected broadcast action or unsupported Android version");
        } else {
            new Thread(new PreviewUtils$$ExternalSyntheticLambda1(this, context, goAsync())).start();
        }
    }
}
