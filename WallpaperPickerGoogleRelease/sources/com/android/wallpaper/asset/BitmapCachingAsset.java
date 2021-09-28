package com.android.wallpaper.asset;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.LruCache;
import android.widget.ImageView;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import java.util.Objects;
/* loaded from: classes.dex */
public class BitmapCachingAsset extends Asset {
    public static LruCache<CacheKey, Bitmap> sCache = new LruCache<CacheKey, Bitmap>(104857600) { // from class: com.android.wallpaper.asset.BitmapCachingAsset.1
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
        @Override // android.util.LruCache
        public int sizeOf(CacheKey cacheKey, Bitmap bitmap) {
            return bitmap.getByteCount();
        }
    };
    public final boolean mIsLowRam;
    public final Asset mOriginalAsset;

    public BitmapCachingAsset(Context context, Asset asset) {
        this.mOriginalAsset = asset instanceof BitmapCachingAsset ? ((BitmapCachingAsset) asset).mOriginalAsset : asset;
        this.mIsLowRam = ((ActivityManager) context.getApplicationContext().getSystemService("activity")).isLowRamDevice();
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeBitmap(int i, int i2, Asset.BitmapReceiver bitmapReceiver) {
        if (this.mIsLowRam) {
            this.mOriginalAsset.decodeBitmap(i, i2, new PreviewPager$$ExternalSyntheticLambda1(bitmapReceiver));
            return;
        }
        CacheKey cacheKey = new CacheKey(this.mOriginalAsset, i, i2);
        Bitmap bitmap = sCache.get(cacheKey);
        if (bitmap != null) {
            bitmapReceiver.onBitmapDecoded(bitmap);
        } else {
            this.mOriginalAsset.decodeBitmap(i, i2, new BitmapCachingAsset$$ExternalSyntheticLambda1(cacheKey, bitmapReceiver));
        }
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeBitmapRegion(Rect rect, int i, int i2, boolean z, Asset.BitmapReceiver bitmapReceiver) {
        if (this.mIsLowRam) {
            this.mOriginalAsset.decodeBitmapRegion(rect, i, i2, z, bitmapReceiver);
            return;
        }
        CacheKey cacheKey = new CacheKey(this.mOriginalAsset, i, i2, z, rect);
        Bitmap bitmap = sCache.get(cacheKey);
        if (bitmap != null) {
            bitmapReceiver.onBitmapDecoded(bitmap);
        } else {
            this.mOriginalAsset.decodeBitmapRegion(rect, i, i2, z, new BitmapCachingAsset$$ExternalSyntheticLambda0(cacheKey, bitmapReceiver));
        }
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeRawDimensions(Activity activity, Asset.DimensionsReceiver dimensionsReceiver) {
        this.mOriginalAsset.decodeRawDimensions(activity, dimensionsReceiver);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadPreviewImage(Activity activity, ImageView imageView, int i) {
        this.mOriginalAsset.loadPreviewImage(activity, imageView, i);
    }

    /* loaded from: classes.dex */
    public static class CacheKey {
        public final Asset mAsset;
        public final int mHeight;
        public final Rect mRect;
        public final boolean mRtl;
        public final int mWidth;

        public CacheKey(Asset asset, int i, int i2) {
            this.mAsset = asset;
            this.mWidth = i;
            this.mHeight = i2;
            this.mRtl = false;
            this.mRect = null;
        }

        public boolean equals(Object obj) {
            if (obj instanceof CacheKey) {
                CacheKey cacheKey = (CacheKey) obj;
                if (Objects.equals(this.mAsset, cacheKey.mAsset) && cacheKey.mWidth == this.mWidth && cacheKey.mHeight == this.mHeight && cacheKey.mRtl == this.mRtl && Objects.equals(this.mRect, cacheKey.mRect)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.mAsset, Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight));
        }

        public CacheKey(Asset asset, int i, int i2, boolean z, Rect rect) {
            this.mAsset = asset;
            this.mWidth = i;
            this.mHeight = i2;
            this.mRtl = z;
            this.mRect = rect;
        }
    }
}
