package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class ThreadFactoryImpl_Factory implements Factory<ThreadFactoryImpl> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final ThreadFactoryImpl_Factory INSTANCE = new ThreadFactoryImpl_Factory();
    }

    @Override // javax.inject.Provider
    public ThreadFactoryImpl get() {
        return newInstance();
    }

    public static ThreadFactoryImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ThreadFactoryImpl newInstance() {
        return new ThreadFactoryImpl();
    }
}
