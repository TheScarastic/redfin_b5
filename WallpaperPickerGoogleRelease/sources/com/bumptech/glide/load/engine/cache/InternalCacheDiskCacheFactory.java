package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
/* loaded from: classes.dex */
public final class InternalCacheDiskCacheFactory extends DiskLruCacheFactory {
    public InternalCacheDiskCacheFactory(final Context context) {
        super(new DiskLruCacheFactory.CacheDirectoryGetter("image_manager_disk_cache") { // from class: com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory.1
        }, 262144000);
    }

    public InternalCacheDiskCacheFactory(final Context context, long j) {
        super(new DiskLruCacheFactory.CacheDirectoryGetter("image_manager_disk_cache") { // from class: com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory.1
        }, j);
    }
}
