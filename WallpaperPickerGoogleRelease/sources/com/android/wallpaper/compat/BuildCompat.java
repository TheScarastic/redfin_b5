package com.android.wallpaper.compat;

import android.os.Build;
/* loaded from: classes.dex */
public class BuildCompat {
    public static int sSdk = Build.VERSION.SDK_INT;

    public static boolean isAtLeastN() {
        return sSdk >= 24;
    }
}
