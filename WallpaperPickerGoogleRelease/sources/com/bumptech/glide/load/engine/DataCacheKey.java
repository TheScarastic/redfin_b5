package com.bumptech.glide.load.engine;

import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.bumptech.glide.load.Key;
import java.security.MessageDigest;
/* loaded from: classes.dex */
public final class DataCacheKey implements Key {
    public final Key signature;
    public final Key sourceKey;

    public DataCacheKey(Key key, Key key2) {
        this.sourceKey = key;
        this.signature = key2;
    }

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        if (!(obj instanceof DataCacheKey)) {
            return false;
        }
        DataCacheKey dataCacheKey = (DataCacheKey) obj;
        if (!this.sourceKey.equals(dataCacheKey.sourceKey) || !this.signature.equals(dataCacheKey.signature)) {
            return false;
        }
        return true;
    }

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        return this.signature.hashCode() + (this.sourceKey.hashCode() * 31);
    }

    public String toString() {
        String valueOf = String.valueOf(this.sourceKey);
        String valueOf2 = String.valueOf(this.signature);
        StringBuilder m = R$string$$ExternalSyntheticOutline0.m(valueOf2.length() + valueOf.length() + 36, "DataCacheKey{sourceKey=", valueOf, ", signature=", valueOf2);
        m.append('}');
        return m.toString();
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        this.sourceKey.updateDiskCacheKey(messageDigest);
        this.signature.updateDiskCacheKey(messageDigest);
    }
}
