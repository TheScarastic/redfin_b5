package com.android.wallpaper.module;

import android.content.Context;
import com.android.wallpaper.asset.BitmapCachingAsset$$ExternalSyntheticLambda1;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.CurrentWallpaperInfoFactory;
/* loaded from: classes.dex */
public class DefaultCurrentWallpaperInfoFactory implements CurrentWallpaperInfoFactory {
    public WallpaperInfo mHomeWallpaper;
    public final NoOpUserEventLogger mLiveWallpaperInfoFactory;
    public WallpaperInfo mLockWallpaper;
    public int mPresentationMode;
    public final WallpaperRefresher mWallpaperRefresher;

    public DefaultCurrentWallpaperInfoFactory(Context context) {
        Context applicationContext = context.getApplicationContext();
        Injector injector = InjectorProvider.getInjector();
        this.mWallpaperRefresher = injector.getWallpaperRefresher(applicationContext);
        this.mLiveWallpaperInfoFactory = injector.getLiveWallpaperInfoFactory(applicationContext);
    }

    public synchronized void createCurrentWallpaperInfos(CurrentWallpaperInfoFactory.WallpaperInfoCallback wallpaperInfoCallback, boolean z) {
        int i;
        if (!z) {
            WallpaperInfo wallpaperInfo = this.mHomeWallpaper;
            if (!(wallpaperInfo == null || (i = this.mPresentationMode) == 2)) {
                wallpaperInfoCallback.onWallpaperInfoCreated(wallpaperInfo, this.mLockWallpaper, i);
                return;
            }
        }
        if (z) {
            this.mHomeWallpaper = null;
            this.mLockWallpaper = null;
        }
        ((DefaultWallpaperRefresher) this.mWallpaperRefresher).refresh(new BitmapCachingAsset$$ExternalSyntheticLambda1(this, wallpaperInfoCallback));
    }
}
