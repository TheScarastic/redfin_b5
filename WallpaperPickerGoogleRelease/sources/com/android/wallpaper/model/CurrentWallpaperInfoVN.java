package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.BuiltInWallpaperAsset;
import com.android.wallpaper.asset.CurrentWallpaperAssetVN;
import com.android.wallpaper.module.InjectorProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class CurrentWallpaperInfoVN extends WallpaperInfo {
    public static final Parcelable.Creator<CurrentWallpaperInfoVN> CREATOR = new Parcelable.Creator<CurrentWallpaperInfoVN>() { // from class: com.android.wallpaper.model.CurrentWallpaperInfoVN.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public CurrentWallpaperInfoVN createFromParcel(Parcel parcel) {
            return new CurrentWallpaperInfoVN(parcel, null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public CurrentWallpaperInfoVN[] newArray(int i) {
            return new CurrentWallpaperInfoVN[i];
        }
    };
    public int mActionIconRes;
    public int mActionLabelRes;
    public String mActionUrl;
    public Asset mAsset;
    public List<String> mAttributions;
    public String mCollectionId;
    public int mWallpaperManagerFlag;

    public CurrentWallpaperInfoVN(List<String> list, String str, int i, int i2, String str2, int i3) {
        this.mAttributions = list;
        this.mWallpaperManagerFlag = i3;
        this.mActionUrl = str;
        this.mActionLabelRes = i;
        this.mActionIconRes = i2;
        this.mCollectionId = str2;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public int getActionIconRes(Context context) {
        int i = this.mActionIconRes;
        return i != 0 ? i : R.drawable.ic_explore_24px;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public int getActionLabelRes(Context context) {
        int i = this.mActionLabelRes;
        return i != 0 ? i : R.string.explore;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getActionUrl(Context context) {
        return this.mActionUrl;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        Asset asset;
        if (this.mAsset == null) {
            boolean z = true;
            ParcelFileDescriptor wallpaperFile = InjectorProvider.getInjector().getWallpaperManagerCompat(context).getWallpaperFile(1);
            if (!(this.mWallpaperManagerFlag == 1 && wallpaperFile == null)) {
                z = false;
            }
            if (wallpaperFile != null) {
                try {
                    wallpaperFile.close();
                } catch (IOException e) {
                    Log.e("CurrentWallpaperInfoVN", "Unable to close system wallpaper ParcelFileDescriptor", e);
                }
            }
            if (z) {
                asset = new BuiltInWallpaperAsset(context);
            } else {
                asset = new CurrentWallpaperAssetVN(context, this.mWallpaperManagerFlag);
            }
            this.mAsset = asset;
        }
        return this.mAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public List<String> getAttributions(Context context) {
        return this.mAttributions;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getCollectionId(Context context) {
        return this.mCollectionId;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        return getAsset(context);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory, int i) {
        activity.startActivityForResult(inlinePreviewIntentFactory.newIntent(activity, this), i);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlaceholderColor);
        parcel.writeStringList(this.mAttributions);
        parcel.writeInt(this.mWallpaperManagerFlag);
        parcel.writeString(this.mActionUrl);
        parcel.writeString(this.mCollectionId);
        parcel.writeInt(this.mActionLabelRes);
        parcel.writeInt(this.mActionIconRes);
    }

    public CurrentWallpaperInfoVN(Parcel parcel, AnonymousClass1 r2) {
        super(parcel);
        ArrayList arrayList = new ArrayList();
        this.mAttributions = arrayList;
        parcel.readStringList(arrayList);
        this.mWallpaperManagerFlag = parcel.readInt();
        this.mActionUrl = parcel.readString();
        this.mCollectionId = parcel.readString();
        this.mActionLabelRes = parcel.readInt();
        this.mActionIconRes = parcel.readInt();
    }
}
