package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.ResourceAsset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class AppResourceWallpaperInfo extends WallpaperInfo {
    public static final Parcelable.Creator<AppResourceWallpaperInfo> CREATOR = new Parcelable.Creator<AppResourceWallpaperInfo>() { // from class: com.android.wallpaper.model.AppResourceWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public AppResourceWallpaperInfo createFromParcel(Parcel parcel) {
            return new AppResourceWallpaperInfo(parcel, null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public AppResourceWallpaperInfo[] newArray(int i) {
            return new AppResourceWallpaperInfo[i];
        }
    };
    public ResourceAsset mAsset;
    public int mFullRes;
    public String mPackageName;
    public Resources mResources;
    public ResourceAsset mThumbAsset;
    public int mThumbRes;

    public AppResourceWallpaperInfo(String str, int i, int i2) {
        this.mPackageName = str;
        this.mThumbRes = i;
        this.mFullRes = i2;
    }

    public static List<WallpaperInfo> getAll(Context context, ApplicationInfo applicationInfo, int i) {
        ArrayList arrayList = new ArrayList();
        try {
            Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication(applicationInfo);
            String[] stringArray = resourcesForApplication.getStringArray(i);
            for (String str : stringArray) {
                int identifier = resourcesForApplication.getIdentifier(str, "drawable", applicationInfo.packageName);
                int identifier2 = resourcesForApplication.getIdentifier(str + "_small", "drawable", applicationInfo.packageName);
                if (!(identifier == 0 || identifier2 == 0)) {
                    arrayList.add(new AppResourceWallpaperInfo(applicationInfo.packageName, identifier2, identifier));
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("AppResource", "Hosting app package not found");
        }
        return arrayList;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AppResourceWallpaperInfo)) {
            return false;
        }
        AppResourceWallpaperInfo appResourceWallpaperInfo = (AppResourceWallpaperInfo) obj;
        return this.mPackageName.equals(appResourceWallpaperInfo.mPackageName) && this.mThumbRes == appResourceWallpaperInfo.mThumbRes && this.mFullRes == appResourceWallpaperInfo.mFullRes;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        if (this.mAsset == null) {
            this.mAsset = new ResourceAsset(getPackageResources(context), this.mFullRes);
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

    public final Resources getPackageResources(Context context) {
        Resources resources = this.mResources;
        if (resources != null) {
            return resources;
        }
        try {
            this.mResources = context.getPackageManager().getResourcesForApplication(this.mPackageName);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("AppResource", "Could not get app resources");
        }
        return this.mResources;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        if (this.mThumbAsset == null) {
            this.mThumbAsset = new ResourceAsset(getPackageResources(context), this.mThumbRes);
        }
        return this.mThumbAsset;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return ((((this.mPackageName.hashCode() + 527) * 31) + this.mThumbRes) * 31) + this.mFullRes;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory, int i) {
        activity.startActivityForResult(inlinePreviewIntentFactory.newIntent(activity, this), i);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlaceholderColor);
        parcel.writeString(this.mPackageName);
        parcel.writeInt(this.mThumbRes);
        parcel.writeInt(this.mFullRes);
    }

    public AppResourceWallpaperInfo(Parcel parcel, AnonymousClass1 r2) {
        super(parcel);
        this.mPackageName = parcel.readString();
        this.mThumbRes = parcel.readInt();
        this.mFullRes = parcel.readInt();
    }
}
