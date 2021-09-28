package com.android.systemui.dagger;

import com.android.internal.statusbar.IStatusBarService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideIStatusBarServiceFactory implements Factory<IStatusBarService> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvideIStatusBarServiceFactory INSTANCE = new FrameworkServicesModule_ProvideIStatusBarServiceFactory();
    }

    @Override // javax.inject.Provider
    public IStatusBarService get() {
        return provideIStatusBarService();
    }

    public static FrameworkServicesModule_ProvideIStatusBarServiceFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IStatusBarService provideIStatusBarService() {
        return (IStatusBarService) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIStatusBarService());
    }
}
