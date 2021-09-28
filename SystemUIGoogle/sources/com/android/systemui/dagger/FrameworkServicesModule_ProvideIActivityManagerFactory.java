package com.android.systemui.dagger;

import android.app.IActivityManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIActivityManagerFactory implements Factory<IActivityManager> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIActivityManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIActivityManagerFactory();
    }

    @Override // javax.inject.Provider
    public IActivityManager get() {
        return provideIActivityManager();
    }

    public static FrameworkServicesModule_ProvideIActivityManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IActivityManager provideIActivityManager() {
        return (IActivityManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIActivityManager());
    }
}
