package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class KeyguardEnvironmentImpl_Factory implements Factory<KeyguardEnvironmentImpl> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final KeyguardEnvironmentImpl_Factory INSTANCE = new KeyguardEnvironmentImpl_Factory();
    }

    @Override // javax.inject.Provider
    public KeyguardEnvironmentImpl get() {
        return newInstance();
    }

    public static KeyguardEnvironmentImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static KeyguardEnvironmentImpl newInstance() {
        return new KeyguardEnvironmentImpl();
    }
}
