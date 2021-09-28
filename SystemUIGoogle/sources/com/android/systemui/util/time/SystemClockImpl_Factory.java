package com.android.systemui.util.time;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class SystemClockImpl_Factory implements Factory<SystemClockImpl> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SystemClockImpl_Factory INSTANCE = new SystemClockImpl_Factory();
    }

    @Override // javax.inject.Provider
    public SystemClockImpl get() {
        return newInstance();
    }

    public static SystemClockImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SystemClockImpl newInstance() {
        return new SystemClockImpl();
    }
}
