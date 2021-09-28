package com.android.systemui.util;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class DeviceConfigProxy_Factory implements Factory<DeviceConfigProxy> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final DeviceConfigProxy_Factory INSTANCE = new DeviceConfigProxy_Factory();
    }

    @Override // javax.inject.Provider
    public DeviceConfigProxy get() {
        return newInstance();
    }

    public static DeviceConfigProxy_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static DeviceConfigProxy newInstance() {
        return new DeviceConfigProxy();
    }
}
