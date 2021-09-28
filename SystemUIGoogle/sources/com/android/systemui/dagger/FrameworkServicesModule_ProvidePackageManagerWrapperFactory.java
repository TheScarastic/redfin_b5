package com.android.systemui.dagger;

import com.android.systemui.shared.system.PackageManagerWrapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvidePackageManagerWrapperFactory implements Factory<PackageManagerWrapper> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final FrameworkServicesModule_ProvidePackageManagerWrapperFactory INSTANCE = new FrameworkServicesModule_ProvidePackageManagerWrapperFactory();
    }

    @Override // javax.inject.Provider
    public PackageManagerWrapper get() {
        return providePackageManagerWrapper();
    }

    public static FrameworkServicesModule_ProvidePackageManagerWrapperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PackageManagerWrapper providePackageManagerWrapper() {
        return (PackageManagerWrapper) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.providePackageManagerWrapper());
    }
}
