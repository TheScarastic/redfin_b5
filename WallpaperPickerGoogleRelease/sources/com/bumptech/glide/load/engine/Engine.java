package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools$Pool;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.ActiveResources;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.EngineResource;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class Engine implements EngineJobListener, MemoryCache.ResourceRemovedListener, EngineResource.ResourceListener {
    public static final boolean VERBOSE_IS_LOGGABLE = Log.isLoggable("Engine", 2);
    public final ActiveResources activeResources;
    public final MemoryCache cache;
    public final DecodeJobFactory decodeJobFactory;
    public final LazyDiskCacheProvider diskCacheProvider;
    public final EngineJobFactory engineJobFactory;
    public final Jobs jobs;
    public final EngineKeyFactory keyFactory;
    public final ResourceRecycler resourceRecycler;

    /* loaded from: classes.dex */
    public static class DecodeJobFactory {
        public int creationOrder;
        public final DecodeJob.DiskCacheProvider diskCacheProvider;
        public final Pools$Pool<DecodeJob<?>> pool = FactoryPools.simple(150, new FactoryPools.Factory<DecodeJob<?>>() { // from class: com.bumptech.glide.load.engine.Engine.DecodeJobFactory.1
            /* Return type fixed from 'java.lang.Object' to match base method */
            @Override // com.bumptech.glide.util.pool.FactoryPools.Factory
            public DecodeJob<?> create() {
                DecodeJobFactory decodeJobFactory = DecodeJobFactory.this;
                return new DecodeJob<>(decodeJobFactory.diskCacheProvider, decodeJobFactory.pool);
            }
        });

        public DecodeJobFactory(DecodeJob.DiskCacheProvider diskCacheProvider) {
            this.diskCacheProvider = diskCacheProvider;
        }
    }

    /* loaded from: classes.dex */
    public static class EngineJobFactory {
        public final GlideExecutor animationExecutor;
        public final GlideExecutor diskCacheExecutor;
        public final EngineJobListener listener;
        public final Pools$Pool<EngineJob<?>> pool = FactoryPools.simple(150, new FactoryPools.Factory<EngineJob<?>>() { // from class: com.bumptech.glide.load.engine.Engine.EngineJobFactory.1
            /* Return type fixed from 'java.lang.Object' to match base method */
            @Override // com.bumptech.glide.util.pool.FactoryPools.Factory
            public EngineJob<?> create() {
                EngineJobFactory engineJobFactory = EngineJobFactory.this;
                return new EngineJob<>(engineJobFactory.diskCacheExecutor, engineJobFactory.sourceExecutor, engineJobFactory.sourceUnlimitedExecutor, engineJobFactory.animationExecutor, engineJobFactory.listener, engineJobFactory.pool, EngineJob.DEFAULT_FACTORY);
            }
        });
        public final GlideExecutor sourceExecutor;
        public final GlideExecutor sourceUnlimitedExecutor;

        public EngineJobFactory(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener) {
            this.diskCacheExecutor = glideExecutor;
            this.sourceExecutor = glideExecutor2;
            this.sourceUnlimitedExecutor = glideExecutor3;
            this.animationExecutor = glideExecutor4;
            this.listener = engineJobListener;
        }

        public static void shutdownAndAwaitTermination(ExecutorService executorService) {
            ((GlideExecutor) executorService).delegate.shutdown();
            try {
                TimeUnit timeUnit = TimeUnit.SECONDS;
                GlideExecutor glideExecutor = (GlideExecutor) executorService;
                if (!glideExecutor.awaitTermination(5, timeUnit)) {
                    glideExecutor.shutdownNow();
                    if (!glideExecutor.awaitTermination(5, timeUnit)) {
                        throw new RuntimeException("Failed to shutdown");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void shutdown() {
            shutdownAndAwaitTermination(this.diskCacheExecutor);
            shutdownAndAwaitTermination(this.sourceExecutor);
            shutdownAndAwaitTermination(this.sourceUnlimitedExecutor);
            shutdownAndAwaitTermination(this.animationExecutor);
        }
    }

    /* loaded from: classes.dex */
    public static class LazyDiskCacheProvider implements DecodeJob.DiskCacheProvider {
        public volatile DiskCache diskCache;
        public final DiskCache.Factory factory;

        public LazyDiskCacheProvider(DiskCache.Factory factory) {
            this.factory = factory;
        }

        public synchronized void clearDiskCacheIfCreated() {
            if (this.diskCache != null) {
                this.diskCache.clear();
            }
        }

        public DiskCache getDiskCache() {
            if (this.diskCache == null) {
                synchronized (this) {
                    if (this.diskCache == null) {
                        DiskLruCacheFactory diskLruCacheFactory = (DiskLruCacheFactory) this.factory;
                        InternalCacheDiskCacheFactory.AnonymousClass1 r1 = (InternalCacheDiskCacheFactory.AnonymousClass1) diskLruCacheFactory.cacheDirectoryGetter;
                        File cacheDir = context.getCacheDir();
                        DiskLruCacheWrapper diskLruCacheWrapper = null;
                        if (cacheDir == null) {
                            cacheDir = null;
                        } else if ("image_manager_disk_cache" != null) {
                            cacheDir = new File(cacheDir, "image_manager_disk_cache");
                        }
                        if (cacheDir != null && (cacheDir.mkdirs() || (cacheDir.exists() && cacheDir.isDirectory()))) {
                            diskLruCacheWrapper = new DiskLruCacheWrapper(cacheDir, diskLruCacheFactory.diskCacheSize);
                        }
                        this.diskCache = diskLruCacheWrapper;
                    }
                    if (this.diskCache == null) {
                        this.diskCache = new DiskCacheAdapter();
                    }
                }
            }
            return this.diskCache;
        }
    }

    /* loaded from: classes.dex */
    public static class LoadStatus {
        public final ResourceCallback cb;
        public final EngineJob<?> engineJob;

        public LoadStatus(ResourceCallback resourceCallback, EngineJob<?> engineJob) {
            this.cb = resourceCallback;
            this.engineJob = engineJob;
        }
    }

    public Engine(MemoryCache memoryCache, DiskCache.Factory factory, GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, Jobs jobs, EngineKeyFactory engineKeyFactory, ActiveResources activeResources, EngineJobFactory engineJobFactory, DecodeJobFactory decodeJobFactory, ResourceRecycler resourceRecycler, boolean z) {
        this.cache = memoryCache;
        LazyDiskCacheProvider lazyDiskCacheProvider = new LazyDiskCacheProvider(factory);
        this.diskCacheProvider = lazyDiskCacheProvider;
        ActiveResources activeResources2 = activeResources == null ? new ActiveResources(z) : activeResources;
        this.activeResources = activeResources2;
        activeResources2.listener = this;
        this.keyFactory = engineKeyFactory == null ? new EngineKeyFactory() : engineKeyFactory;
        this.jobs = jobs == null ? new Jobs() : jobs;
        this.engineJobFactory = engineJobFactory == null ? new EngineJobFactory(glideExecutor, glideExecutor2, glideExecutor3, glideExecutor4, this) : engineJobFactory;
        this.decodeJobFactory = decodeJobFactory == null ? new DecodeJobFactory(lazyDiskCacheProvider) : decodeJobFactory;
        this.resourceRecycler = resourceRecycler == null ? new ResourceRecycler() : resourceRecycler;
        ((LruResourceCache) memoryCache).listener = this;
    }

    public static void logWithTimeAndKey(String str, long j, Key key) {
        double elapsedMillis = LogTime.getElapsedMillis(j);
        String valueOf = String.valueOf(key);
        StringBuilder sb = new StringBuilder(valueOf.length() + str.length() + 37);
        sb.append(str);
        sb.append(" in ");
        sb.append(elapsedMillis);
        sb.append("ms, key: ");
        sb.append(valueOf);
        Log.v("Engine", sb.toString());
    }

    public void onEngineJobCancelled(EngineJob<?> engineJob, Key key) {
        Util.assertMainThread();
        Jobs jobs = this.jobs;
        Objects.requireNonNull(jobs);
        Map<Key, EngineJob<?>> jobMap = jobs.getJobMap(engineJob.onlyRetrieveFromCache);
        if (engineJob.equals(jobMap.get(key))) {
            jobMap.remove(key);
        }
    }

    public void onEngineJobComplete(EngineJob<?> engineJob, Key key, EngineResource<?> engineResource) {
        Util.assertMainThread();
        if (engineResource != null) {
            engineResource.key = key;
            engineResource.listener = this;
            if (engineResource.isCacheable) {
                this.activeResources.activate(key, engineResource);
            }
        }
        Jobs jobs = this.jobs;
        Objects.requireNonNull(jobs);
        Map<Key, EngineJob<?>> jobMap = jobs.getJobMap(engineJob.onlyRetrieveFromCache);
        if (engineJob.equals(jobMap.get(key))) {
            jobMap.remove(key);
        }
    }

    public void onResourceReleased(Key key, EngineResource<?> engineResource) {
        Util.assertMainThread();
        ActiveResources.ResourceWeakReference remove = this.activeResources.activeEngineResources.remove(key);
        if (remove != null) {
            remove.resource = null;
            remove.clear();
        }
        if (engineResource.isCacheable) {
            ((LruResourceCache) this.cache).put(key, engineResource);
        } else {
            this.resourceRecycler.recycle(engineResource);
        }
    }

    public void shutdown() {
        this.engineJobFactory.shutdown();
        this.diskCacheProvider.clearDiskCacheIfCreated();
        this.activeResources.shutdown();
    }
}
