package com.google.android.apps.wallpaper.backdrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0;
import androidx.coordinatorlayout.R$style;
import androidx.core.R$attr;
import androidx.lifecycle.runtime.R$id;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.GoogleWallpapersInjector;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.module.WallpaperPreferences;
import com.android.wallpaper.network.ServerFetcher;
import com.android.wallpaper.util.DiskBasedLogger;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Image;
import java.util.Date;
/* loaded from: classes.dex */
public class BackdropWallpaperRotator {

    /* loaded from: classes.dex */
    public static class BackdropRotationAsyncTask extends AsyncTask<Void, Void, Void> {
        public Context mAppContext;
        public String mCollectionId;
        public String mCollectionName;
        public RotationListener mListener;
        public String mResumeToken;

        public BackdropRotationAsyncTask(Context context, String str, String str2, String str3, RotationListener rotationListener) {
            this.mAppContext = context;
            this.mCollectionId = str;
            this.mCollectionName = str2;
            this.mListener = rotationListener;
            this.mResumeToken = str3;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void[] voidArr) {
            BackdropWallpaperRotator.fetchAndSetNextWallpaper(this.mAppContext, this.mCollectionId, this.mCollectionName, this.mResumeToken, this.mListener);
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class NextImageCallback implements ServerFetcher.NextImageInCollectionCallback {
        public final Context mAppContext;
        public final String mCollectionId;
        public final String mCollectionName;
        public final RotationListener mListener;
        public final WallpaperPreferences mPreferences;
        public final String mResumeToken;

        public NextImageCallback(Context context, String str, String str2, String str3, RotationListener rotationListener, WallpaperPreferences wallpaperPreferences) {
            this.mAppContext = context;
            this.mCollectionId = str;
            this.mCollectionName = str2;
            this.mResumeToken = str3;
            this.mListener = rotationListener;
            this.mPreferences = wallpaperPreferences;
        }

        @Override // com.android.wallpaper.network.ServerFetcher.NextImageInCollectionCallback
        public void onError(VolleyError volleyError) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Volley error of type ");
            m.append(volleyError.getClass().getSimpleName());
            m.append(" requesting next wallpaper metadata for collectionId: ");
            m.append(this.mCollectionId);
            m.append(" and collectionName: ");
            m.append(this.mCollectionName);
            m.append(" with resumeToken: ");
            DiskBasedLogger.e("BackdropWPRotator", FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(m, this.mResumeToken, ", rescheduling this task."), this.mAppContext);
            DiskBasedLogger.e("BackdropWPRotator", "Detailed error message: " + volleyError.getMessage(), this.mAppContext);
            NetworkResponse networkResponse = volleyError.networkResponse;
            if (networkResponse != null) {
                StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Volley network response with status code: ");
                m2.append(networkResponse.statusCode);
                m2.append(" and headers: ");
                m2.append(networkResponse.headers);
                m2.append(" and network roundtrip time of ");
                m2.append(networkResponse.networkTimeMs);
                m2.append("ms");
                DiskBasedLogger.e("BackdropWPRotator", m2.toString(), this.mAppContext);
            }
            UserEventLogger userEventLogger = InjectorProvider.getInjector().getUserEventLogger(this.mAppContext);
            if (volleyError instanceof NoConnectionError) {
                userEventLogger.logDailyWallpaperMetadataRequestFailure(1);
            } else if (volleyError instanceof ParseError) {
                userEventLogger.logDailyWallpaperMetadataRequestFailure(2);
            } else if (volleyError instanceof ServerError) {
                userEventLogger.logDailyWallpaperMetadataRequestFailure(3);
            } else if (volleyError instanceof TimeoutError) {
                userEventLogger.logDailyWallpaperMetadataRequestFailure(4);
            } else {
                userEventLogger.logDailyWallpaperMetadataRequestFailure(0);
            }
            BackdropWallpaperRotator.access$300(this.mAppContext, 1);
            this.mListener.onError();
        }

        @Override // com.android.wallpaper.network.ServerFetcher.NextImageInCollectionCallback
        public void onSuccess(final ImaxWallpaperProto$Image imaxWallpaperProto$Image, final String str) {
            final Uri createRotatingWallpaperUri = FifeImageUrlFactory.getInstance().createRotatingWallpaperUri(this.mAppContext, imaxWallpaperProto$Image.getImageUrl());
            final Injector injector = InjectorProvider.getInjector();
            injector.getRequester(this.mAppContext).loadImageBitmap(createRotatingWallpaperUri, new SimpleTarget<Bitmap>() { // from class: com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotator.NextImageCallback.1
                @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
                public void onLoadFailed(Drawable drawable) {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Wallpaper bitmap load failed for FIFE URL: ");
                    m.append(createRotatingWallpaperUri);
                    m.append(" from collection with collectionId: ");
                    m.append(NextImageCallback.this.mCollectionId);
                    m.append(" with new resumeToken: ");
                    DiskBasedLogger.e("BackdropWPRotator", FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(m, str, ", rescheduling this task."), NextImageCallback.this.mAppContext);
                    BackdropWallpaperRotator.access$300(NextImageCallback.this.mAppContext, 2);
                    NextImageCallback.this.mListener.onError();
                }

                @Override // com.bumptech.glide.request.target.Target
                public void onResourceReady(Object obj, Transition transition) {
                    Bitmap bitmap = (Bitmap) obj;
                    int i = 2;
                    if (bitmap == null) {
                        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Wallpaper bitmap was null for wallpaper from FIFE URL: ");
                        m.append(createRotatingWallpaperUri);
                        m.append(" from collection with collectionId: ");
                        m.append(NextImageCallback.this.mCollectionId);
                        m.append(" with new resumeToken: ");
                        DiskBasedLogger.e("BackdropWPRotator", FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(m, str, ", rescheduling this task."), NextImageCallback.this.mAppContext);
                        BackdropWallpaperRotator.access$300(NextImageCallback.this.mAppContext, 2);
                        NextImageCallback.this.mListener.onError();
                        return;
                    }
                    if (!((DefaultWallpaperPersister) injector.getWallpaperPersister(NextImageCallback.this.mAppContext)).setWallpaperInRotation(bitmap, R$id.parseAttributions(imaxWallpaperProto$Image.getAttributionList(), NextImageCallback.this.mCollectionName), R$style.getActionLabelForType(imaxWallpaperProto$Image.getType().getNumber()), R$style.getActionIconForType(imaxWallpaperProto$Image.getType().getNumber()), imaxWallpaperProto$Image.getActionUrl(), NextImageCallback.this.mCollectionId)) {
                        StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Unable to set wallpaper in rotation for wallpaper from FIFE URL: ");
                        m2.append(createRotatingWallpaperUri);
                        m2.append(" from collection with collectionId: ");
                        m2.append(NextImageCallback.this.mCollectionId);
                        m2.append(" with new resumeToken: ");
                        DiskBasedLogger.e("BackdropWPRotator", FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(m2, str, ", rescheduling this task."), NextImageCallback.this.mAppContext);
                        BackdropWallpaperRotator.access$300(NextImageCallback.this.mAppContext, 3);
                        NextImageCallback.this.mListener.onError();
                        return;
                    }
                    NextImageCallback.this.mPreferences.setLastAppActiveTimestamp(new Date().getTime());
                    BackdropWallpaperRotator.access$300(NextImageCallback.this.mAppContext, 0);
                    boolean isLockWallpaperSet = R$attr.isLockWallpaperSet(NextImageCallback.this.mAppContext);
                    NextImageCallback nextImageCallback = NextImageCallback.this;
                    WallpaperPreferences wallpaperPreferences = nextImageCallback.mPreferences;
                    if (isLockWallpaperSet) {
                        i = 0;
                    }
                    wallpaperPreferences.updateDailyWallpaperSet(i, nextImageCallback.mCollectionId, String.valueOf(imaxWallpaperProto$Image.getAssetId()));
                    NextImageCallback.this.mListener.onSuccess(str);
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public interface RotationListener {
        void onError();

        void onSuccess(String str);
    }

    public static void access$300(Context context, int i) {
        Injector injector = InjectorProvider.getInjector();
        WallpaperPreferences preferences = injector.getPreferences(context);
        UserEventLogger userEventLogger = injector.getUserEventLogger(context);
        preferences.setPendingDailyWallpaperUpdateStatus(0);
        userEventLogger.logDailyWallpaperSetNextWallpaperResult(i);
    }

    public static void fetchAndSetNextWallpaper(Context context, String str, String str2, String str3, RotationListener rotationListener) {
        Injector injector = InjectorProvider.getInjector();
        WallpaperPreferences preferences = injector.getPreferences(context);
        preferences.setPendingDailyWallpaperUpdateStatus(1);
        ((BackdropFetcher) ((GoogleWallpapersInjector) injector).getServerFetcher(context)).fetchNextImageInCollection(context, str, str3, new NextImageCallback(context, str, str2, str3, rotationListener, preferences));
    }

    public static void updateWallpaper(Context context, String str, String str2, String str3, RotationListener rotationListener) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new BackdropRotationAsyncTask(context, str, str2, str3, rotationListener).execute(new Void[0]);
        } else {
            fetchAndSetNextWallpaper(context, str, str2, str3, rotationListener);
        }
    }
}
