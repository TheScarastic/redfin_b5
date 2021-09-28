package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.FileAsset;
import com.android.wallpaper.module.DefaultPartnerProvider;
import com.android.wallpaper.module.InjectorProvider;
import java.io.File;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class LegacyPartnerWallpaperInfo extends WallpaperInfo {
    public static final Parcelable.Creator<LegacyPartnerWallpaperInfo> CREATOR = new Parcelable.Creator<LegacyPartnerWallpaperInfo>() { // from class: com.android.wallpaper.model.LegacyPartnerWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public LegacyPartnerWallpaperInfo createFromParcel(Parcel parcel) {
            return new LegacyPartnerWallpaperInfo(parcel, (AnonymousClass1) null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public LegacyPartnerWallpaperInfo[] newArray(int i) {
            return new LegacyPartnerWallpaperInfo[i];
        }
    };
    public FileAsset mAsset;
    public boolean mFetchedSystemLegacyDir;
    public String mFullName;
    public File mSystemLegacyDir;
    public FileAsset mThumbAsset;
    public String mThumbName;

    public LegacyPartnerWallpaperInfo(String str, String str2) {
        this.mThumbName = str;
        this.mFullName = str2;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        File file;
        if (this.mAsset == null) {
            File systemLegacyDir = getSystemLegacyDir(context);
            if (systemLegacyDir == null) {
                file = null;
            } else {
                file = new File(systemLegacyDir, this.mFullName);
            }
            this.mAsset = new FileAsset(file);
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

    public final File getSystemLegacyDir(Context context) {
        if (!this.mFetchedSystemLegacyDir) {
            this.mSystemLegacyDir = ((DefaultPartnerProvider) InjectorProvider.getInjector().getPartnerProvider(context)).getLegacyWallpaperDirectory();
            this.mFetchedSystemLegacyDir = true;
        }
        return this.mSystemLegacyDir;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        File file;
        if (this.mThumbAsset == null) {
            File systemLegacyDir = getSystemLegacyDir(context);
            if (systemLegacyDir == null) {
                file = null;
            } else {
                file = new File(systemLegacyDir, this.mThumbName);
            }
            this.mThumbAsset = new FileAsset(file);
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
        parcel.writeString(this.mThumbName);
        parcel.writeString(this.mFullName);
    }

    public LegacyPartnerWallpaperInfo(Parcel parcel, AnonymousClass1 r2) {
        super(parcel);
        this.mThumbName = parcel.readString();
        this.mFullName = parcel.readString();
    }
}
