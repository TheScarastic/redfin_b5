package com.android.wallpaper.asset;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import android.widget.ImageView;
import androidx.exifinterface.media.ExifInterface;
import com.android.wallpaper.asset.Asset;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ContentUriAsset extends StreamableAsset {
    public final Context mContext;
    public ExifInterfaceCompat mExifCompat;
    public int mExifOrientation = -1;
    public final RequestOptions mRequestOptions;
    public final Uri mUri;

    /* loaded from: classes.dex */
    public static class BitmapCropTask extends AsyncTask<Void, Void, Bitmap> {
        public Rect mCropRect;
        public Bitmap mFromBitmap;
        public Asset.BitmapReceiver mReceiver;

        public BitmapCropTask(Bitmap bitmap, Rect rect, Asset.BitmapReceiver bitmapReceiver) {
            this.mFromBitmap = bitmap;
            this.mCropRect = rect;
            this.mReceiver = bitmapReceiver;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Void[] voidArr) {
            Bitmap bitmap = this.mFromBitmap;
            if (bitmap == null) {
                return null;
            }
            Rect rect = this.mCropRect;
            return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), this.mCropRect.height());
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            this.mReceiver.onBitmapDecoded(bitmap);
        }
    }

    public ContentUriAsset(Context context, Uri uri, boolean z) {
        RequestOptions centerCropTransform = RequestOptions.centerCropTransform();
        this.mContext = context.getApplicationContext();
        this.mUri = uri;
        if (z) {
            this.mRequestOptions = centerCropTransform.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true));
        } else {
            this.mRequestOptions = centerCropTransform;
        }
    }

    @Override // com.android.wallpaper.asset.StreamableAsset, com.android.wallpaper.asset.Asset
    public void decodeBitmapRegion(final Rect rect, int i, int i2, boolean z, final Asset.BitmapReceiver bitmapReceiver) {
        if (!isJpeg()) {
            String type = this.mContext.getContentResolver().getType(this.mUri);
            if (!(type != null && type.equals("image/png"))) {
                decodeRawDimensions(null, new Asset.DimensionsReceiver() { // from class: com.android.wallpaper.asset.ContentUriAsset.1
                    @Override // com.android.wallpaper.asset.Asset.DimensionsReceiver
                    public void onDimensionsDecoded(Point point) {
                        if (point == null) {
                            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("There was an error decoding the asset's raw dimensions with content URI: ");
                            m.append(ContentUriAsset.this.mUri);
                            Log.e("ContentUriAsset", m.toString());
                            bitmapReceiver.onBitmapDecoded(null);
                            return;
                        }
                        ContentUriAsset.this.decodeBitmap(point.x, point.y, new Asset.BitmapReceiver() { // from class: com.android.wallpaper.asset.ContentUriAsset.1.1
                            @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
                            public void onBitmapDecoded(Bitmap bitmap) {
                                if (bitmap == null) {
                                    StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("There was an error decoding the asset's full bitmap with content URI: ");
                                    m2.append(ContentUriAsset.this.mUri);
                                    Log.e("ContentUriAsset", m2.toString());
                                    bitmapReceiver.onBitmapDecoded(null);
                                    return;
                                }
                                AnonymousClass1 r2 = AnonymousClass1.this;
                                new BitmapCropTask(bitmap, rect, bitmapReceiver).execute(new Void[0]);
                            }
                        });
                    }
                });
                return;
            }
        }
        super.decodeBitmapRegion(rect, i, i2, z, bitmapReceiver);
    }

    public final void ensureExifInterface() {
        if (this.mExifCompat == null) {
            try {
                InputStream openInputStream = openInputStream();
                if (openInputStream != null) {
                    this.mExifCompat = new ExifInterfaceCompat(openInputStream);
                }
                if (openInputStream != null) {
                    openInputStream.close();
                }
            } catch (IOException e) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Couldn't read stream for ");
                m.append(this.mUri);
                Log.w("ContentUriAsset", m.toString(), e);
            }
        }
    }

    @Override // com.android.wallpaper.asset.StreamableAsset
    public int getExifOrientation() {
        int i = this.mExifOrientation;
        if (i != -1) {
            return i;
        }
        ensureExifInterface();
        ExifInterfaceCompat exifInterfaceCompat = this.mExifCompat;
        int i2 = 1;
        if (exifInterfaceCompat == null) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Unable to read EXIF rotation for content URI asset with content URI: ");
            m.append(this.mUri);
            Log.w("ContentUriAsset", m.toString());
        } else {
            ExifInterface exifInterface = exifInterfaceCompat.mFrameworkExifInterface;
            if (exifInterface != null) {
                i2 = exifInterface.getAttributeInt("Orientation", 1);
            } else {
                androidx.exifinterface.media.ExifInterface exifInterface2 = exifInterfaceCompat.mSupportExifInterface;
                Objects.requireNonNull(exifInterface2);
                ExifInterface.ExifAttribute exifAttribute = exifInterface2.getExifAttribute("Orientation");
                if (exifAttribute != null) {
                    try {
                        i2 = exifAttribute.getIntValue(exifInterface2.mExifByteOrder);
                    } catch (NumberFormatException unused) {
                    }
                }
            }
        }
        this.mExifOrientation = i2;
        return i2;
    }

    public boolean isJpeg() {
        String type = this.mContext.getContentResolver().getType(this.mUri);
        return type != null && type.equals("image/jpeg");
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadDrawable(Context context, ImageView imageView, int i) {
        RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
        asDrawable.load(this.mUri);
        RequestBuilder<Drawable> apply = asDrawable.apply((BaseRequestOptions<?>) this.mRequestOptions.placeholder(new ColorDrawable(i)));
        apply.transition(DrawableTransitionOptions.withCrossFade());
        apply.into(imageView);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadDrawableWithTransition(Context context, ImageView imageView, int i, final Asset.DrawableLoadedListener drawableLoadedListener, int i2) {
        RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
        asDrawable.load(this.mUri);
        RequestBuilder<Drawable> apply = asDrawable.apply((BaseRequestOptions<?>) this.mRequestOptions.placeholder(new ColorDrawable(i2)));
        DrawableTransitionOptions drawableTransitionOptions = new DrawableTransitionOptions();
        drawableTransitionOptions.transitionFactory = new DrawableCrossFadeFactory(i, false);
        apply.transition(drawableTransitionOptions);
        AnonymousClass2 r5 = new RequestListener<Drawable>(this) { // from class: com.android.wallpaper.asset.ContentUriAsset.2
            @Override // com.bumptech.glide.request.RequestListener
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                return false;
            }

            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object, com.bumptech.glide.request.target.Target, com.bumptech.glide.load.DataSource, boolean] */
            @Override // com.bumptech.glide.request.RequestListener
            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                Asset.DrawableLoadedListener drawableLoadedListener2 = drawableLoadedListener;
                if (drawableLoadedListener2 == null) {
                    return false;
                }
                drawableLoadedListener2.onDrawableLoaded();
                return false;
            }
        };
        apply.requestListeners = null;
        ArrayList arrayList = new ArrayList();
        apply.requestListeners = arrayList;
        arrayList.add(r5);
        apply.into(imageView);
    }

    @Override // com.android.wallpaper.asset.StreamableAsset
    public InputStream openInputStream() {
        try {
            return this.mContext.getContentResolver().openInputStream(this.mUri);
        } catch (FileNotFoundException e) {
            Log.w("ContentUriAsset", "Image file not found", e);
            return null;
        }
    }
}
