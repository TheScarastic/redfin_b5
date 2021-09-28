package com.android.wallpaper.asset;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import com.android.wallpaper.module.BitmapCropper;
import com.android.wallpaper.module.DefaultBitmapCropper;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.util.ScreenSizeCalculator;
import com.android.wallpaper.util.WallpaperCropUtils;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class Asset {

    /* loaded from: classes.dex */
    public interface BitmapReceiver {
        void onBitmapDecoded(Bitmap bitmap);
    }

    /* loaded from: classes.dex */
    public static class CenterCropBitmapTask extends AsyncTask<Void, Void, Bitmap> {
        public Bitmap mBitmap;
        public BitmapReceiver mBitmapReceiver;
        public int mImageViewHeight;
        public int mImageViewWidth;

        public CenterCropBitmapTask(Bitmap bitmap, View view, BitmapReceiver bitmapReceiver) {
            this.mBitmap = bitmap;
            this.mBitmapReceiver = bitmapReceiver;
            Point viewDimensions = Asset.getViewDimensions(view);
            this.mImageViewWidth = viewDimensions.x;
            this.mImageViewHeight = viewDimensions.y;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void[] voidArr) {
            int i = this.mImageViewWidth;
            int i2 = this.mImageViewHeight;
            float width = (float) this.mBitmap.getWidth();
            float height = (float) this.mBitmap.getHeight();
            float min = Math.min(width / ((float) i), height / ((float) i2));
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(this.mBitmap, Math.round(width / min), Math.round(height / min), true);
            int max = Math.max(0, (createScaledBitmap.getWidth() - i) / 2);
            int max2 = Math.max(0, (createScaledBitmap.getHeight() - i2) / 2);
            return Bitmap.createBitmap(createScaledBitmap, max, max2, createScaledBitmap.getWidth() - (max * 2), createScaledBitmap.getHeight() - (max2 * 2));
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            this.mBitmapReceiver.onBitmapDecoded(bitmap);
        }
    }

    /* loaded from: classes.dex */
    public interface DimensionsReceiver {
        void onDimensionsDecoded(Point point);
    }

    /* loaded from: classes.dex */
    public interface DrawableLoadedListener {
        void onDrawableLoaded();
    }

    public static Drawable getPlaceholderDrawable(Context context, ImageView imageView, int i) {
        Point viewDimensions = getViewDimensions(imageView);
        Bitmap createBitmap = Bitmap.createBitmap(viewDimensions.x, viewDimensions.y, Bitmap.Config.ARGB_8888);
        createBitmap.eraseColor(i);
        return new BitmapDrawable(context.getResources(), createBitmap);
    }

    public static Point getViewDimensions(View view) {
        int i;
        int width = view.getWidth() > 0 ? view.getWidth() : Math.abs(view.getLayoutParams().width);
        if (view.getHeight() > 0) {
            i = view.getHeight();
        } else {
            i = Math.abs(view.getLayoutParams().height);
        }
        return new Point(width, i);
    }

    public void adjustCropRect(Context context, Point point, Rect rect) {
        WallpaperCropUtils.adjustCropRect(context, rect, true);
    }

    public abstract void decodeBitmap(int i, int i2, BitmapReceiver bitmapReceiver);

    public abstract void decodeBitmapRegion(Rect rect, int i, int i2, boolean z, BitmapReceiver bitmapReceiver);

    public abstract void decodeRawDimensions(Activity activity, DimensionsReceiver dimensionsReceiver);

    public Bitmap getLowResBitmap(Context context) {
        return null;
    }

    public void loadDrawable(final Context context, final ImageView imageView, int i) {
        int i2;
        int i3;
        final boolean z = imageView.getDrawable() == null;
        final ColorDrawable colorDrawable = new ColorDrawable(i);
        if (z) {
            imageView.setImageDrawable(colorDrawable);
        }
        if (imageView.getWidth() > 0) {
            i2 = imageView.getWidth();
        } else {
            i2 = Math.abs(imageView.getLayoutParams().width);
        }
        if (imageView.getHeight() > 0) {
            i3 = imageView.getHeight();
        } else {
            i3 = Math.abs(imageView.getLayoutParams().height);
        }
        decodeBitmap(i2, i3, new BitmapReceiver(this) { // from class: com.android.wallpaper.asset.Asset.1
            @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
            public void onBitmapDecoded(Bitmap bitmap) {
                if (!z) {
                    imageView.setImageBitmap(bitmap);
                    return;
                }
                Resources resources = context.getResources();
                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{colorDrawable, new BitmapDrawable(resources, bitmap)});
                transitionDrawable.setCrossFadeEnabled(true);
                imageView.setImageDrawable(transitionDrawable);
                transitionDrawable.startTransition(resources.getInteger(17694720));
            }
        });
    }

    public void loadDrawableWithTransition(final Context context, final ImageView imageView, final int i, final DrawableLoadedListener drawableLoadedListener, int i2) {
        Point viewDimensions = getViewDimensions(imageView);
        if (imageView.getDrawable() == null) {
            imageView.setImageDrawable(getPlaceholderDrawable(context, imageView, i2));
        }
        decodeBitmap(viewDimensions.x, viewDimensions.y, new BitmapReceiver(this) { // from class: com.android.wallpaper.asset.Asset.2
            @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
            public void onBitmapDecoded(Bitmap bitmap) {
                final Resources resources = context.getResources();
                new CenterCropBitmapTask(bitmap, imageView, new BitmapReceiver() { // from class: com.android.wallpaper.asset.Asset.2.1
                    @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
                    public void onBitmapDecoded(Bitmap bitmap2) {
                        Drawable[] drawableArr = new Drawable[2];
                        Drawable drawable = imageView.getDrawable();
                        if (drawable instanceof TransitionDrawable) {
                            TransitionDrawable transitionDrawable = (TransitionDrawable) drawable;
                            drawableArr[0] = transitionDrawable.findDrawableByLayerId(transitionDrawable.getId(1));
                        } else {
                            drawableArr[0] = drawable;
                        }
                        drawableArr[1] = new BitmapDrawable(resources, bitmap2);
                        TransitionDrawable transitionDrawable2 = new TransitionDrawable(drawableArr);
                        transitionDrawable2.setCrossFadeEnabled(true);
                        imageView.setImageDrawable(transitionDrawable2);
                        transitionDrawable2.startTransition(i);
                        DrawableLoadedListener drawableLoadedListener2 = drawableLoadedListener;
                        if (drawableLoadedListener2 != null) {
                            drawableLoadedListener2.onDrawableLoaded();
                        }
                    }
                }).execute(new Void[0]);
            }
        });
    }

    public void loadLowResDrawable(Activity activity, ImageView imageView, int i, BitmapTransformation bitmapTransformation) {
    }

    public void loadPreviewImage(Activity activity, ImageView imageView, int i) {
        boolean z = imageView.getDrawable() == null;
        ColorDrawable colorDrawable = new ColorDrawable(i);
        if (z) {
            imageView.setImageDrawable(colorDrawable);
        }
        decodeRawDimensions(activity, new DimensionsReceiver(activity, imageView, i, z, colorDrawable) { // from class: com.android.wallpaper.asset.Asset$$ExternalSyntheticLambda0
            public final /* synthetic */ Activity f$1;
            public final /* synthetic */ ImageView f$2;
            public final /* synthetic */ int f$3;
            public final /* synthetic */ boolean f$4;
            public final /* synthetic */ Drawable f$5;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            @Override // com.android.wallpaper.asset.Asset.DimensionsReceiver
            public final void onDimensionsDecoded(Point point) {
                Asset asset = Asset.this;
                Activity activity2 = this.f$1;
                ImageView imageView2 = this.f$2;
                int i2 = this.f$3;
                boolean z2 = this.f$4;
                Drawable drawable = this.f$5;
                Objects.requireNonNull(asset);
                if (point == null) {
                    asset.loadDrawable(activity2, imageView2, i2);
                    return;
                }
                Rect calculateVisibleRect = WallpaperCropUtils.calculateVisibleRect(point, ScreenSizeCalculator.getInstance().getScreenSize(activity2.getWindowManager().getDefaultDisplay()));
                asset.adjustCropRect(activity2, point, calculateVisibleRect);
                ((DefaultBitmapCropper) InjectorProvider.getInjector().getBitmapCropper()).cropAndScaleBitmap(asset, 1.0f, calculateVisibleRect, WallpaperCropUtils.isRtl(activity2), new BitmapCropper.Callback(asset, imageView2, z2, activity2, drawable) { // from class: com.android.wallpaper.asset.Asset.3
                    public final /* synthetic */ Activity val$activity;
                    public final /* synthetic */ ImageView val$imageView;
                    public final /* synthetic */ boolean val$needsTransition;
                    public final /* synthetic */ Drawable val$placeholderDrawable;

                    {
                        this.val$imageView = r2;
                        this.val$needsTransition = r3;
                        this.val$activity = r4;
                        this.val$placeholderDrawable = r5;
                    }

                    @Override // com.android.wallpaper.module.BitmapCropper.Callback
                    public void onBitmapCropped(Bitmap bitmap) {
                        this.val$imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if (!this.val$needsTransition) {
                            this.val$imageView.setImageBitmap(bitmap);
                            return;
                        }
                        Resources resources = this.val$activity.getResources();
                        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{this.val$placeholderDrawable, new BitmapDrawable(resources, bitmap)});
                        transitionDrawable.setCrossFadeEnabled(true);
                        this.val$imageView.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(resources.getInteger(17694720));
                    }

                    @Override // com.android.wallpaper.module.BitmapCropper.Callback
                    public void onError(Throwable th) {
                    }
                });
            }
        });
    }
}
