package com.android.wallpaper.widget;

import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.Point;
import android.util.LruCache;
import android.view.WindowManager;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.BitmapCachingAsset;
import com.android.wallpaper.asset.BitmapCachingAsset$$ExternalSyntheticLambda0;
import com.android.wallpaper.util.ScreenSizeCalculator;
/* loaded from: classes.dex */
public class WallpaperColorsLoader {
    public static LruCache<Asset, WallpaperColors> sCache = new LruCache<>(6);

    /* loaded from: classes.dex */
    public interface Callback {
        void onLoaded(WallpaperColors wallpaperColors);
    }

    public static void getWallpaperColors(Context context, Asset asset, Callback callback) {
        WallpaperColors wallpaperColors = sCache.get(asset);
        if (wallpaperColors != null) {
            callback.onLoaded(wallpaperColors);
            return;
        }
        Point screenSize = ScreenSizeCalculator.getInstance().getScreenSize(((WindowManager) context.getSystemService(WindowManager.class)).getDefaultDisplay());
        new BitmapCachingAsset(context, asset).decodeBitmap(screenSize.y / 2, screenSize.x / 2, new BitmapCachingAsset$$ExternalSyntheticLambda0(asset, callback));
    }
}
