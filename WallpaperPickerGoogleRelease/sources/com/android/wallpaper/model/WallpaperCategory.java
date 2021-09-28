package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.picker.individual.IndividualPickerActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class WallpaperCategory extends Category {
    public int mFeaturedThumbnailIndex;
    public Asset mThumbAsset;
    public final List<WallpaperInfo> mWallpapers;
    public final Object mWallpapersLock;

    public WallpaperCategory(String str, String str2, int i, List<WallpaperInfo> list, int i2) {
        super(str, str2, i2);
        this.mWallpapers = list;
        this.mWallpapersLock = new Object();
        this.mFeaturedThumbnailIndex = i;
    }

    @Override // com.android.wallpaper.model.Category
    public boolean containsThirdParty(String str) {
        return false;
    }

    public void fetchWallpapers(Context context, WallpaperReceiver wallpaperReceiver, boolean z) {
        wallpaperReceiver.onWallpapersReceived(new ArrayList(this.mWallpapers));
    }

    @Override // com.android.wallpaper.model.Category
    public WallpaperInfo getSingleWallpaper() {
        List<WallpaperInfo> list = this.mWallpapers;
        boolean z = true;
        if (list == null || list.size() != 1) {
            z = false;
        }
        if (z) {
            return this.mWallpapers.get(0);
        }
        return null;
    }

    @Override // com.android.wallpaper.model.Category
    public Asset getThumbnail(Context context) {
        synchronized (this.mWallpapersLock) {
            if (this.mThumbAsset == null && this.mWallpapers.size() > 0) {
                this.mThumbAsset = this.mWallpapers.get(this.mFeaturedThumbnailIndex).getThumbAsset(context);
            }
        }
        return this.mThumbAsset;
    }

    @Override // com.android.wallpaper.model.Category
    public boolean isEnumerable() {
        return true;
    }

    @Override // com.android.wallpaper.model.Category
    public boolean isSingleWallpaperCategory() {
        List<WallpaperInfo> list = this.mWallpapers;
        return list != null && list.size() == 1;
    }

    @Override // com.android.wallpaper.model.Category
    public void show(Activity activity, PickerIntentFactory pickerIntentFactory, int i) {
        String str = this.mCollectionId;
        Objects.requireNonNull((IndividualPickerActivity.IndividualPickerActivityIntentFactory) pickerIntentFactory);
        activity.startActivityForResult(new Intent(activity, IndividualPickerActivity.class).putExtra("com.android.wallpaper.category_collection_id", str), i);
    }

    @Override // com.android.wallpaper.model.Category
    public boolean supportsThirdParty() {
        return this instanceof ThirdPartyLiveWallpaperCategory;
    }

    public WallpaperCategory(String str, String str2, List<WallpaperInfo> list, int i) {
        super(str, str2, i);
        this.mWallpapers = list;
        this.mWallpapersLock = new Object();
        this.mFeaturedThumbnailIndex = 0;
    }

    public WallpaperCategory(String str, String str2, Asset asset, List<WallpaperInfo> list, int i) {
        super(str, str2, i);
        this.mWallpapers = list;
        this.mWallpapersLock = new Object();
        this.mThumbAsset = asset;
    }
}
