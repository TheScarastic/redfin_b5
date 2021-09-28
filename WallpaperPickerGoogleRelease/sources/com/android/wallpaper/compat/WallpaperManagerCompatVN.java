package com.android.wallpaper.compat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class WallpaperManagerCompatVN extends WallpaperManagerCompatV16 {
    public WallpaperManagerCompatVN(Context context) {
        super(context);
    }

    @Override // com.android.wallpaper.compat.WallpaperManagerCompatV16
    public ParcelFileDescriptor getWallpaperFile(int i) {
        try {
            return this.mWallpaperManager.getWallpaperFile(i);
        } catch (Exception e) {
            Log.e("WallpaperMgrCompatVN", "Exception on getWallpaperFile", e);
            return null;
        }
    }

    @Override // com.android.wallpaper.compat.WallpaperManagerCompatV16
    public int getWallpaperId(int i) {
        return this.mWallpaperManager.getWallpaperId(i);
    }

    @Override // com.android.wallpaper.compat.WallpaperManagerCompatV16
    public int setBitmap(Bitmap bitmap, Rect rect, boolean z, int i) throws IOException {
        return this.mWallpaperManager.setBitmap(bitmap, null, z, i);
    }

    @Override // com.android.wallpaper.compat.WallpaperManagerCompatV16
    public int setStream(InputStream inputStream, Rect rect, boolean z, int i) throws IOException {
        return this.mWallpaperManager.setStream(inputStream, null, z, i);
    }
}
