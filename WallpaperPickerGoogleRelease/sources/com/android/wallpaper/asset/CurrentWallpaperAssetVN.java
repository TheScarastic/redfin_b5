package com.android.wallpaper.asset;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.ParcelFileDescriptor;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import android.widget.ImageView;
import com.android.wallpaper.compat.WallpaperManagerCompatV16;
import com.android.wallpaper.util.WallpaperCropUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestOptions;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;
/* loaded from: classes.dex */
public class CurrentWallpaperAssetVN extends StreamableAsset {
    public int mWallpaperId;
    public WallpaperManager mWallpaperManager;
    public WallpaperManagerCompatV16 mWallpaperManagerCompat;
    public int mWallpaperManagerFlag;

    /* loaded from: classes.dex */
    public static final class CurrentWallpaperVNKey implements Key {
        public int mWallpaperFlag;
        public WallpaperManager mWallpaperManager;

        public CurrentWallpaperVNKey(WallpaperManager wallpaperManager, int i) {
            this.mWallpaperManager = wallpaperManager;
            this.mWallpaperFlag = i;
        }

        @Override // com.bumptech.glide.load.Key
        public boolean equals(Object obj) {
            if (obj instanceof CurrentWallpaperVNKey) {
                return getCacheKey().equals(((CurrentWallpaperVNKey) obj).getCacheKey());
            }
            return false;
        }

        public final String getCacheKey() {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("CurrentWallpaperVNKey{flag=");
            m.append(this.mWallpaperFlag);
            m.append(",id=");
            m.append(this.mWallpaperManager.getWallpaperId(this.mWallpaperFlag));
            m.append('}');
            return m.toString();
        }

        @Override // com.bumptech.glide.load.Key
        public int hashCode() {
            return getCacheKey().hashCode();
        }

        public String toString() {
            return getCacheKey();
        }

        @Override // com.bumptech.glide.load.Key
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(getCacheKey().getBytes(Key.CHARSET));
        }
    }

    public CurrentWallpaperAssetVN(Context context, int i) {
        this.mWallpaperManager = WallpaperManager.getInstance(context);
        WallpaperManagerCompatV16 instance = WallpaperManagerCompatV16.getInstance(context);
        this.mWallpaperManagerCompat = instance;
        this.mWallpaperManagerFlag = i;
        this.mWallpaperId = instance.getWallpaperId(i);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void adjustCropRect(Context context, Point point, Rect rect) {
        rect.offsetTo(0, 0);
        WallpaperCropUtils.adjustCropRect(context, rect, true);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CurrentWallpaperAssetVN)) {
            return false;
        }
        CurrentWallpaperAssetVN currentWallpaperAssetVN = (CurrentWallpaperAssetVN) obj;
        if (currentWallpaperAssetVN.mWallpaperManagerFlag == this.mWallpaperManagerFlag && currentWallpaperAssetVN.mWallpaperId == this.mWallpaperId) {
            return true;
        }
        return false;
    }

    @Override // com.android.wallpaper.asset.Asset
    public Bitmap getLowResBitmap(Context context) {
        try {
            RequestBuilder<Bitmap> asBitmap = Glide.with(context).asBitmap();
            asBitmap.model = this;
            asBitmap.isModelSet = true;
            return (Bitmap) ((RequestFutureTarget) asBitmap.submit()).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("CurrentWallpaperAssetVN", "Couldn't obtain low res bitmap", e);
            return null;
        }
    }

    public int hashCode() {
        return ((527 + this.mWallpaperManagerFlag) * 31) + this.mWallpaperId;
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadDrawable(Context context, ImageView imageView, int i) {
        RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
        asDrawable.model = this;
        asDrawable.isModelSet = true;
        RequestBuilder<Drawable> apply = asDrawable.apply((BaseRequestOptions<?>) RequestOptions.centerCropTransform());
        apply.transition(DrawableTransitionOptions.withCrossFade());
        apply.into(imageView);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadLowResDrawable(Activity activity, ImageView imageView, int i, BitmapTransformation bitmapTransformation) {
        MultiTransformation multiTransformation = new MultiTransformation(new FitCenter(), bitmapTransformation);
        RequestBuilder<Drawable> asDrawable = Glide.with(activity).asDrawable();
        asDrawable.model = this;
        asDrawable.isModelSet = true;
        asDrawable.apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(multiTransformation).placeholder(new ColorDrawable(i))).into(imageView);
    }

    @Override // com.android.wallpaper.asset.StreamableAsset
    public InputStream openInputStream() {
        ParcelFileDescriptor wallpaperFile = this.mWallpaperManagerCompat.getWallpaperFile(this.mWallpaperManagerFlag);
        if (wallpaperFile != null) {
            return new ParcelFileDescriptor.AutoCloseInputStream(wallpaperFile);
        }
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("ParcelFileDescriptor for wallpaper ");
        m.append(this.mWallpaperManagerFlag);
        m.append(" is null, unable to open InputStream.");
        Log.e("CurrentWallpaperAssetVN", m.toString());
        return null;
    }
}
