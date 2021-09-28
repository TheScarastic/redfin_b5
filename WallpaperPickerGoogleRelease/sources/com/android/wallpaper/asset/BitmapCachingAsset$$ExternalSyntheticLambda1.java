package com.android.wallpaper.asset;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.android.customization.picker.WallpaperPreviewer;
import com.android.customization.picker.grid.GridFragment;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.BitmapCachingAsset;
import com.android.wallpaper.model.CurrentWallpaperInfoVN;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.model.WallpaperMetadata;
import com.android.wallpaper.module.CurrentWallpaperInfoFactory;
import com.android.wallpaper.module.DefaultCurrentWallpaperInfoFactory;
import com.android.wallpaper.module.ExploreIntentChecker;
import com.android.wallpaper.module.WallpaperRefresher;
import com.android.wallpaper.picker.PreviewFragment;
import com.android.wallpaper.picker.WallpaperInfoHelper$ExploreIntentReceiver;
import com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class BitmapCachingAsset$$ExternalSyntheticLambda1 implements Asset.BitmapReceiver, CurrentWallpaperInfoFactory.WallpaperInfoCallback, WallpaperRefresher.RefreshListener, WallpaperInfoHelper$ExploreIntentReceiver, ExploreIntentChecker.IntentReceiver {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BitmapCachingAsset$$ExternalSyntheticLambda1(GridFragment gridFragment, WallpaperPreviewer wallpaperPreviewer) {
        this.f$0 = gridFragment;
        this.f$1 = wallpaperPreviewer;
    }

    public /* synthetic */ BitmapCachingAsset$$ExternalSyntheticLambda1(BitmapCachingAsset.CacheKey cacheKey, Asset.BitmapReceiver bitmapReceiver) {
        this.f$0 = cacheKey;
        this.f$1 = bitmapReceiver;
    }

    public /* synthetic */ BitmapCachingAsset$$ExternalSyntheticLambda1(DefaultCurrentWallpaperInfoFactory defaultCurrentWallpaperInfoFactory, CurrentWallpaperInfoFactory.WallpaperInfoCallback wallpaperInfoCallback) {
        this.f$0 = defaultCurrentWallpaperInfoFactory;
        this.f$1 = wallpaperInfoCallback;
    }

    public /* synthetic */ BitmapCachingAsset$$ExternalSyntheticLambda1(PreviewFragment.WallpaperInfoContent wallpaperInfoContent, Runnable runnable) {
        this.f$0 = wallpaperInfoContent;
        this.f$1 = runnable;
    }

    public /* synthetic */ BitmapCachingAsset$$ExternalSyntheticLambda1(WallpaperInfoHelper$ExploreIntentReceiver wallpaperInfoHelper$ExploreIntentReceiver, CharSequence charSequence) {
        this.f$0 = wallpaperInfoHelper$ExploreIntentReceiver;
        this.f$1 = charSequence;
    }

    @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
    public void onBitmapDecoded(Bitmap bitmap) {
        BitmapCachingAsset.CacheKey cacheKey = (BitmapCachingAsset.CacheKey) this.f$0;
        Asset.BitmapReceiver bitmapReceiver = (Asset.BitmapReceiver) this.f$1;
        if (bitmap != null) {
            BitmapCachingAsset.sCache.put(cacheKey, bitmap);
        }
        bitmapReceiver.onBitmapDecoded(bitmap);
    }

    @Override // com.android.wallpaper.module.ExploreIntentChecker.IntentReceiver
    public void onIntentReceived(Intent intent) {
        BitmapCachingAsset$$ExternalSyntheticLambda1 bitmapCachingAsset$$ExternalSyntheticLambda1 = (BitmapCachingAsset$$ExternalSyntheticLambda1) ((WallpaperInfoHelper$ExploreIntentReceiver) this.f$0);
        PreviewFragment.WallpaperInfoContent wallpaperInfoContent = (PreviewFragment.WallpaperInfoContent) bitmapCachingAsset$$ExternalSyntheticLambda1.f$0;
        Runnable runnable = (Runnable) bitmapCachingAsset$$ExternalSyntheticLambda1.f$1;
        wallpaperInfoContent.mActionLabel = (CharSequence) this.f$1;
        wallpaperInfoContent.mExploreIntent = intent;
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override // com.android.wallpaper.module.WallpaperRefresher.RefreshListener
    public void onRefreshed(WallpaperMetadata wallpaperMetadata, WallpaperMetadata wallpaperMetadata2, int i) {
        WallpaperInfo wallpaperInfo;
        DefaultCurrentWallpaperInfoFactory defaultCurrentWallpaperInfoFactory = (DefaultCurrentWallpaperInfoFactory) this.f$0;
        CurrentWallpaperInfoFactory.WallpaperInfoCallback wallpaperInfoCallback = (CurrentWallpaperInfoFactory.WallpaperInfoCallback) this.f$1;
        Objects.requireNonNull(defaultCurrentWallpaperInfoFactory);
        android.app.WallpaperInfo wallpaperInfo2 = wallpaperMetadata.mWallpaperComponent;
        if (wallpaperInfo2 == null) {
            wallpaperInfo = new CurrentWallpaperInfoVN(wallpaperMetadata.mAttributions, wallpaperMetadata.mActionUrl, wallpaperMetadata.mActionLabelRes, wallpaperMetadata.mActionIconRes, wallpaperMetadata.mCollectionId, 1);
        } else {
            wallpaperInfo = defaultCurrentWallpaperInfoFactory.mLiveWallpaperInfoFactory.getLiveWallpaperInfo(wallpaperInfo2);
        }
        CurrentWallpaperInfoVN currentWallpaperInfoVN = null;
        if (wallpaperMetadata2 != null) {
            currentWallpaperInfoVN = new CurrentWallpaperInfoVN(wallpaperMetadata2.mAttributions, wallpaperMetadata2.mActionUrl, wallpaperMetadata2.mActionLabelRes, wallpaperMetadata2.mActionIconRes, wallpaperMetadata2.mCollectionId, 2);
        }
        defaultCurrentWallpaperInfoFactory.mHomeWallpaper = wallpaperInfo;
        defaultCurrentWallpaperInfoFactory.mLockWallpaper = currentWallpaperInfoVN;
        defaultCurrentWallpaperInfoFactory.mPresentationMode = i;
        wallpaperInfoCallback.onWallpaperInfoCreated(wallpaperInfo, currentWallpaperInfoVN, i);
    }

    @Override // com.android.wallpaper.module.CurrentWallpaperInfoFactory.WallpaperInfoCallback
    public void onWallpaperInfoCreated(WallpaperInfo wallpaperInfo, WallpaperInfo wallpaperInfo2, int i) {
        WallpaperPreviewer wallpaperPreviewer = (WallpaperPreviewer) this.f$1;
        Objects.requireNonNull((GridFragment) this.f$0);
        wallpaperPreviewer.mWallpaper = wallpaperInfo;
        wallpaperPreviewer.mWallpaperColorsListener = null;
        ImageView imageView = wallpaperPreviewer.mWallpaperSurfaceCallback.mHomeImageWallpaper;
        if (wallpaperInfo != null && imageView != null) {
            imageView.post(new PreviewUtils$$ExternalSyntheticLambda0(wallpaperPreviewer, imageView));
        }
    }
}
