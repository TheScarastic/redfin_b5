package com.android.wallpaper.module;

import com.android.wallpaper.model.WallpaperInfo;
/* loaded from: classes.dex */
public interface WallpaperPersister {

    /* loaded from: classes.dex */
    public interface SetWallpaperCallback {
        void onError(Throwable th);

        void onSuccess(WallpaperInfo wallpaperInfo);
    }
}
