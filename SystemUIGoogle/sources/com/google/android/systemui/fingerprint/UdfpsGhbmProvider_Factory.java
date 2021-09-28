package com.google.android.systemui.fingerprint;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class UdfpsGhbmProvider_Factory implements Factory<UdfpsGhbmProvider> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final UdfpsGhbmProvider_Factory INSTANCE = new UdfpsGhbmProvider_Factory();
    }

    @Override // javax.inject.Provider
    public UdfpsGhbmProvider get() {
        return newInstance();
    }

    public static UdfpsGhbmProvider_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UdfpsGhbmProvider newInstance() {
        return new UdfpsGhbmProvider();
    }
}
