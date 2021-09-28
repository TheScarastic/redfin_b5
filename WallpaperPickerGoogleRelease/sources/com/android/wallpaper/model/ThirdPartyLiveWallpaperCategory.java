package com.android.wallpaper.model;

import android.app.WallpaperInfo;
import android.content.Context;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class ThirdPartyLiveWallpaperCategory extends WallpaperCategory {
    public final Set<String> mExcludedPackages;

    /* loaded from: classes.dex */
    public class FetchLiveWallpapersTask extends AsyncTask<Void, Void, Void> {
        public final List<WallpaperInfo> mCategoryWallpapers;
        public final Context mContext;
        public final Set<String> mExcludedPackages;
        public final WallpaperReceiver mReceiver;

        public FetchLiveWallpapersTask(Context context, List<WallpaperInfo> list, Set<String> set, WallpaperReceiver wallpaperReceiver) {
            this.mContext = context;
            this.mCategoryWallpapers = list;
            this.mExcludedPackages = set;
            this.mReceiver = wallpaperReceiver;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void[] voidArr) {
            List<WallpaperInfo> all = LiveWallpaperInfo.getAll(this.mContext, this.mExcludedPackages);
            synchronized (ThirdPartyLiveWallpaperCategory.this.mWallpapersLock) {
                this.mCategoryWallpapers.clear();
                this.mCategoryWallpapers.addAll(all);
            }
            return null;
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void r2) {
            this.mReceiver.onWallpapersReceived(new ArrayList(this.mCategoryWallpapers));
        }
    }

    public ThirdPartyLiveWallpaperCategory(String str, String str2, List<WallpaperInfo> list, int i, Set<String> set) {
        super(str, str2, list, i);
        this.mExcludedPackages = set;
    }

    @Override // com.android.wallpaper.model.WallpaperCategory, com.android.wallpaper.model.Category
    public boolean containsThirdParty(String str) {
        synchronized (this.mWallpapersLock) {
            for (WallpaperInfo wallpaperInfo : this.mWallpapers) {
                WallpaperInfo wallpaperComponent = wallpaperInfo.getWallpaperComponent();
                if (wallpaperComponent != null && wallpaperComponent.getPackageName().equals(str)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // com.android.wallpaper.model.WallpaperCategory
    public void fetchWallpapers(Context context, WallpaperReceiver wallpaperReceiver, boolean z) {
        if (z) {
            new FetchLiveWallpapersTask(context, this.mWallpapers, this.mExcludedPackages, wallpaperReceiver).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else {
            super.fetchWallpapers(context, wallpaperReceiver, z);
        }
    }
}
