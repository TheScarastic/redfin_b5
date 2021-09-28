package com.android.systemui.telephony;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class TelephonyCallback_Factory implements Factory<TelephonyCallback> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final TelephonyCallback_Factory INSTANCE = new TelephonyCallback_Factory();
    }

    @Override // javax.inject.Provider
    public TelephonyCallback get() {
        return newInstance();
    }

    public static TelephonyCallback_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TelephonyCallback newInstance() {
        return new TelephonyCallback();
    }
}
