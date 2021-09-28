package com.android.wallpaper.asset;

import android.app.WallpaperManager;
import android.content.Context;
/* loaded from: classes.dex */
public class WallpaperModel {
    public WallpaperManager mWallpaperManager;
    public int mWallpaperSource;

    public WallpaperModel(Context context, int i) {
        this.mWallpaperSource = i;
        this.mWallpaperManager = WallpaperManager.getInstance(context);
    }
}
