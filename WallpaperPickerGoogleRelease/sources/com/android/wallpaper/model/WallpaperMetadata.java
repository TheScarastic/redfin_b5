package com.android.wallpaper.model;

import android.app.WallpaperInfo;
import java.util.List;
/* loaded from: classes.dex */
public class WallpaperMetadata {
    public final int mActionIconRes;
    public final int mActionLabelRes;
    public final String mActionUrl;
    public final List<String> mAttributions;
    public final String mCollectionId;
    public final WallpaperInfo mWallpaperComponent;

    public WallpaperMetadata(List<String> list, String str, int i, int i2, String str2, String str3, WallpaperInfo wallpaperInfo) {
        this.mAttributions = list;
        this.mActionUrl = str;
        this.mActionLabelRes = i;
        this.mActionIconRes = i2;
        this.mCollectionId = str2;
        this.mWallpaperComponent = wallpaperInfo;
    }
}
