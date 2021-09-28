package com.android.systemui.keyguard;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ScreenLifecycle_Factory implements Factory<ScreenLifecycle> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final ScreenLifecycle_Factory INSTANCE = new ScreenLifecycle_Factory();
    }

    @Override // javax.inject.Provider
    public ScreenLifecycle get() {
        return newInstance();
    }

    public static ScreenLifecycle_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ScreenLifecycle newInstance() {
        return new ScreenLifecycle();
    }
}
