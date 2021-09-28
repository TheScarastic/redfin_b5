package com.android.systemui.flags;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemPropertiesHelper_Factory implements Factory<SystemPropertiesHelper> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final SystemPropertiesHelper_Factory INSTANCE = new SystemPropertiesHelper_Factory();
    }

    @Override // javax.inject.Provider
    public SystemPropertiesHelper get() {
        return newInstance();
    }

    public static SystemPropertiesHelper_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SystemPropertiesHelper newInstance() {
        return new SystemPropertiesHelper();
    }
}
