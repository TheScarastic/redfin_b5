package com.google.android.apps.wallpaper.backdrop;

import com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotationInitializer;
/* loaded from: classes.dex */
public abstract class WallpaperRotationInitializerFactory {
    public static WallpaperRotationInitializerFactory sInstance;

    public static synchronized WallpaperRotationInitializerFactory getInstance() {
        WallpaperRotationInitializerFactory wallpaperRotationInitializerFactory;
        synchronized (WallpaperRotationInitializerFactory.class) {
            if (sInstance == null) {
                sInstance = new BackdropWallpaperRotationInitializer.Factory();
            }
            wallpaperRotationInitializerFactory = sInstance;
        }
        return wallpaperRotationInitializerFactory;
    }
}
