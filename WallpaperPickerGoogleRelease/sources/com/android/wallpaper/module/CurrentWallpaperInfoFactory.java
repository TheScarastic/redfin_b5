package com.android.wallpaper.module;

import com.android.wallpaper.model.WallpaperInfo;
/* loaded from: classes.dex */
public interface CurrentWallpaperInfoFactory {

    /* loaded from: classes.dex */
    public interface WallpaperInfoCallback {
        void onWallpaperInfoCreated(WallpaperInfo wallpaperInfo, WallpaperInfo wallpaperInfo2, int i);
    }
}
