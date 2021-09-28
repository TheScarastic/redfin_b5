package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.android.wallpaper.asset.Asset;
/* loaded from: classes.dex */
public abstract class Category {
    public final String mCollectionId;
    public final int mPriority;
    public final String mTitle;

    public Category(String str, String str2, int i) {
        this.mTitle = str;
        this.mCollectionId = str2;
        this.mPriority = i;
    }

    public boolean containsThirdParty(String str) {
        return false;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Category)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return TextUtils.equals(this.mCollectionId, ((Category) obj).mCollectionId);
    }

    public Drawable getOverlayIcon(Context context) {
        return null;
    }

    public int getOverlayIconSizeDp() {
        return 40;
    }

    public WallpaperInfo getSingleWallpaper() {
        return null;
    }

    public abstract Asset getThumbnail(Context context);

    public WallpaperRotationInitializer getWallpaperRotationInitializer() {
        return null;
    }

    public int hashCode() {
        String str = this.mCollectionId;
        return str == null ? super.hashCode() : str.hashCode();
    }

    public boolean isEnumerable() {
        return false;
    }

    public boolean isSingleWallpaperCategory() {
        return false;
    }

    public abstract void show(Activity activity, PickerIntentFactory pickerIntentFactory, int i);

    public boolean supportsCustomPhotos() {
        return this instanceof DesktopCustomCategory;
    }

    public boolean supportsThirdParty() {
        return this instanceof ThirdPartyAppCategory;
    }
}
