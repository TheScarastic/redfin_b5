package com.android.systemui.dagger;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvideDevicePolicyManagerFactory implements Factory<DevicePolicyManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvideDevicePolicyManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public DevicePolicyManager get() {
        return provideDevicePolicyManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvideDevicePolicyManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvideDevicePolicyManagerFactory(provider);
    }

    public static DevicePolicyManager provideDevicePolicyManager(Context context) {
        return (DevicePolicyManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideDevicePolicyManager(context));
    }
}
