package com.bumptech.glide.load.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.appcompat.R$dimen$$ExternalSyntheticOutline0;
import androidx.core.util.Pools$Pool;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DecodeJob;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class EngineJob<R> implements DecodeJob.Callback<R>, FactoryPools.Poolable {
    public static final EngineResourceFactory DEFAULT_FACTORY = new EngineResourceFactory();
    public static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper(), new MainThreadCallback());
    public final GlideExecutor animationExecutor;
    public DataSource dataSource;
    public DecodeJob<R> decodeJob;
    public final GlideExecutor diskCacheExecutor;
    public EngineResource<?> engineResource;
    public final EngineResourceFactory engineResourceFactory;
    public GlideException exception;
    public boolean hasLoadFailed;
    public boolean hasResource;
    public List<ResourceCallback> ignoredCallbacks;
    public boolean isCacheable;
    public volatile boolean isCancelled;
    public Key key;
    public final EngineJobListener listener;
    public boolean onlyRetrieveFromCache;
    public final Pools$Pool<EngineJob<?>> pool;
    public Resource<?> resource;
    public final GlideExecutor sourceExecutor;
    public final GlideExecutor sourceUnlimitedExecutor;
    public boolean useAnimationPool;
    public boolean useUnlimitedSourceGeneratorPool;
    public final List<ResourceCallback> cbs = new ArrayList(2);
    public final StateVerifier stateVerifier = new StateVerifier.DefaultStateVerifier();

    /* loaded from: classes.dex */
    public static class EngineResourceFactory {
    }

    /* loaded from: classes.dex */
    public static class MainThreadCallback implements Handler.Callback {
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            EngineJob<?> engineJob = (EngineJob) message.obj;
            int i = message.what;
            if (i == 1) {
                engineJob.stateVerifier.throwIfRecycled();
                if (engineJob.isCancelled) {
                    engineJob.resource.recycle();
                    engineJob.release(false);
                } else if (engineJob.cbs.isEmpty()) {
                    throw new IllegalStateException("Received a resource without any callbacks to notify");
                } else if (!engineJob.hasResource) {
                    EngineResourceFactory engineResourceFactory = engineJob.engineResourceFactory;
                    Resource<?> resource = engineJob.resource;
                    boolean z = engineJob.isCacheable;
                    Objects.requireNonNull(engineResourceFactory);
                    EngineResource<?> engineResource = new EngineResource<>(resource, z, true);
                    engineJob.engineResource = engineResource;
                    engineJob.hasResource = true;
                    engineResource.acquire();
                    ((Engine) engineJob.listener).onEngineJobComplete(engineJob, engineJob.key, engineJob.engineResource);
                    int size = engineJob.cbs.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        ResourceCallback resourceCallback = engineJob.cbs.get(i2);
                        List<ResourceCallback> list = engineJob.ignoredCallbacks;
                        if (!(list != null && list.contains(resourceCallback))) {
                            engineJob.engineResource.acquire();
                            resourceCallback.onResourceReady(engineJob.engineResource, engineJob.dataSource);
                        }
                    }
                    engineJob.engineResource.release();
                    engineJob.release(false);
                } else {
                    throw new IllegalStateException("Already have resource");
                }
            } else if (i == 2) {
                engineJob.stateVerifier.throwIfRecycled();
                if (engineJob.isCancelled) {
                    engineJob.release(false);
                } else if (engineJob.cbs.isEmpty()) {
                    throw new IllegalStateException("Received an exception without any callbacks to notify");
                } else if (!engineJob.hasLoadFailed) {
                    engineJob.hasLoadFailed = true;
                    ((Engine) engineJob.listener).onEngineJobComplete(engineJob, engineJob.key, null);
                    for (ResourceCallback resourceCallback2 : engineJob.cbs) {
                        List<ResourceCallback> list2 = engineJob.ignoredCallbacks;
                        if (!(list2 != null && list2.contains(resourceCallback2))) {
                            resourceCallback2.onLoadFailed(engineJob.exception);
                        }
                    }
                    engineJob.release(false);
                } else {
                    throw new IllegalStateException("Already failed once");
                }
            } else if (i == 3) {
                engineJob.stateVerifier.throwIfRecycled();
                if (engineJob.isCancelled) {
                    ((Engine) engineJob.listener).onEngineJobCancelled(engineJob, engineJob.key);
                    engineJob.release(false);
                } else {
                    throw new IllegalStateException("Not cancelled");
                }
            } else {
                throw new IllegalStateException(R$dimen$$ExternalSyntheticOutline0.m(33, "Unrecognized message: ", message.what));
            }
            return true;
        }
    }

    public EngineJob(GlideExecutor glideExecutor, GlideExecutor glideExecutor2, GlideExecutor glideExecutor3, GlideExecutor glideExecutor4, EngineJobListener engineJobListener, Pools$Pool<EngineJob<?>> pools$Pool, EngineResourceFactory engineResourceFactory) {
        this.diskCacheExecutor = glideExecutor;
        this.sourceExecutor = glideExecutor2;
        this.sourceUnlimitedExecutor = glideExecutor3;
        this.animationExecutor = glideExecutor4;
        this.listener = engineJobListener;
        this.pool = pools$Pool;
        this.engineResourceFactory = engineResourceFactory;
    }

    public void addCallback(ResourceCallback resourceCallback) {
        Util.assertMainThread();
        this.stateVerifier.throwIfRecycled();
        if (this.hasResource) {
            resourceCallback.onResourceReady(this.engineResource, this.dataSource);
        } else if (this.hasLoadFailed) {
            resourceCallback.onLoadFailed(this.exception);
        } else {
            this.cbs.add(resourceCallback);
        }
    }

    @Override // com.bumptech.glide.util.pool.FactoryPools.Poolable
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    public EngineJob<R> init(Key key, boolean z, boolean z2, boolean z3, boolean z4) {
        this.key = key;
        this.isCacheable = z;
        this.useUnlimitedSourceGeneratorPool = z2;
        this.useAnimationPool = z3;
        this.onlyRetrieveFromCache = z4;
        return this;
    }

    public final void release(boolean z) {
        boolean isComplete;
        Util.assertMainThread();
        this.cbs.clear();
        this.key = null;
        this.engineResource = null;
        this.resource = null;
        List<ResourceCallback> list = this.ignoredCallbacks;
        if (list != null) {
            list.clear();
        }
        this.hasLoadFailed = false;
        this.isCancelled = false;
        this.hasResource = false;
        DecodeJob<R> decodeJob = this.decodeJob;
        DecodeJob.ReleaseManager releaseManager = decodeJob.releaseManager;
        synchronized (releaseManager) {
            releaseManager.isReleased = true;
            isComplete = releaseManager.isComplete(z);
        }
        if (isComplete) {
            decodeJob.releaseInternal();
        }
        this.decodeJob = null;
        this.exception = null;
        this.dataSource = null;
        this.pool.release(this);
    }

    public void reschedule(DecodeJob<?> decodeJob) {
        (this.useUnlimitedSourceGeneratorPool ? this.sourceUnlimitedExecutor : this.useAnimationPool ? this.animationExecutor : this.sourceExecutor).delegate.execute(decodeJob);
    }
}
