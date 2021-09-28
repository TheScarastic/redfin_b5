package com.bumptech.glide.load.engine;

import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
/* loaded from: classes.dex */
public final class ResourceCacheKey implements Key {
    public static final LruCache<Class<?>, byte[]> RESOURCE_CLASS_BYTES = new LruCache<>(50);
    public final ArrayPool arrayPool;
    public final Class<?> decodedResourceClass;
    public final int height;
    public final Options options;
    public final Key signature;
    public final Key sourceKey;
    public final Transformation<?> transformation;
    public final int width;

    public ResourceCacheKey(ArrayPool arrayPool, Key key, Key key2, int i, int i2, Transformation<?> transformation, Class<?> cls, Options options) {
        this.arrayPool = arrayPool;
        this.sourceKey = key;
        this.signature = key2;
        this.width = i;
        this.height = i2;
        this.transformation = transformation;
        this.decodedResourceClass = cls;
        this.options = options;
    }

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        if (!(obj instanceof ResourceCacheKey)) {
            return false;
        }
        ResourceCacheKey resourceCacheKey = (ResourceCacheKey) obj;
        if (this.height != resourceCacheKey.height || this.width != resourceCacheKey.width || !Util.bothNullOrEqual(this.transformation, resourceCacheKey.transformation) || !this.decodedResourceClass.equals(resourceCacheKey.decodedResourceClass) || !this.sourceKey.equals(resourceCacheKey.sourceKey) || !this.signature.equals(resourceCacheKey.signature) || !this.options.equals(resourceCacheKey.options)) {
            return false;
        }
        return true;
    }

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        int hashCode = ((((this.signature.hashCode() + (this.sourceKey.hashCode() * 31)) * 31) + this.width) * 31) + this.height;
        Transformation<?> transformation = this.transformation;
        if (transformation != null) {
            hashCode = (hashCode * 31) + transformation.hashCode();
        }
        int hashCode2 = this.decodedResourceClass.hashCode();
        return this.options.hashCode() + ((hashCode2 + (hashCode * 31)) * 31);
    }

    public String toString() {
        String valueOf = String.valueOf(this.sourceKey);
        String valueOf2 = String.valueOf(this.signature);
        int i = this.width;
        int i2 = this.height;
        String valueOf3 = String.valueOf(this.decodedResourceClass);
        String valueOf4 = String.valueOf(this.transformation);
        String valueOf5 = String.valueOf(this.options);
        StringBuilder m = R$string$$ExternalSyntheticOutline0.m(valueOf5.length() + valueOf4.length() + valueOf3.length() + valueOf2.length() + valueOf.length() + 131, "ResourceCacheKey{sourceKey=", valueOf, ", signature=", valueOf2);
        m.append(", width=");
        m.append(i);
        m.append(", height=");
        m.append(i2);
        m.append(", decodedResourceClass=");
        m.append(valueOf3);
        m.append(", transformation='");
        m.append(valueOf4);
        m.append('\'');
        m.append(", options=");
        m.append(valueOf5);
        m.append('}');
        return m.toString();
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        byte[] bArr = (byte[]) this.arrayPool.getExact(8, byte[].class);
        ByteBuffer.wrap(bArr).putInt(this.width).putInt(this.height).array();
        this.signature.updateDiskCacheKey(messageDigest);
        this.sourceKey.updateDiskCacheKey(messageDigest);
        messageDigest.update(bArr);
        Transformation<?> transformation = this.transformation;
        if (transformation != null) {
            transformation.updateDiskCacheKey(messageDigest);
        }
        this.options.updateDiskCacheKey(messageDigest);
        LruCache<Class<?>, byte[]> lruCache = RESOURCE_CLASS_BYTES;
        byte[] bArr2 = lruCache.get(this.decodedResourceClass);
        if (bArr2 == null) {
            bArr2 = this.decodedResourceClass.getName().getBytes(Key.CHARSET);
            lruCache.put(this.decodedResourceClass, bArr2);
        }
        messageDigest.update(bArr2);
        this.arrayPool.put(bArr);
    }
}
