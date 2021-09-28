package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
/* loaded from: classes.dex */
public class LruResourceCache extends LruCache<Key, Resource<?>> implements MemoryCache {
    public MemoryCache.ResourceRemovedListener listener;

    public LruResourceCache(long j) {
        super(j);
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // com.bumptech.glide.util.LruCache
    public int getSize(Resource<?> resource) {
        Resource<?> resource2 = resource;
        if (resource2 == null) {
            return 1;
        }
        return resource2.getSize();
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // com.bumptech.glide.util.LruCache
    public void onItemEvicted(Key key, Resource<?> resource) {
        Resource<?> resource2 = resource;
        MemoryCache.ResourceRemovedListener resourceRemovedListener = this.listener;
        if (resourceRemovedListener != null && resource2 != null) {
            Util.assertMainThread();
            ((Engine) resourceRemovedListener).resourceRecycler.recycle(resource2);
        }
    }
}
