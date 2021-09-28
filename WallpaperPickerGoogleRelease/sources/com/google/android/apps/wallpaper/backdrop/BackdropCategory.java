package com.google.android.apps.wallpaper.backdrop;

import android.content.Context;
import android.os.AsyncTask;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import com.android.volley.VolleyError;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.model.WallpaperReceiver;
import com.android.wallpaper.model.WallpaperRotationInitializer;
import com.android.wallpaper.module.GoogleWallpapersInjector;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.network.ServerFetcher;
import com.google.android.apps.wallpaper.asset.NetworkAsset;
import com.google.android.apps.wallpaper.backdrop.BackdropWallpaperRotationInitializer;
import com.google.android.apps.wallpaper.model.NetworkWallpaperInfo;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Attribution;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Collection;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class BackdropCategory extends WallpaperCategory {
    public final String mCollectionName;
    public final FifeImageUrlFactory mFifeImageUrlFactory = FifeImageUrlFactory.getInstance();
    public final List<WallpaperReceiver> mPendingWallpapersReceivers = new ArrayList();
    public WallpaperRotationInitializer mRotationInitializer;
    public Asset mThumbAsset;
    public final String mThumbUrl;

    /* loaded from: classes.dex */
    public class ParseBackdropImagesAsyncTask extends AsyncTask<Void, Void, Void> {
        public final Context mAppContext;
        public final List<ImaxWallpaperProto$Image> mBackdropImages;
        public final String mCollectionId;
        public final List<WallpaperInfo> mWallpapers;

        public ParseBackdropImagesAsyncTask(List<ImaxWallpaperProto$Image> list, Context context, List<WallpaperInfo> list2, String str) {
            this.mBackdropImages = list;
            this.mAppContext = context.getApplicationContext();
            this.mWallpapers = list2;
            this.mCollectionId = str;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void[] voidArr) {
            synchronized (BackdropCategory.this.mWallpapersLock) {
                if (this.mWallpapers.size() > 0) {
                    return null;
                }
                for (ImaxWallpaperProto$Image imaxWallpaperProto$Image : this.mBackdropImages) {
                    ArrayList arrayList = new ArrayList();
                    for (ImaxWallpaperProto$Attribution imaxWallpaperProto$Attribution : imaxWallpaperProto$Image.getAttributionList()) {
                        arrayList.add(imaxWallpaperProto$Attribution.getText());
                    }
                    if (arrayList.size() == 0) {
                        arrayList.add(BackdropCategory.this.mTitle);
                    }
                    this.mWallpapers.add(new NetworkWallpaperInfo(imaxWallpaperProto$Image.getImageUrl(), BackdropCategory.this.mFifeImageUrlFactory.createFullSizedUri(this.mAppContext, imaxWallpaperProto$Image.getImageUrl()), BackdropCategory.this.mFifeImageUrlFactory.createThumbUri(this.mAppContext, imaxWallpaperProto$Image.getImageUrl()), BackdropCategory.this.mFifeImageUrlFactory.createTinyUri(imaxWallpaperProto$Image.getImageUrl()), BackdropCategory.this.mFifeImageUrlFactory.createDesktopUri(this.mAppContext, imaxWallpaperProto$Image.getImageUrl()), arrayList, imaxWallpaperProto$Image.getActionUrl(), this.mCollectionId, String.valueOf(imaxWallpaperProto$Image.getAssetId()), imaxWallpaperProto$Image.getType().getNumber()));
                }
                return null;
            }
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void r4) {
            synchronized (BackdropCategory.this.mWallpapersLock) {
                for (WallpaperReceiver wallpaperReceiver : BackdropCategory.this.mPendingWallpapersReceivers) {
                    if (wallpaperReceiver != null) {
                        wallpaperReceiver.onWallpapersReceived(this.mWallpapers);
                    }
                }
                BackdropCategory.this.mPendingWallpapersReceivers.clear();
            }
        }
    }

    public BackdropCategory(ImaxWallpaperProto$Collection imaxWallpaperProto$Collection, int i) {
        super(imaxWallpaperProto$Collection.getCollectionName(), imaxWallpaperProto$Collection.getCollectionId(), new ArrayList(), i);
        this.mCollectionName = imaxWallpaperProto$Collection.getCollectionName();
        this.mThumbUrl = imaxWallpaperProto$Collection.getPreview(0).getImageUrl();
    }

    @Override // com.android.wallpaper.model.WallpaperCategory
    public void fetchWallpapers(final Context context, WallpaperReceiver wallpaperReceiver, boolean z) {
        ServerFetcher serverFetcher = ((GoogleWallpapersInjector) InjectorProvider.getInjector()).getServerFetcher(context);
        synchronized (this.mWallpapersLock) {
            final List<WallpaperInfo> list = this.mWallpapers;
            if (list.size() <= 0 || wallpaperReceiver == null || z) {
                if (z) {
                    list.clear();
                }
                boolean z2 = this.mPendingWallpapersReceivers.size() > 0;
                this.mPendingWallpapersReceivers.add(wallpaperReceiver);
                if (!z2) {
                    final String str = this.mCollectionId;
                    ((BackdropFetcher) serverFetcher).fetchImagesInCollection(context, str, new ServerFetcher.ResultsCallback<ImaxWallpaperProto$Image>() { // from class: com.google.android.apps.wallpaper.backdrop.BackdropCategory.2
                        @Override // com.android.wallpaper.network.ServerFetcher.ResultsCallback
                        public void onError(VolleyError volleyError) {
                            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Unable to fetch Backdrop wallpapers for the collection ID: ");
                            m.append(BackdropCategory.this.mCollectionId);
                            Log.e("BackdropCategory", m.toString(), volleyError);
                        }

                        @Override // com.android.wallpaper.network.ServerFetcher.ResultsCallback
                        public void onSuccess(List<ImaxWallpaperProto$Image> list2) {
                            new ParseBackdropImagesAsyncTask(list2, context, list, str).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                        }
                    });
                    return;
                }
                return;
            }
            wallpaperReceiver.onWallpapersReceived(list);
        }
    }

    @Override // com.android.wallpaper.model.WallpaperCategory, com.android.wallpaper.model.Category
    public Asset getThumbnail(Context context) {
        if (this.mThumbAsset == null) {
            this.mThumbAsset = new NetworkAsset(context, this.mFifeImageUrlFactory.createThumbUri(context, this.mThumbUrl), this.mFifeImageUrlFactory.createTinyUri(this.mThumbUrl));
        }
        return this.mThumbAsset;
    }

    @Override // com.android.wallpaper.model.Category
    public WallpaperRotationInitializer getWallpaperRotationInitializer() {
        String str = this.mCollectionId;
        if (str != null && str.endsWith("_no_rotate")) {
            return null;
        }
        if (this.mRotationInitializer == null) {
            WallpaperRotationInitializerFactory instance = WallpaperRotationInitializerFactory.getInstance();
            String str2 = this.mCollectionId;
            String str3 = this.mCollectionName;
            Objects.requireNonNull((BackdropWallpaperRotationInitializer.Factory) instance);
            this.mRotationInitializer = new BackdropWallpaperRotationInitializer(str2, str3);
        }
        return this.mRotationInitializer;
    }
}
