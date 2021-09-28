package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class EngineKey implements Key {
    public int hashCode;
    public final int height;
    public final Object model;
    public final Options options;
    public final Class<?> resourceClass;
    public final Key signature;
    public final Class<?> transcodeClass;
    public final Map<Class<?>, Transformation<?>> transformations;
    public final int width;

    public EngineKey(Object obj, Key key, int i, int i2, Map<Class<?>, Transformation<?>> map, Class<?> cls, Class<?> cls2, Options options) {
        Objects.requireNonNull(obj, "Argument must not be null");
        this.model = obj;
        Objects.requireNonNull(key, "Signature must not be null");
        this.signature = key;
        this.width = i;
        this.height = i2;
        Objects.requireNonNull(map, "Argument must not be null");
        this.transformations = map;
        Objects.requireNonNull(cls, "Resource class must not be null");
        this.resourceClass = cls;
        Objects.requireNonNull(cls2, "Transcode class must not be null");
        this.transcodeClass = cls2;
        Objects.requireNonNull(options, "Argument must not be null");
        this.options = options;
    }

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        if (!(obj instanceof EngineKey)) {
            return false;
        }
        EngineKey engineKey = (EngineKey) obj;
        if (!this.model.equals(engineKey.model) || !this.signature.equals(engineKey.signature) || this.height != engineKey.height || this.width != engineKey.width || !this.transformations.equals(engineKey.transformations) || !this.resourceClass.equals(engineKey.resourceClass) || !this.transcodeClass.equals(engineKey.transcodeClass) || !this.options.equals(engineKey.options)) {
            return false;
        }
        return true;
    }

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        if (this.hashCode == 0) {
            int hashCode = this.model.hashCode();
            this.hashCode = hashCode;
            int hashCode2 = this.signature.hashCode() + (hashCode * 31);
            this.hashCode = hashCode2;
            int i = (hashCode2 * 31) + this.width;
            this.hashCode = i;
            int i2 = (i * 31) + this.height;
            this.hashCode = i2;
            int hashCode3 = this.transformations.hashCode() + (i2 * 31);
            this.hashCode = hashCode3;
            int hashCode4 = this.resourceClass.hashCode() + (hashCode3 * 31);
            this.hashCode = hashCode4;
            int hashCode5 = this.transcodeClass.hashCode() + (hashCode4 * 31);
            this.hashCode = hashCode5;
            this.hashCode = this.options.hashCode() + (hashCode5 * 31);
        }
        return this.hashCode;
    }

    public String toString() {
        String valueOf = String.valueOf(this.model);
        int i = this.width;
        int i2 = this.height;
        String valueOf2 = String.valueOf(this.resourceClass);
        String valueOf3 = String.valueOf(this.transcodeClass);
        String valueOf4 = String.valueOf(this.signature);
        int i3 = this.hashCode;
        String valueOf5 = String.valueOf(this.transformations);
        String valueOf6 = String.valueOf(this.options);
        StringBuilder sb = new StringBuilder(valueOf6.length() + valueOf5.length() + valueOf4.length() + valueOf3.length() + valueOf2.length() + valueOf.length() + 151);
        sb.append("EngineKey{model=");
        sb.append(valueOf);
        sb.append(", width=");
        sb.append(i);
        sb.append(", height=");
        sb.append(i2);
        sb.append(", resourceClass=");
        sb.append(valueOf2);
        sb.append(", transcodeClass=");
        sb.append(valueOf3);
        sb.append(", signature=");
        sb.append(valueOf4);
        sb.append(", hashCode=");
        sb.append(i3);
        sb.append(", transformations=");
        sb.append(valueOf5);
        sb.append(", options=");
        sb.append(valueOf6);
        sb.append('}');
        return sb.toString();
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        throw new UnsupportedOperationException();
    }
}
