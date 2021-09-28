package com.android.wallpaper.module;

import com.android.wallpaper.model.WallpaperMetadata;
/* loaded from: classes.dex */
public interface WallpaperRefresher {

    /* loaded from: classes.dex */
    public interface RefreshListener {
        void onRefreshed(WallpaperMetadata wallpaperMetadata, WallpaperMetadata wallpaperMetadata2, int i);
    }
}
