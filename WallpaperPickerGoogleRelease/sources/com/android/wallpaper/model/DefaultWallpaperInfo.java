package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.BuiltInWallpaperAsset;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class DefaultWallpaperInfo extends WallpaperInfo {
    public static final Parcelable.Creator<DefaultWallpaperInfo> CREATOR = new Parcelable.Creator<DefaultWallpaperInfo>() { // from class: com.android.wallpaper.model.DefaultWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public DefaultWallpaperInfo createFromParcel(Parcel parcel) {
            return new DefaultWallpaperInfo(parcel, null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public DefaultWallpaperInfo[] newArray(int i) {
            return new DefaultWallpaperInfo[i];
        }
    };
    public Asset mAsset;

    public DefaultWallpaperInfo() {
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        if (this.mAsset == null) {
            this.mAsset = new BuiltInWallpaperAsset(context);
        }
        return this.mAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public List<String> getAttributions(Context context) {
        return Arrays.asList(context.getResources().getString(R.string.fallback_wallpaper_title));
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public int getBackupPermission() {
        return 0;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getCollectionId(Context context) {
        return context.getString(R.string.on_device_wallpaper_collection_id);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        if (this.mAsset == null) {
            this.mAsset = new BuiltInWallpaperAsset(context);
        }
        return this.mAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getWallpaperId() {
        return "built-in-wallpaper";
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory, int i) {
        activity.startActivityForResult(inlinePreviewIntentFactory.newIntent(activity, this), i);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlaceholderColor);
    }

    public DefaultWallpaperInfo(Parcel parcel, AnonymousClass1 r2) {
        super(parcel);
    }
}
