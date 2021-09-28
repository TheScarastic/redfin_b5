package com.android.systemui.dagger;

import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideDevicePolicyManagerWrapperFactory implements Factory<DevicePolicyManagerWrapper> {
    private final DependencyProvider module;

    public DependencyProvider_ProvideDevicePolicyManagerWrapperFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public DevicePolicyManagerWrapper get() {
        return provideDevicePolicyManagerWrapper(this.module);
    }

    public static DependencyProvider_ProvideDevicePolicyManagerWrapperFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvideDevicePolicyManagerWrapperFactory(dependencyProvider);
    }

    public static DevicePolicyManagerWrapper provideDevicePolicyManagerWrapper(DependencyProvider dependencyProvider) {
        return (DevicePolicyManagerWrapper) Preconditions.checkNotNullFromProvides(dependencyProvider.provideDevicePolicyManagerWrapper());
    }
}
