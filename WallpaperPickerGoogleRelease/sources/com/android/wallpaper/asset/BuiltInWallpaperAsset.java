package com.android.wallpaper.asset;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.android.wallpaper.asset.Asset;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
@TargetApi(19)
/* loaded from: classes.dex */
public final class BuiltInWallpaperAsset extends Asset {
    public WallpaperModel mBuiltInWallpaperModel;
    public final Context mContext;
    public Point mDimensions;

    /* loaded from: classes.dex */
    public class DecodeBitmapAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        public int mHeight;
        public Asset.BitmapReceiver mReceiver;
        public int mWidth;

        public DecodeBitmapAsyncTask(int i, int i2, Asset.BitmapReceiver bitmapReceiver) {
            this.mWidth = i;
            this.mHeight = i2;
            this.mReceiver = bitmapReceiver;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void[] voidArr) {
            WallpaperManager instance = WallpaperManager.getInstance(BuiltInWallpaperAsset.this.mContext);
            Drawable builtInDrawable = instance.getBuiltInDrawable(this.mWidth, this.mHeight, true, 0.5f, 0.5f);
            instance.forgetLoadedWallpaper();
            return ((BitmapDrawable) builtInDrawable).getBitmap();
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            this.mReceiver.onBitmapDecoded(bitmap);
        }
    }

    /* loaded from: classes.dex */
    public class DecodeBitmapRegionAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        public Asset.BitmapReceiver mReceiver;
        public Rect mRect;

        public DecodeBitmapRegionAsyncTask(Rect rect, Asset.BitmapReceiver bitmapReceiver) {
            this.mRect = rect;
            this.mReceiver = bitmapReceiver;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void[] voidArr) {
            float f;
            Point access$100 = BuiltInWallpaperAsset.access$100(BuiltInWallpaperAsset.this);
            Rect rect = this.mRect;
            int i = rect.left;
            int i2 = access$100.x - rect.right;
            float f2 = 0.5f;
            if (i + i2 == 0) {
                f = 0.5f;
            } else {
                float f3 = (float) i;
                f = f3 / (((float) i2) + f3);
            }
            int i3 = rect.top;
            int i4 = access$100.y - rect.bottom;
            if (i3 + i4 != 0) {
                float f4 = (float) i3;
                f2 = f4 / (((float) i4) + f4);
            }
            return ((BitmapDrawable) WallpaperManager.getInstance(BuiltInWallpaperAsset.this.mContext).getBuiltInDrawable(this.mRect.width(), this.mRect.height(), false, f, f2)).getBitmap();
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            this.mReceiver.onBitmapDecoded(bitmap);
        }
    }

    /* loaded from: classes.dex */
    public class DecodeDimensionsAsyncTask extends AsyncTask<Void, Void, Point> {
        public Asset.DimensionsReceiver mReceiver;

        public DecodeDimensionsAsyncTask(Asset.DimensionsReceiver dimensionsReceiver) {
            this.mReceiver = dimensionsReceiver;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Point doInBackground(Void[] voidArr) {
            return BuiltInWallpaperAsset.access$100(BuiltInWallpaperAsset.this);
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Point point) {
            this.mReceiver.onDimensionsDecoded(point);
        }
    }

    public BuiltInWallpaperAsset(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static Point access$100(BuiltInWallpaperAsset builtInWallpaperAsset) {
        Point point = builtInWallpaperAsset.mDimensions;
        if (point != null) {
            return point;
        }
        Bitmap bitmap = ((BitmapDrawable) WallpaperManager.getInstance(builtInWallpaperAsset.mContext).getBuiltInDrawable()).getBitmap();
        Point point2 = new Point(bitmap.getWidth(), bitmap.getHeight());
        builtInWallpaperAsset.mDimensions = point2;
        return point2;
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeBitmap(int i, int i2, Asset.BitmapReceiver bitmapReceiver) {
        new DecodeBitmapAsyncTask(i, i2, bitmapReceiver).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeBitmapRegion(Rect rect, int i, int i2, boolean z, Asset.BitmapReceiver bitmapReceiver) {
        new DecodeBitmapRegionAsyncTask(rect, bitmapReceiver).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void decodeRawDimensions(Activity activity, Asset.DimensionsReceiver dimensionsReceiver) {
        new DecodeDimensionsAsyncTask(dimensionsReceiver).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadDrawable(Context context, ImageView imageView, int i) {
        if (this.mBuiltInWallpaperModel == null) {
            this.mBuiltInWallpaperModel = new WallpaperModel(context.getApplicationContext(), 0);
        }
        RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
        asDrawable.load(this.mBuiltInWallpaperModel);
        RequestBuilder<Drawable> apply = asDrawable.apply((BaseRequestOptions<?>) RequestOptions.centerCropTransform().placeholder(new ColorDrawable(i)));
        apply.transition(DrawableTransitionOptions.withCrossFade());
        apply.into(imageView);
    }
}
