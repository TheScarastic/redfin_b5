package com.android.wallpaper.compat;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.ParcelFileDescriptor;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class WallpaperManagerCompatV16 {
    public static WallpaperManagerCompatV16 sInstance;
    public static final Object sInstanceLock = new Object();
    public WallpaperManager mWallpaperManager;

    @SuppressLint({"ServiceCast"})
    public WallpaperManagerCompatV16(Context context) {
        this.mWallpaperManager = (WallpaperManager) context.getSystemService("wallpaper");
    }

    public static WallpaperManagerCompatV16 getInstance(Context context) {
        WallpaperManagerCompatV16 wallpaperManagerCompatV16;
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                if (BuildCompat.isAtLeastN()) {
                    sInstance = new WallpaperManagerCompatVN(context.getApplicationContext());
                } else {
                    sInstance = new WallpaperManagerCompatV16(context.getApplicationContext());
                }
            }
            wallpaperManagerCompatV16 = sInstance;
        }
        return wallpaperManagerCompatV16;
    }

    public Drawable getDrawable() {
        try {
            return this.mWallpaperManager.getDrawable();
        } catch (Exception unused) {
            return this.mWallpaperManager.getBuiltInDrawable();
        }
    }

    public ParcelFileDescriptor getWallpaperFile(int i) {
        return null;
    }

    public int getWallpaperId(int i) {
        throw new UnsupportedOperationException("This method should not be called on pre-N versions of Android.");
    }

    public int setBitmap(Bitmap bitmap, Rect rect, boolean z, int i) throws IOException {
        this.mWallpaperManager.setBitmap(bitmap);
        return 1;
    }

    public int setStream(InputStream inputStream, Rect rect, boolean z, int i) throws IOException {
        this.mWallpaperManager.setStream(inputStream);
        return 1;
    }
}
