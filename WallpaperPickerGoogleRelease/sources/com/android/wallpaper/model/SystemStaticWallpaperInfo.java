package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.ResourceAsset;
import com.android.wallpaper.asset.SystemStaticAsset;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class SystemStaticWallpaperInfo extends WallpaperInfo {
    public static final Parcelable.Creator<SystemStaticWallpaperInfo> CREATOR = new Parcelable.Creator<SystemStaticWallpaperInfo>() { // from class: com.android.wallpaper.model.SystemStaticWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public SystemStaticWallpaperInfo createFromParcel(Parcel parcel) {
            return new SystemStaticWallpaperInfo(parcel, null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public SystemStaticWallpaperInfo[] newArray(int i) {
            return new SystemStaticWallpaperInfo[i];
        }
    };
    public final int mActionTypeResId;
    public String mActionUrl;
    public final int mActionUrlResId;
    public ResourceAsset mAsset;
    public List<String> mAttributions;
    public final String mCollectionId;
    public final int mDrawableResId;
    public final String mPackageName;
    public Resources mResources;
    public final int mSubtitle1ResId;
    public final int mSubtitle2ResId;
    public final int mTitleResId;
    public final String mWallpaperId;

    public SystemStaticWallpaperInfo(String str, String str2, String str3, int i, int i2, int i3, int i4, int i5, int i6) {
        this.mPackageName = str;
        this.mWallpaperId = str2;
        this.mCollectionId = str3;
        this.mDrawableResId = i;
        this.mTitleResId = i2;
        this.mSubtitle1ResId = i3;
        this.mSubtitle2ResId = i4;
        this.mActionTypeResId = i5;
        this.mActionUrlResId = i6;
    }

    public static SystemStaticWallpaperInfo fromAttributeSet(String str, String str2, AttributeSet attributeSet) {
        String attributeValue = attributeSet.getAttributeValue(null, "id");
        if (TextUtils.isEmpty(attributeValue)) {
            return null;
        }
        return new SystemStaticWallpaperInfo(str, attributeValue, str2, attributeSet.getAttributeResourceValue(null, "src", 0), attributeSet.getAttributeResourceValue(null, "title", 0), attributeSet.getAttributeResourceValue(null, "subtitle1", 0), attributeSet.getAttributeResourceValue(null, "subtitle2", 0), 0, attributeSet.getAttributeResourceValue(null, "actionUrl", 0));
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public int getActionIconRes(Context context) {
        return R.drawable.ic_explore_24px;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public int getActionLabelRes(Context context) {
        return R.string.explore;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getActionUrl(Context context) {
        if (this.mActionUrl == null && this.mActionUrlResId != 0) {
            this.mActionUrl = getPackageResources(context).getString(this.mActionUrlResId);
        }
        return this.mActionUrl;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        if (this.mAsset == null) {
            this.mAsset = new SystemStaticAsset(getPackageResources(context), this.mDrawableResId, this.mWallpaperId);
        }
        return this.mAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public List<String> getAttributions(Context context) {
        if (this.mAttributions == null) {
            Resources packageResources = getPackageResources(context);
            ArrayList arrayList = new ArrayList();
            this.mAttributions = arrayList;
            int i = this.mTitleResId;
            if (i != 0) {
                arrayList.add(packageResources.getString(i));
            }
            int i2 = this.mSubtitle1ResId;
            if (i2 != 0) {
                this.mAttributions.add(packageResources.getString(i2));
            }
            int i3 = this.mSubtitle2ResId;
            if (i3 != 0) {
                this.mAttributions.add(packageResources.getString(i3));
            }
        }
        return this.mAttributions;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getCollectionId(Context context) {
        return this.mCollectionId;
    }

    public final Resources getPackageResources(Context context) {
        Resources resources = this.mResources;
        if (resources != null) {
            return resources;
        }
        try {
            this.mResources = context.getPackageManager().getResourcesForApplication(this.mPackageName);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("PartnerStaticWPInfo", "Could not get app resources");
        }
        return this.mResources;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        return getAsset(context);
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
        parcel.writeString(this.mPackageName);
        parcel.writeString(this.mWallpaperId);
        parcel.writeString(this.mCollectionId);
        parcel.writeInt(this.mDrawableResId);
        parcel.writeInt(this.mTitleResId);
        parcel.writeInt(this.mSubtitle1ResId);
        parcel.writeInt(this.mSubtitle2ResId);
        parcel.writeInt(this.mActionTypeResId);
        parcel.writeInt(this.mActionUrlResId);
    }

    public SystemStaticWallpaperInfo(Parcel parcel, AnonymousClass1 r2) {
        super(parcel);
        this.mPackageName = parcel.readString();
        this.mWallpaperId = parcel.readString();
        this.mCollectionId = parcel.readString();
        this.mDrawableResId = parcel.readInt();
        this.mTitleResId = parcel.readInt();
        this.mSubtitle1ResId = parcel.readInt();
        this.mSubtitle2ResId = parcel.readInt();
        this.mActionTypeResId = parcel.readInt();
        this.mActionUrlResId = parcel.readInt();
    }
}
