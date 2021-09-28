package com.google.android.apps.wallpaper.model;

import android.app.WallpaperInfo;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.LiveWallpaperThumbAsset;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.google.android.apps.wallpaper.asset.GoogleLiveWallpaperThumbAsset;
/* loaded from: classes.dex */
public class GoogleLiveWallpaperInfo extends LiveWallpaperInfo {
    public static final Parcelable.Creator<GoogleLiveWallpaperInfo> CREATOR = new Parcelable.Creator<GoogleLiveWallpaperInfo>() { // from class: com.google.android.apps.wallpaper.model.GoogleLiveWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public GoogleLiveWallpaperInfo createFromParcel(Parcel parcel) {
            return new GoogleLiveWallpaperInfo(parcel);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public GoogleLiveWallpaperInfo[] newArray(int i) {
            return new GoogleLiveWallpaperInfo[i];
        }
    };

    public GoogleLiveWallpaperInfo(WallpaperInfo wallpaperInfo) {
        super(wallpaperInfo, true, null);
    }

    @Override // com.android.wallpaper.model.LiveWallpaperInfo, com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        Bundle bundle;
        String string;
        if (this.mThumbAsset == null) {
            ServiceInfo serviceInfo = this.mInfo.getServiceInfo();
            Uri parse = (serviceInfo == null || (bundle = serviceInfo.metaData) == null || (string = bundle.getString("android.service.wallpaper.thumbnail")) == null) ? null : Uri.parse(string);
            if (parse != null) {
                this.mThumbAsset = new LiveWallpaperThumbAsset(context, this.mInfo, parse);
            } else {
                this.mThumbAsset = new GoogleLiveWallpaperThumbAsset(context, this.mInfo);
            }
        }
        return this.mThumbAsset;
    }

    public GoogleLiveWallpaperInfo(WallpaperInfo wallpaperInfo, boolean z, String str) {
        super(wallpaperInfo, z, str);
    }

    public GoogleLiveWallpaperInfo(Parcel parcel) {
        super(parcel);
    }
}
