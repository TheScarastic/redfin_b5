package com.google.android.apps.wallpaper.asset;

import android.app.WallpaperInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.LiveWallpaperThumbAsset;
import com.android.wallpaper.module.DeviceColorLayerResolver;
import com.android.wallpaper.module.DrawableLayerResolver;
import com.android.wallpaper.module.InjectorProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.Objects;
/* loaded from: classes.dex */
public class GoogleLiveWallpaperThumbAsset extends LiveWallpaperThumbAsset {
    public DrawableLayerResolver mLayerResolver = InjectorProvider.getInjector().getDrawableLayerResolver();

    /* loaded from: classes.dex */
    public static class LoadThumbnailTask extends AsyncTask<Void, Void, Bitmap> {
        public WallpaperInfo mInfo;
        public final DrawableLayerResolver mLayerResolver;
        public final PackageManager mPackageManager;
        public Asset.BitmapReceiver mReceiver;

        public LoadThumbnailTask(Context context, WallpaperInfo wallpaperInfo, DrawableLayerResolver drawableLayerResolver, Asset.BitmapReceiver bitmapReceiver) {
            this.mInfo = wallpaperInfo;
            this.mReceiver = bitmapReceiver;
            this.mLayerResolver = drawableLayerResolver;
            this.mPackageManager = context.getPackageManager();
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void[] voidArr) {
            Drawable loadThumbnail = this.mInfo.loadThumbnail(this.mPackageManager);
            if (loadThumbnail instanceof BitmapDrawable) {
                return ((BitmapDrawable) loadThumbnail).getBitmap();
            }
            if (loadThumbnail instanceof LayerDrawable) {
                LayerDrawable layerDrawable = (LayerDrawable) loadThumbnail;
                Objects.requireNonNull((DeviceColorLayerResolver) this.mLayerResolver);
                Drawable drawable = layerDrawable.getDrawable(Math.min(DeviceColorLayerResolver.LAYER_INDEX, layerDrawable.getNumberOfLayers() - 1));
                if (drawable instanceof BitmapDrawable) {
                    return ((BitmapDrawable) drawable).getBitmap();
                }
            }
            return null;
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            this.mReceiver.onBitmapDecoded(bitmap);
        }
    }

    public GoogleLiveWallpaperThumbAsset(Context context, WallpaperInfo wallpaperInfo) {
        super(context, wallpaperInfo);
    }

    @Override // com.android.wallpaper.asset.LiveWallpaperThumbAsset, com.android.wallpaper.asset.Asset
    public void decodeBitmap(int i, int i2, Asset.BitmapReceiver bitmapReceiver) {
        new LoadThumbnailTask(this.mContext, this.mInfo, this.mLayerResolver, bitmapReceiver).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.android.wallpaper.asset.LiveWallpaperThumbAsset
    public Drawable getThumbnailDrawable() {
        Drawable loadThumbnail = this.mInfo.loadThumbnail(this.mContext.getPackageManager());
        if (!(loadThumbnail instanceof LayerDrawable)) {
            return loadThumbnail;
        }
        LayerDrawable layerDrawable = (LayerDrawable) loadThumbnail;
        Objects.requireNonNull((DeviceColorLayerResolver) this.mLayerResolver);
        return layerDrawable.getDrawable(Math.min(DeviceColorLayerResolver.LAYER_INDEX, layerDrawable.getNumberOfLayers() - 1));
    }

    @Override // com.android.wallpaper.asset.LiveWallpaperThumbAsset, com.android.wallpaper.asset.Asset
    public void loadDrawable(Context context, ImageView imageView, int i) {
        RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
        asDrawable.model = this;
        asDrawable.isModelSet = true;
        RequestBuilder<Drawable> apply = asDrawable.apply((BaseRequestOptions<?>) RequestOptions.centerCropTransform().placeholder(new ColorDrawable(i)));
        apply.transition(DrawableTransitionOptions.withCrossFade());
        apply.into(imageView);
    }
}
