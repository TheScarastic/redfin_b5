package com.bumptech.glide.load.model;

import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.load.Key;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Objects;
/* loaded from: classes.dex */
public class GlideUrl implements Key {
    public volatile byte[] cacheKeyBytes;
    public int hashCode;
    public final Headers headers;
    public String safeStringUrl;
    public URL safeUrl;
    public final String stringUrl;
    public final URL url;

    public GlideUrl(URL url) {
        Headers headers = Headers.DEFAULT;
        Objects.requireNonNull(url, "Argument must not be null");
        this.url = url;
        this.stringUrl = null;
        Objects.requireNonNull(headers, "Argument must not be null");
        this.headers = headers;
    }

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        if (!(obj instanceof GlideUrl)) {
            return false;
        }
        GlideUrl glideUrl = (GlideUrl) obj;
        if (!getCacheKey().equals(glideUrl.getCacheKey()) || !this.headers.equals(glideUrl.headers)) {
            return false;
        }
        return true;
    }

    public String getCacheKey() {
        String str = this.stringUrl;
        if (str != null) {
            return str;
        }
        URL url = this.url;
        Objects.requireNonNull(url, "Argument must not be null");
        return url.toString();
    }

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        if (this.hashCode == 0) {
            int hashCode = getCacheKey().hashCode();
            this.hashCode = hashCode;
            this.hashCode = this.headers.hashCode() + (hashCode * 31);
        }
        return this.hashCode;
    }

    public String toString() {
        return getCacheKey();
    }

    public URL toURL() throws MalformedURLException {
        if (this.safeUrl == null) {
            if (TextUtils.isEmpty(this.safeStringUrl)) {
                String str = this.stringUrl;
                if (TextUtils.isEmpty(str)) {
                    URL url = this.url;
                    Objects.requireNonNull(url, "Argument must not be null");
                    str = url.toString();
                }
                this.safeStringUrl = Uri.encode(str, "@#&=*+-_.,:!?()/~'%;$");
            }
            this.safeUrl = new URL(this.safeStringUrl);
        }
        return this.safeUrl;
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        if (this.cacheKeyBytes == null) {
            this.cacheKeyBytes = getCacheKey().getBytes(Key.CHARSET);
        }
        messageDigest.update(this.cacheKeyBytes);
    }

    public GlideUrl(String str) {
        Headers headers = Headers.DEFAULT;
        this.url = null;
        if (!TextUtils.isEmpty(str)) {
            this.stringUrl = str;
            Objects.requireNonNull(headers, "Argument must not be null");
            this.headers = headers;
            return;
        }
        throw new IllegalArgumentException("Must not be null or empty");
    }
}
