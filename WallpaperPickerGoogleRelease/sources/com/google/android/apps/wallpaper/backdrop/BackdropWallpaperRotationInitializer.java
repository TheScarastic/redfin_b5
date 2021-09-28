package com.google.android.apps.wallpaper.backdrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0;
import androidx.coordinatorlayout.R$style;
import androidx.core.R$attr;
import androidx.lifecycle.runtime.R$id;
import com.android.volley.VolleyError;
import com.android.wallpaper.model.WallpaperRotationInitializer;
import com.android.wallpaper.module.DefaultWallpaperPersister;
import com.android.wallpaper.module.DefaultWallpaperPreferences$$ExternalSyntheticOutline0;
import com.android.wallpaper.module.GoogleWallpapersInjector;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.WallpaperChangedNotifier;
import com.android.wallpaper.module.WallpaperPreferences;
import com.android.wallpaper.network.ServerFetcher;
import com.android.wallpaper.util.DiskBasedLogger;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/* loaded from: classes.dex */
public class BackdropWallpaperRotationInitializer implements WallpaperRotationInitializer {
    public static final Parcelable.Creator<BackdropWallpaperRotationInitializer> CREATOR = new Parcelable.Creator<BackdropWallpaperRotationInitializer>() { // from class: com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotationInitializer.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public BackdropWallpaperRotationInitializer createFromParcel(Parcel parcel) {
            return new BackdropWallpaperRotationInitializer(parcel, (AnonymousClass1) null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public BackdropWallpaperRotationInitializer[] newArray(int i) {
            return new BackdropWallpaperRotationInitializer[i];
        }
    };
    public String mCollectionId;
    public String mCollectionName;
    public int mStagedActionIconRes;
    public int mStagedActionLabelRes;
    public String mStagedActionUrl;
    public List<String> mStagedAttributions;
    public int mStagedRequiredNetworkState;
    public String mStagedResumeToken;
    public int mStagedWallpaperId;

    /* loaded from: classes.dex */
    public static class Factory extends WallpaperRotationInitializerFactory {
    }

    /* loaded from: classes.dex */
    public class NextImageCallback implements ServerFetcher.NextImageInCollectionCallback {
        public final Context mAppContext;
        public final WallpaperRotationInitializer.Listener mListener;
        public final int mNetworkPreference;

        public NextImageCallback(Context context, int i, WallpaperRotationInitializer.Listener listener) {
            this.mAppContext = context;
            this.mListener = listener;
            this.mNetworkPreference = i;
        }

        @Override // com.android.wallpaper.network.ServerFetcher.NextImageInCollectionCallback
        public void onError(VolleyError volleyError) {
            DiskBasedLogger.e("BackdropWPRotationInit", FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Unable to get first wallpaper in rotation for wallpaper from collection  with ID "), BackdropWallpaperRotationInitializer.this.mCollectionId, "."), this.mAppContext);
            this.mListener.onError();
        }

        @Override // com.android.wallpaper.network.ServerFetcher.NextImageInCollectionCallback
        public void onSuccess(final ImaxWallpaperProto$Image imaxWallpaperProto$Image, final String str) {
            final Uri createRotatingWallpaperUri = FifeImageUrlFactory.getInstance().createRotatingWallpaperUri(this.mAppContext, imaxWallpaperProto$Image.getImageUrl());
            InjectorProvider.getInjector().getRequester(this.mAppContext).loadImageBitmap(createRotatingWallpaperUri, new SimpleTarget<Bitmap>() { // from class: com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotationInitializer.NextImageCallback.1
                @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
                public void onLoadFailed(Drawable drawable) {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Wallpaper bitmap load failed for FIFE URL: ");
                    m.append(createRotatingWallpaperUri);
                    m.append(" from  collection with collectionId: ");
                    m.append(BackdropWallpaperRotationInitializer.this.mCollectionId);
                    m.append(" with new resumeToken: ");
                    DiskBasedLogger.e("BackdropWPRotationInit", FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(m, str, "."), NextImageCallback.this.mAppContext);
                    NextImageCallback.this.mListener.onError();
                }

                @Override // com.bumptech.glide.request.target.Target
                public void onResourceReady(Object obj, Transition transition) {
                    Bitmap bitmap = (Bitmap) obj;
                    if (bitmap == null) {
                        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Wallpaper bitmap was null for wallpaper from FIFE URL: ");
                        m.append(createRotatingWallpaperUri);
                        m.append(" from collection with collectionId: ");
                        m.append(BackdropWallpaperRotationInitializer.this.mCollectionId);
                        m.append(" and resumeToken: ");
                        DiskBasedLogger.e("BackdropWPRotationInit", FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(m, str, "."), NextImageCallback.this.mAppContext);
                        NextImageCallback.this.mListener.onError();
                        return;
                    }
                    List<String> parseAttributions = R$id.parseAttributions(imaxWallpaperProto$Image.getAttributionList(), BackdropWallpaperRotationInitializer.this.mCollectionName);
                    int cropAndSetWallpaperBitmapInRotationStatic = ((DefaultWallpaperPersister) InjectorProvider.getInjector().getWallpaperPersister(NextImageCallback.this.mAppContext)).cropAndSetWallpaperBitmapInRotationStatic(bitmap);
                    if (cropAndSetWallpaperBitmapInRotationStatic == 0) {
                        StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Unable to set rotating wallpaper bitmap for wallpaper from FIFE URL: ");
                        m2.append(createRotatingWallpaperUri);
                        m2.append(" from collection with collectionId: ");
                        m2.append(BackdropWallpaperRotationInitializer.this.mCollectionId);
                        m2.append(" with new resumeToken: ");
                        DiskBasedLogger.e("BackdropWPRotationInit", FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(m2, str, "."), NextImageCallback.this.mAppContext);
                        NextImageCallback.this.mListener.onError();
                        return;
                    }
                    BackdropWallpaperRotationInitializer backdropWallpaperRotationInitializer = BackdropWallpaperRotationInitializer.this;
                    backdropWallpaperRotationInitializer.mStagedAttributions = parseAttributions;
                    backdropWallpaperRotationInitializer.mStagedActionUrl = imaxWallpaperProto$Image.getActionUrl();
                    BackdropWallpaperRotationInitializer.this.mStagedActionLabelRes = R$style.getActionLabelForType(imaxWallpaperProto$Image.getType().getNumber());
                    BackdropWallpaperRotationInitializer.this.mStagedActionIconRes = R$style.getActionIconForType(imaxWallpaperProto$Image.getType().getNumber());
                    NextImageCallback nextImageCallback = NextImageCallback.this;
                    BackdropWallpaperRotationInitializer backdropWallpaperRotationInitializer2 = BackdropWallpaperRotationInitializer.this;
                    backdropWallpaperRotationInitializer2.mStagedWallpaperId = cropAndSetWallpaperBitmapInRotationStatic;
                    backdropWallpaperRotationInitializer2.mStagedRequiredNetworkState = nextImageCallback.mNetworkPreference;
                    backdropWallpaperRotationInitializer2.mStagedResumeToken = str;
                    nextImageCallback.mListener.onFirstWallpaperInRotationSet();
                    InjectorProvider.getInjector().getPreferences(NextImageCallback.this.mAppContext).updateDailyWallpaperSet(R$attr.isLockWallpaperSet(NextImageCallback.this.mAppContext) ? 0 : 2, BackdropWallpaperRotationInitializer.this.mCollectionId, String.valueOf(imaxWallpaperProto$Image.getAssetId()));
                }
            });
        }
    }

    public BackdropWallpaperRotationInitializer(String str, String str2) {
        this.mCollectionId = str;
        this.mCollectionName = str2;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.android.wallpaper.model.WallpaperRotationInitializer
    public void setFirstWallpaperInRotation(Context context, int i, WallpaperRotationInitializer.Listener listener) {
        ((BackdropFetcher) ((GoogleWallpapersInjector) InjectorProvider.getInjector()).getServerFetcher(context)).fetchNextImageInCollection(context, this.mCollectionId, null, new NextImageCallback(context, i, listener));
    }

    @Override // com.android.wallpaper.model.WallpaperRotationInitializer
    public boolean startRotation(Context context) {
        Injector injector = InjectorProvider.getInjector();
        WallpaperPreferences preferences = injector.getPreferences(context);
        ((DefaultWallpaperPersister) injector.getWallpaperPersister(context)).finalizeWallpaperForRotatingComponent(this.mStagedAttributions, this.mStagedActionUrl, this.mStagedActionLabelRes, this.mStagedActionIconRes, this.mCollectionId, this.mStagedWallpaperId);
        BackdropPreferences instance = BackdropPreferences.getInstance(context);
        instance.clear();
        DefaultWallpaperPreferences$$ExternalSyntheticOutline0.m(instance.mSharedPrefs, "collection_id", this.mCollectionId);
        DefaultWallpaperPreferences$$ExternalSyntheticOutline0.m(instance.mSharedPrefs, "collection_name", this.mCollectionName);
        instance.mSharedPrefs.edit().putInt("required_network_state", this.mStagedRequiredNetworkState).apply();
        instance.setResumeToken(this.mStagedResumeToken);
        JobSchedulerBackdropTaskScheduler jobSchedulerBackdropTaskScheduler = (JobSchedulerBackdropTaskScheduler) BackdropTaskScheduler.getInstance(context);
        jobSchedulerBackdropTaskScheduler.mJobScheduler.cancel(0);
        jobSchedulerBackdropTaskScheduler.mJobScheduler.cancel(1);
        BackdropAlarmScheduler.snoozeAlarm(context);
        preferences.setWallpaperPresentationMode(2);
        preferences.setDailyWallpaperEnabledTimestamp(new Date().getTime());
        WallpaperChangedNotifier.getInstance().notifyWallpaperChanged();
        injector.getUserEventLogger(context).logDailyRefreshTurnedOn();
        return true;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mCollectionId);
        parcel.writeString(this.mCollectionName);
        parcel.writeStringList(this.mStagedAttributions);
        parcel.writeString(this.mStagedActionUrl);
        parcel.writeInt(this.mStagedWallpaperId);
        parcel.writeInt(this.mStagedRequiredNetworkState);
        parcel.writeString(this.mStagedResumeToken);
        parcel.writeInt(this.mStagedActionLabelRes);
        parcel.writeInt(this.mStagedActionIconRes);
    }

    public BackdropWallpaperRotationInitializer(Parcel parcel, AnonymousClass1 r2) {
        this.mCollectionId = parcel.readString();
        this.mCollectionName = parcel.readString();
        ArrayList arrayList = new ArrayList();
        this.mStagedAttributions = arrayList;
        parcel.readStringList(arrayList);
        this.mStagedActionUrl = parcel.readString();
        this.mStagedWallpaperId = parcel.readInt();
        this.mStagedRequiredNetworkState = parcel.readInt();
        this.mStagedResumeToken = parcel.readString();
        this.mStagedActionLabelRes = parcel.readInt();
        this.mStagedActionIconRes = parcel.readInt();
    }
}
