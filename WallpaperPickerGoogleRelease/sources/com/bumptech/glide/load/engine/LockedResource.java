package com.bumptech.glide.load.engine;

import androidx.core.util.Pools$Pool;
import androidx.core.util.Pools$SynchronizedPool;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LockedResource<Z> implements Resource<Z>, FactoryPools.Poolable {
    public static final Pools$Pool<LockedResource<?>> POOL = new FactoryPools.FactoryPool(new Pools$SynchronizedPool(20), new FactoryPools.Factory<LockedResource<?>>() { // from class: com.bumptech.glide.load.engine.LockedResource.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // com.bumptech.glide.util.pool.FactoryPools.Factory
        public LockedResource<?> create() {
            return new LockedResource<>();
        }
    }, FactoryPools.EMPTY_RESETTER);
    public boolean isLocked;
    public boolean isRecycled;
    public final StateVerifier stateVerifier = new StateVerifier.DefaultStateVerifier();
    public Resource<Z> toWrap;

    public static <Z> LockedResource<Z> obtain(Resource<Z> resource) {
        LockedResource<Z> lockedResource = (LockedResource) ((FactoryPools.FactoryPool) POOL).acquire();
        Objects.requireNonNull(lockedResource, "Argument must not be null");
        lockedResource.isRecycled = false;
        lockedResource.isLocked = true;
        lockedResource.toWrap = resource;
        return lockedResource;
    }

    @Override // com.bumptech.glide.load.engine.Resource
    public Z get() {
        return this.toWrap.get();
    }

    @Override // com.bumptech.glide.load.engine.Resource
    public Class<Z> getResourceClass() {
        return this.toWrap.getResourceClass();
    }

    @Override // com.bumptech.glide.load.engine.Resource
    public int getSize() {
        return this.toWrap.getSize();
    }

    @Override // com.bumptech.glide.util.pool.FactoryPools.Poolable
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    @Override // com.bumptech.glide.load.engine.Resource
    public synchronized void recycle() {
        this.stateVerifier.throwIfRecycled();
        this.isRecycled = true;
        if (!this.isLocked) {
            this.toWrap.recycle();
            this.toWrap = null;
            ((FactoryPools.FactoryPool) POOL).release(this);
        }
    }

    public synchronized void unlock() {
        this.stateVerifier.throwIfRecycled();
        if (this.isLocked) {
            this.isLocked = false;
            if (this.isRecycled) {
                recycle();
            }
        } else {
            throw new IllegalStateException("Already unlocked");
        }
    }
}
