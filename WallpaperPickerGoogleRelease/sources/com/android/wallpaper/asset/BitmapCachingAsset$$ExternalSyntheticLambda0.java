package com.android.wallpaper.asset;

import android.app.WallpaperColors;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.BitmapCachingAsset;
import com.android.wallpaper.asset.StreamableAsset;
import com.android.wallpaper.widget.WallpaperColorsLoader;
import com.google.android.apps.wallpaper.picker.MicropaperPreviewFragmentGoogle;
import com.google.common.io.ByteStreams;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
/* loaded from: classes.dex */
public final /* synthetic */ class BitmapCachingAsset$$ExternalSyntheticLambda0 implements Asset.BitmapReceiver, StreamableAsset.StreamReceiver {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BitmapCachingAsset$$ExternalSyntheticLambda0(Asset asset, WallpaperColorsLoader.Callback callback) {
        this.f$0 = asset;
        this.f$1 = callback;
    }

    public /* synthetic */ BitmapCachingAsset$$ExternalSyntheticLambda0(BitmapCachingAsset.CacheKey cacheKey, Asset.BitmapReceiver bitmapReceiver) {
        this.f$0 = cacheKey;
        this.f$1 = bitmapReceiver;
    }

    public /* synthetic */ BitmapCachingAsset$$ExternalSyntheticLambda0(SettableFuture settableFuture, OutputStream outputStream) {
        this.f$0 = settableFuture;
        this.f$1 = outputStream;
    }

    @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
    public void onBitmapDecoded(Bitmap bitmap) {
        switch (this.$r8$classId) {
            case 0:
                BitmapCachingAsset.CacheKey cacheKey = (BitmapCachingAsset.CacheKey) this.f$0;
                Asset.BitmapReceiver bitmapReceiver = (Asset.BitmapReceiver) this.f$1;
                if (bitmap != null) {
                    BitmapCachingAsset.sCache.put(cacheKey, bitmap);
                }
                bitmapReceiver.onBitmapDecoded(bitmap);
                return;
            default:
                Asset asset = (Asset) this.f$0;
                WallpaperColorsLoader.Callback callback = (WallpaperColorsLoader.Callback) this.f$1;
                LruCache<Asset, WallpaperColors> lruCache = WallpaperColorsLoader.sCache;
                if (bitmap != null) {
                    boolean z = false;
                    if (bitmap.getConfig() == Bitmap.Config.HARDWARE) {
                        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                        z = true;
                    }
                    WallpaperColors fromBitmap = WallpaperColors.fromBitmap(bitmap);
                    WallpaperColorsLoader.sCache.put(asset, fromBitmap);
                    callback.onLoaded(fromBitmap);
                    if (z) {
                        bitmap.recycle();
                        return;
                    }
                    return;
                }
                Log.i("WallpaperColorsLoader", "Can't get wallpaper colors from a null bitmap, uses null color.");
                callback.onLoaded(null);
                return;
        }
    }

    @Override // com.android.wallpaper.asset.StreamableAsset.StreamReceiver
    public void onInputStreamOpened(InputStream inputStream) {
        SettableFuture settableFuture = (SettableFuture) this.f$0;
        OutputStream outputStream = (OutputStream) this.f$1;
        int i = MicropaperPreviewFragmentGoogle.$r8$clinit;
        if (inputStream == null) {
            settableFuture.setException(new IllegalStateException("Input stream could not beopened in copyAssetToLocalFile"));
            return;
        }
        try {
            Object valueOf = Long.valueOf(ByteStreams.copy(inputStream, outputStream));
            Objects.requireNonNull(settableFuture);
            if (valueOf == null) {
                valueOf = AbstractFuture.NULL;
            }
            if (AbstractFuture.ATOMIC_HELPER.casValue(settableFuture, null, valueOf)) {
                AbstractFuture.complete(settableFuture);
            }
        } catch (IOException e) {
            settableFuture.setException(e);
        }
    }
}
