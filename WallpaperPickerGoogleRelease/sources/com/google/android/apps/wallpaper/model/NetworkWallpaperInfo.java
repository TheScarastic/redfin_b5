package com.google.android.apps.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.coordinatorlayout.R$style;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.model.InlinePreviewIntentFactory;
import com.android.wallpaper.model.WallpaperInfo;
import com.google.android.apps.wallpaper.asset.NetworkAsset;
import java.util.List;
/* loaded from: classes.dex */
public class NetworkWallpaperInfo extends WallpaperInfo {
    public static final Parcelable.Creator<NetworkWallpaperInfo> CREATOR = new Parcelable.Creator<NetworkWallpaperInfo>() { // from class: com.google.android.apps.wallpaper.model.NetworkWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public NetworkWallpaperInfo createFromParcel(Parcel parcel) {
            return new NetworkWallpaperInfo(parcel, null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public NetworkWallpaperInfo[] newArray(int i) {
            return new NetworkWallpaperInfo[i];
        }
    };
    public int mActionType;
    public String mActionUrl;
    public List<String> mAttributions;
    public String mBaseImageUrl;
    public String mCollectionId;
    public NetworkAsset mDesktopAsset;
    public Uri mDesktopImageUrl;
    public NetworkAsset mFullAsset;
    public Uri mFullImageUrl;
    public NetworkAsset mThumbAsset;
    public Uri mThumbImageUrl;
    public Uri mTinyThumbImageUrl;
    public String mWallpaperId;

    public NetworkWallpaperInfo(String str, Uri uri, Uri uri2, Uri uri3, Uri uri4, List<String> list, String str2, String str3, String str4, int i) {
        this.mBaseImageUrl = str;
        this.mFullImageUrl = uri;
        this.mThumbImageUrl = uri2;
        this.mTinyThumbImageUrl = uri3;
        this.mDesktopImageUrl = uri4;
        this.mAttributions = list;
        this.mActionUrl = str2;
        this.mCollectionId = str3;
        this.mWallpaperId = str4;
        this.mActionType = i;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public int getActionIconRes(Context context) {
        return R$style.getActionIconForType(this.mActionType);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public int getActionLabelRes(Context context) {
        return R$style.getActionLabelForType(this.mActionType);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getActionUrl(Context context) {
        return this.mActionUrl;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        if (this.mFullAsset == null) {
            this.mFullAsset = new NetworkAsset(context, this.mFullImageUrl, this.mTinyThumbImageUrl);
        }
        return this.mFullAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public List<String> getAttributions(Context context) {
        return this.mAttributions;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getBaseImageUrl() {
        return this.mBaseImageUrl;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getCollectionId(Context context) {
        return this.mCollectionId;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getDesktopAsset(Context context) {
        if (this.mDesktopAsset == null) {
            this.mDesktopAsset = new NetworkAsset(context, this.mDesktopImageUrl);
        }
        return this.mDesktopAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        if (this.mThumbAsset == null) {
            this.mThumbAsset = new NetworkAsset(context, this.mThumbImageUrl, this.mTinyThumbImageUrl);
        }
        return this.mThumbAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getWallpaperId() {
        return this.mWallpaperId;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory, int i) {
        activity.startActivityForResult(inlinePreviewIntentFactory.newIntent(activity, this), i);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlaceholderColor);
        parcel.writeString(this.mBaseImageUrl);
        parcel.writeString(this.mFullImageUrl.toString());
        parcel.writeString(this.mThumbImageUrl.toString());
        parcel.writeString(this.mTinyThumbImageUrl.toString());
        parcel.writeString(this.mDesktopImageUrl.toString());
        parcel.writeStringList(this.mAttributions);
        parcel.writeString(this.mActionUrl);
        parcel.writeString(this.mCollectionId);
        parcel.writeString(this.mWallpaperId);
        parcel.writeInt(this.mActionType);
    }

    public NetworkWallpaperInfo(Parcel parcel, AnonymousClass1 r2) {
        super(parcel);
        this.mBaseImageUrl = parcel.readString();
        this.mFullImageUrl = Uri.parse(parcel.readString());
        this.mThumbImageUrl = Uri.parse(parcel.readString());
        this.mTinyThumbImageUrl = Uri.parse(parcel.readString());
        this.mDesktopImageUrl = Uri.parse(parcel.readString());
        this.mAttributions = parcel.createStringArrayList();
        this.mActionUrl = parcel.readString();
        this.mCollectionId = parcel.readString();
        this.mWallpaperId = parcel.readString();
        this.mActionType = parcel.readInt();
    }
}
