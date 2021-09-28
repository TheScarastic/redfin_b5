package com.android.wallpaper.asset;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import android.widget.ImageView;
import com.android.wallpaper.asset.Asset;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestOptions;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public class LiveWallpaperThumbAsset extends Asset {
    public final Context mContext;
    public final WallpaperInfo mInfo;
    public Drawable mThumbnailDrawable;
    public Uri mUri;

    /* loaded from: classes.dex */
    public static final class LiveWallpaperThumbKey implements Key {
        public WallpaperInfo mInfo;

        public LiveWallpaperThumbKey(WallpaperInfo wallpaperInfo) {
            this.mInfo = wallpaperInfo;
        }

        @Override // com.bumptech.glide.load.Key
        public boolean equals(Object obj) {
            if (!(obj instanceof LiveWallpaperThumbKey)) {
                return false;
            }
            return getCacheKey().equals(((LiveWallpaperThumbKey) obj).getCacheKey());
        }

        public final String getCacheKey() {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("LiveWallpaperThumbKey{packageName=");
            m.append(this.mInfo.getPackageName());
            m.append(",serviceName=");
            m.append(this.mInfo.getServiceName());
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

    /* loaded from: classes.dex */
    public static class LoadThumbnailTask extends AsyncTask<Void, Void, Bitmap> {
        public WallpaperInfo mInfo;
        public final PackageManager mPackageManager;
        public Asset.BitmapReceiver mReceiver;

        public LoadThumbnailTask(Context context, WallpaperInfo wallpaperInfo, Asset.BitmapReceiver bitmapReceiver) {
            this.mInfo = wallpaperInfo;
            this.mReceiver = bitmapReceiver;
            this.mPackageManager = context.getPackageManager();
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void[] voidArr) {
            Drawable loadThumbnail = this.mInfo.loadThumbnail(this.mPackageManager);
            if (loadThumbnail == null || !(loadThumbnail instanceof BitmapDrawable)) {
                return null;
            }
            return ((BitmapDrawable) loadThumbnail).getBitmap();
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            this.mReceiver.onBitmapDecoded(bitmap);
        }
    }

    public LiveWallpaperThumbAsset(Context context, WallpaperInfo wallpaperInfo) {
        this.mContext = context.getApplicationContext();
        this.mInfo = wallpaperInfo;
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeBitmap(int i, int i2, Asset.BitmapReceiver bitmapReceiver) {
        new LoadThumbnailTask(this.mContext, this.mInfo, bitmapReceiver).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeBitmapRegion(Rect rect, int i, int i2, boolean z, Asset.BitmapReceiver bitmapReceiver) {
        bitmapReceiver.onBitmapDecoded(null);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeRawDimensions(Activity activity, Asset.DimensionsReceiver dimensionsReceiver) {
        dimensionsReceiver.onDimensionsDecoded(null);
    }

    @Override // com.android.wallpaper.asset.Asset
    public Bitmap getLowResBitmap(Context context) {
        Bitmap bitmap;
        try {
            RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
            asDrawable.model = this;
            asDrawable.isModelSet = true;
            Drawable drawable = (Drawable) ((RequestFutureTarget) asDrawable.submit()).get(2, TimeUnit.SECONDS);
            if ((drawable instanceof BitmapDrawable) && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null) {
                return bitmap;
            }
            if (drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0) {
                Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(createBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return createBitmap;
            }
            return null;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.w("LiveWallpaperThumbAsset", "Couldn't obtain low res bitmap", e);
            return null;
        }
    }

    public Drawable getThumbnailDrawable() {
        Drawable drawable = this.mThumbnailDrawable;
        if (drawable != null) {
            return drawable;
        }
        if (this.mUri != null) {
            try {
                AssetFileDescriptor openAssetFileDescriptor = this.mContext.getContentResolver().openAssetFileDescriptor(this.mUri, "r");
                if (openAssetFileDescriptor != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(this.mContext.getResources(), BitmapFactory.decodeStream(openAssetFileDescriptor.createInputStream()));
                    this.mThumbnailDrawable = bitmapDrawable;
                    openAssetFileDescriptor.close();
                    return bitmapDrawable;
                } else if (openAssetFileDescriptor != null) {
                    openAssetFileDescriptor.close();
                }
            } catch (IOException unused) {
                Log.w("LiveWallpaperThumbAsset", "Not found thumbnail from URI.");
            }
        }
        Drawable loadThumbnail = this.mInfo.loadThumbnail(this.mContext.getPackageManager());
        this.mThumbnailDrawable = loadThumbnail;
        return loadThumbnail;
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadDrawable(Context context, ImageView imageView, int i) {
        RequestOptions requestOptions;
        if (this.mUri != null) {
            requestOptions = RequestOptions.centerCropTransform().apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true)).placeholder(new ColorDrawable(i));
        } else {
            requestOptions = RequestOptions.centerCropTransform().placeholder(new ColorDrawable(i));
        }
        RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
        asDrawable.model = this;
        asDrawable.isModelSet = true;
        RequestBuilder<Drawable> apply = asDrawable.apply((BaseRequestOptions<?>) requestOptions);
        apply.transition(DrawableTransitionOptions.withCrossFade());
        apply.into(imageView);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadLowResDrawable(Activity activity, ImageView imageView, int i, BitmapTransformation bitmapTransformation) {
        Transformation transformation;
        if (bitmapTransformation == null) {
            transformation = new FitCenter();
        } else {
            transformation = new MultiTransformation(new FitCenter(), bitmapTransformation);
        }
        RequestBuilder<Drawable> asDrawable = Glide.with(activity).asDrawable();
        asDrawable.model = this;
        asDrawable.isModelSet = true;
        asDrawable.apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(transformation).placeholder(new ColorDrawable(i))).into(imageView);
    }

    public LiveWallpaperThumbAsset(Context context, WallpaperInfo wallpaperInfo, Uri uri) {
        this.mContext = context.getApplicationContext();
        this.mInfo = wallpaperInfo;
        this.mUri = uri;
    }
}
