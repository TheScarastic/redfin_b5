package com.android.wallpaper.asset;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.io.InputStream;
import java.security.MessageDigest;
/* loaded from: classes.dex */
public class ResourceAsset extends StreamableAsset {
    public Key mKey;
    public final RequestOptions mRequestOptions;
    public final Resources mRes;
    public final int mResId;

    /* loaded from: classes.dex */
    public static class PackageResourceKey implements Key {
        public String mPackageName;
        public int mResId;

        public PackageResourceKey(Resources resources, int i) {
            this.mPackageName = resources.getResourcePackageName(i);
            this.mResId = i;
        }

        @Override // com.bumptech.glide.load.Key
        public boolean equals(Object obj) {
            if (obj instanceof PackageResourceKey) {
                return getCacheKey().equals(((PackageResourceKey) obj).getCacheKey());
            }
            return false;
        }

        public String getCacheKey() {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("PackageResourceKey{packageName=");
            m.append(this.mPackageName);
            m.append(",resId=");
            m.append(this.mResId);
            m.append('}');
            return m.toString();
        }

        @Override // com.bumptech.glide.load.Key
        public int hashCode() {
            return getCacheKey().hashCode();
        }

        public String toString() {
            return getCacheKey();
        }

        @Override // com.bumptech.glide.load.Key
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update(getCacheKey().getBytes(Key.CHARSET));
        }
    }

    public ResourceAsset(Resources resources, int i) {
        RequestOptions centerCropTransform = RequestOptions.centerCropTransform();
        this.mRes = resources;
        this.mResId = i;
        this.mRequestOptions = centerCropTransform;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ResourceAsset) {
            return getKey().equals(((ResourceAsset) obj).getKey());
        }
        return false;
    }

    public Key getKey() {
        if (this.mKey == null) {
            this.mKey = new PackageResourceKey(this.mRes, this.mResId);
        }
        return this.mKey;
    }

    public int hashCode() {
        return getKey().hashCode();
    }

    @Override // com.android.wallpaper.asset.Asset
    public void loadDrawable(Context context, ImageView imageView, int i) {
        RequestBuilder<Drawable> asDrawable = Glide.with(context).asDrawable();
        asDrawable.model = this;
        asDrawable.isModelSet = true;
        RequestBuilder<Drawable> apply = asDrawable.apply((BaseRequestOptions<?>) this.mRequestOptions.placeholder(new ColorDrawable(i)));
        apply.transition(DrawableTransitionOptions.withCrossFade());
        apply.into(imageView);
    }

    @Override // com.android.wallpaper.asset.StreamableAsset
    public InputStream openInputStream() {
        return this.mRes.openRawResource(this.mResId);
    }
}
