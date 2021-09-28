package com.android.wallpaper.asset;

import android.content.res.Resources;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import com.android.wallpaper.asset.ResourceAsset;
import com.bumptech.glide.load.Key;
/* loaded from: classes.dex */
public final class SystemStaticAsset extends ResourceAsset {
    public final String mResName;

    /* loaded from: classes.dex */
    public static class PackageResourceKey extends ResourceAsset.PackageResourceKey {
        public String mResName;

        public PackageResourceKey(Resources resources, int i, String str) {
            super(resources, i);
            this.mResName = str;
        }

        @Override // com.android.wallpaper.asset.ResourceAsset.PackageResourceKey
        public String getCacheKey() {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("PackageResourceKey{packageName=");
            m.append(this.mPackageName);
            m.append(",resId=");
            m.append(this.mResId);
            m.append(",resName=");
            m.append(this.mResName);
            m.append('}');
            return m.toString();
        }
    }

    public SystemStaticAsset(Resources resources, int i, String str) {
        super(resources, i);
        this.mResName = str;
    }

    @Override // com.android.wallpaper.asset.ResourceAsset
    public Key getKey() {
        if (this.mKey == null) {
            this.mKey = new PackageResourceKey(this.mRes, this.mResId, this.mResName);
        }
        return this.mKey;
    }
}
