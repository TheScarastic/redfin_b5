package com.android.systemui.dump;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class DumpManager_Factory implements Factory<DumpManager> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final DumpManager_Factory INSTANCE = new DumpManager_Factory();
    }

    @Override // javax.inject.Provider
    public DumpManager get() {
        return newInstance();
    }

    public static DumpManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DumpManager newInstance() {
        return new DumpManager();
    }
}
