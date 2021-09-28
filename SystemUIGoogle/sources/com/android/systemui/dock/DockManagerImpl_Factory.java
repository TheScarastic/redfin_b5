package com.android.systemui.dock;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DockManagerImpl_Factory implements Factory<DockManagerImpl> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final DockManagerImpl_Factory INSTANCE = new DockManagerImpl_Factory();
    }

    @Override // javax.inject.Provider
    public DockManagerImpl get() {
        return newInstance();
    }

    public static DockManagerImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DockManagerImpl newInstance() {
        return new DockManagerImpl();
    }
}
