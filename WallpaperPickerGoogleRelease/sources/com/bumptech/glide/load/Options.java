package com.bumptech.glide.load;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.util.CachedHashCodeArrayMap;
import java.security.MessageDigest;
/* loaded from: classes.dex */
public final class Options implements Key {
    public final ArrayMap<Option<?>, Object> values = new CachedHashCodeArrayMap();

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        if (obj instanceof Options) {
            return this.values.equals(((Options) obj).values);
        }
        return false;
    }

    public <T> T get(Option<T> option) {
        if (this.values.indexOfKey(option) >= 0) {
            return (T) this.values.getOrDefault(option, null);
        }
        return option.defaultValue;
    }

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        return this.values.hashCode();
    }

    public void putAll(Options options) {
        this.values.putAll((SimpleArrayMap<? extends Option<?>, ? extends Object>) options.values);
    }

    public String toString() {
        String valueOf = String.valueOf(this.values);
        StringBuilder sb = new StringBuilder(valueOf.length() + 16);
        sb.append("Options{values=");
        sb.append(valueOf);
        sb.append('}');
        return sb.toString();
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        int i = 0;
        while (true) {
            ArrayMap<Option<?>, Object> arrayMap = this.values;
            if (i < arrayMap.mSize) {
                Option<?> keyAt = arrayMap.keyAt(i);
                Object valueAt = this.values.valueAt(i);
                Option.CacheKeyUpdater<?> cacheKeyUpdater = keyAt.cacheKeyUpdater;
                if (keyAt.keyBytes == null) {
                    keyAt.keyBytes = keyAt.key.getBytes(Key.CHARSET);
                }
                cacheKeyUpdater.update(keyAt.keyBytes, valueAt, messageDigest);
                i++;
            } else {
                return;
            }
        }
    }
}
