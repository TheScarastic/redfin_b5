package com.android.systemui.dagger;

import android.content.Context;
import android.content.pm.PackageManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvidePackageManagerFactory implements Factory<PackageManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvidePackageManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public PackageManager get() {
        return providePackageManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvidePackageManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvidePackageManagerFactory(provider);
    }

    public static PackageManager providePackageManager(Context context) {
        return (PackageManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.providePackageManager(context));
    }
}
