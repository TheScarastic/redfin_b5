package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.ResourceAsset;
import com.android.wallpaper.module.DefaultPartnerProvider;
import com.android.wallpaper.module.InjectorProvider;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class PartnerWallpaperInfo extends WallpaperInfo {
    public static final Parcelable.Creator<PartnerWallpaperInfo> CREATOR = new Parcelable.Creator<PartnerWallpaperInfo>() { // from class: com.android.wallpaper.model.PartnerWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public PartnerWallpaperInfo createFromParcel(Parcel parcel) {
            return new PartnerWallpaperInfo(parcel, (AnonymousClass1) null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public PartnerWallpaperInfo[] newArray(int i) {
            return new PartnerWallpaperInfo[i];
        }
    };
    public ResourceAsset mAsset;
    public boolean mFetchedPartnerResources;
    public int mFullRes;
    public Resources mPartnerResources;
    public ResourceAsset mThumbAsset;
    public int mThumbRes;

    public PartnerWallpaperInfo(int i, int i2) {
        this.mThumbRes = i;
        this.mFullRes = i2;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        if (this.mAsset == null) {
            this.mAsset = new ResourceAsset(getPartnerResources(context), this.mFullRes);
        }
        return this.mAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public List<String> getAttributions(Context context) {
        return Arrays.asList(context.getResources().getString(R.string.on_device_wallpaper_title));
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public int getBackupPermission() {
        return 0;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getCollectionId(Context context) {
        return context.getString(R.string.on_device_wallpaper_collection_id);
    }

    public final Resources getPartnerResources(Context context) {
        if (!this.mFetchedPartnerResources) {
            this.mPartnerResources = ((DefaultPartnerProvider) InjectorProvider.getInjector().getPartnerProvider(context)).mResources;
            this.mFetchedPartnerResources = true;
        }
        return this.mPartnerResources;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        if (this.mThumbAsset == null) {
            this.mThumbAsset = new ResourceAsset(getPartnerResources(context), this.mThumbRes);
        }
        return this.mThumbAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory, int i) {
        activity.startActivityForResult(inlinePreviewIntentFactory.newIntent(activity, this), i);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlaceholderColor);
        parcel.writeInt(this.mThumbRes);
        parcel.writeInt(this.mFullRes);
    }

    public PartnerWallpaperInfo(Parcel parcel, AnonymousClass1 r2) {
        super(parcel);
        this.mThumbRes = parcel.readInt();
        this.mFullRes = parcel.readInt();
    }
}
