package com.android.systemui.dagger;

import android.content.Context;
import android.permission.PermissionManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FrameworkServicesModule_ProvidePermissionManagerFactory implements Factory<PermissionManager> {
    private final Provider<Context> contextProvider;

    public FrameworkServicesModule_ProvidePermissionManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public PermissionManager get() {
        return providePermissionManager(this.contextProvider.get());
    }

    public static FrameworkServicesModule_ProvidePermissionManagerFactory create(Provider<Context> provider) {
        return new FrameworkServicesModule_ProvidePermissionManagerFactory(provider);
    }

    public static PermissionManager providePermissionManager(Context context) {
        return (PermissionManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.providePermissionManager(context));
    }
}
