package com.android.systemui.dagger;

import android.service.dreams.IDreamManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIDreamManagerFactory implements Factory<IDreamManager> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIDreamManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIDreamManagerFactory();
    }

    @Override // javax.inject.Provider
    public IDreamManager get() {
        return provideIDreamManager();
    }

    public static FrameworkServicesModule_ProvideIDreamManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IDreamManager provideIDreamManager() {
        return (IDreamManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIDreamManager());
    }
}
