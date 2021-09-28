package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class ExecutionImpl_Factory implements Factory<ExecutionImpl> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final ExecutionImpl_Factory INSTANCE = new ExecutionImpl_Factory();
    }

    @Override // javax.inject.Provider
    public ExecutionImpl get() {
        return newInstance();
    }

    public static ExecutionImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ExecutionImpl newInstance() {
        return new ExecutionImpl();
    }
}
