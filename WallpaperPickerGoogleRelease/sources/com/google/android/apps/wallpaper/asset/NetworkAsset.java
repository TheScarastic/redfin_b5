package com.google.android.apps.wallpaper.asset;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import android.widget.ImageView;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.StreamableAsset;
import com.android.wallpaper.module.InjectorProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.BitmapTransitionFactory;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.internal.zzfit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
/* loaded from: classes.dex */
public class NetworkAsset extends StreamableAsset {
    public zzfit mRequester;
    public Uri mTinyUri;
    public Uri mUri;

    /* loaded from: classes.dex */
    public class DecodeDimensionsAsyncTask extends AsyncTask<Void, Void, Point> {
        public File mFile;
        public Asset.DimensionsReceiver mReceiver;

        public DecodeDimensionsAsyncTask(File file, Asset.DimensionsReceiver dimensionsReceiver) {
            this.mFile = file;
            this.mReceiver = dimensionsReceiver;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Point doInBackground(Void[] voidArr) {
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(this.mFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("File not found for network asset with URL: ");
                m.append(NetworkAsset.this.mUri);
                Log.e("NetworkAsset", m.toString(), e);
                fileInputStream = null;
            }
            if (fileInputStream == null) {
                return null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(fileInputStream, null, options);
            try {
                fileInputStream.close();
            } catch (IOException e2) {
                StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Unable to close input stream for NetworkAsset with URL: ");
                m2.append(NetworkAsset.this.mUri);
                Log.e("NetworkAsset", m2.toString(), e2);
            }
            return new Point(options.outWidth, options.outHeight);
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Point point) {
            this.mReceiver.onDimensionsDecoded(point);
        }
    }

    public NetworkAsset(Context context, Uri uri) {
        this.mUri = uri;
        this.mTinyUri = null;
        this.mRequester = InjectorProvider.getInjector().getRequester(context.getApplicationContext());
    }

    @Override // com.android.wallpaper.asset.StreamableAsset, com.android.wallpaper.asset.Asset
    public void decodeRawDimensions(Activity activity, final Asset.DimensionsReceiver dimensionsReceiver) {
        if (activity == null) {
            super.decodeRawDimensions(null, dimensionsReceiver);
            return;
        }
        zzfit zzfit = this.mRequester;
        Uri uri = this.mUri;
        AnonymousClass1 r3 = new SimpleTarget<File>() { // from class: com.google.android.apps.wallpaper.asset.NetworkAsset.1
            @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
            public void onLoadFailed(Drawable drawable) {
                dimensionsReceiver.onDimensionsDecoded(null);
            }

            @Override // com.bumptech.glide.request.target.Target
            public void onResourceReady(Object obj, Transition transition) {
                new DecodeDimensionsAsyncTask((File) obj, dimensionsReceiver).execute(new Void[0]);
            }
        };
        Objects.requireNonNull(zzfit);
        RequestManager with = Glide.with(activity);
        Objects.requireNonNull(with);
        RequestBuilder as = with.as(File.class);
        if (RequestOptions.skipMemoryCacheTrueOptions == null) {
            RequestOptions skipMemoryCache = new RequestOptions().skipMemoryCache(true);
            skipMemoryCache.autoClone();
            RequestOptions.skipMemoryCacheTrueOptions = skipMemoryCache;
        }
        RequestBuilder apply = as.apply((BaseRequestOptions<?>) RequestOptions.skipMemoryCacheTrueOptions);
        apply.model = uri;
        apply.isModelSet = true;
        RequestBuilder apply2 = apply.apply((BaseRequestOptions<?>) RequestOptions.option(HttpGlideUrlLoader.TIMEOUT, 10000));
        apply2.into(r3, null, apply2);
    }

    @Override // com.android.wallpaper.asset.Asset
    public Bitmap getLowResBitmap(Context context) {
        try {
            RequestBuilder<Bitmap> asBitmap = Glide.with(context).asBitmap();
            asBitmap.load(this.mTinyUri);
            return (Bitmap) ((RequestFutureTarget) asBitmap.apply((BaseRequestOptions<?>) RequestOptions.noTransformation().set(HttpGlideUrlLoader.TIMEOUT, 10000)).submit()).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("NetworkAsset", "Couldn't obtain low res bitmap", e);
            return null;
        }
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadDrawable(Context context, ImageView imageView, int i) {
        RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
        if (this.mTinyUri != null) {
            RequestBuilder asDrawable2 = Glide.with(context).asDrawable();
            asDrawable2.load(this.mTinyUri);
            asDrawable2.transition(DrawableTransitionOptions.withCrossFade());
            asDrawable.thumbnailBuilder = asDrawable2;
        }
        asDrawable.load(this.mUri);
        RequestBuilder<Drawable> apply = asDrawable.apply((BaseRequestOptions<?>) RequestOptions.noTransformation().placeholder(new ColorDrawable(i)).set(HttpGlideUrlLoader.TIMEOUT, 10000));
        apply.transition(DrawableTransitionOptions.withCrossFade());
        apply.into(imageView);
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadDrawableWithTransition(final Context context, final ImageView imageView, final int i, final Asset.DrawableLoadedListener drawableLoadedListener, int i2) {
        if (imageView.getDrawable() == null) {
            imageView.setImageDrawable(Asset.getPlaceholderDrawable(context, imageView, i2));
        }
        InjectorProvider.getInjector().getRequester(context).loadImageBitmap(this.mUri, new SimpleTarget<Bitmap>(this) { // from class: com.google.android.apps.wallpaper.asset.NetworkAsset.2
            @Override // com.bumptech.glide.request.target.Target
            public void onResourceReady(Object obj, Transition transition) {
                final Resources resources = context.getResources();
                new Asset.CenterCropBitmapTask((Bitmap) obj, imageView, new Asset.BitmapReceiver() { // from class: com.google.android.apps.wallpaper.asset.NetworkAsset.2.1
                    @Override // com.android.wallpaper.asset.Asset.BitmapReceiver
                    public void onBitmapDecoded(Bitmap bitmap) {
                        Drawable[] drawableArr = new Drawable[2];
                        Drawable drawable = imageView.getDrawable();
                        if (drawable instanceof TransitionDrawable) {
                            TransitionDrawable transitionDrawable = (TransitionDrawable) drawable;
                            drawableArr[0] = transitionDrawable.findDrawableByLayerId(transitionDrawable.getId(1));
                        } else {
                            drawableArr[0] = drawable;
                        }
                        drawableArr[1] = new BitmapDrawable(resources, bitmap);
                        TransitionDrawable transitionDrawable2 = new TransitionDrawable(drawableArr);
                        transitionDrawable2.setCrossFadeEnabled(true);
                        imageView.setImageDrawable(transitionDrawable2);
                        transitionDrawable2.startTransition(i);
                        Asset.DrawableLoadedListener drawableLoadedListener2 = drawableLoadedListener;
                        if (drawableLoadedListener2 != null) {
                            drawableLoadedListener2.onDrawableLoaded();
                        }
                    }
                }).execute(new Void[0]);
            }
        });
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadLowResDrawable(Activity activity, ImageView imageView, int i, BitmapTransformation bitmapTransformation) {
        RequestBuilder<Bitmap> asBitmap = Glide.with(activity).asBitmap();
        asBitmap.load(this.mTinyUri);
        RequestBuilder<Bitmap> apply = asBitmap.apply((BaseRequestOptions<?>) RequestOptions.bitmapTransform(bitmapTransformation).placeholder(new ColorDrawable(i)).set(HttpGlideUrlLoader.TIMEOUT, 10000));
        BitmapTransitionOptions bitmapTransitionOptions = new BitmapTransitionOptions();
        bitmapTransitionOptions.transitionFactory = new BitmapTransitionFactory(new DrawableCrossFadeFactory(300, false));
        apply.transition(bitmapTransitionOptions);
        apply.into(imageView);
    }

    @Override // com.android.wallpaper.asset.StreamableAsset
    public InputStream openInputStream() {
        File loadImageFile = this.mRequester.loadImageFile(this.mUri);
        if (loadImageFile == null) {
            return null;
        }
        try {
            return new FileInputStream(loadImageFile.getAbsolutePath());
        } catch (FileNotFoundException unused) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("File not found for the image at URL: ");
            m.append(this.mUri);
            Log.e("NetworkAsset", m.toString());
            return null;
        }
    }

    public NetworkAsset(Context context, Uri uri, Uri uri2) {
        this.mUri = uri;
        this.mTinyUri = uri2;
        this.mRequester = InjectorProvider.getInjector().getRequester(context.getApplicationContext());
    }
}
