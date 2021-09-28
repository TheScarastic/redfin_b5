package com.android.systemui.power;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class EnhancedEstimatesImpl_Factory implements Factory<EnhancedEstimatesImpl> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final EnhancedEstimatesImpl_Factory INSTANCE = new EnhancedEstimatesImpl_Factory();
    }

    @Override // javax.inject.Provider
    public EnhancedEstimatesImpl get() {
        return newInstance();
    }

    public static EnhancedEstimatesImpl_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static EnhancedEstimatesImpl newInstance() {
        return new EnhancedEstimatesImpl();
    }
}
