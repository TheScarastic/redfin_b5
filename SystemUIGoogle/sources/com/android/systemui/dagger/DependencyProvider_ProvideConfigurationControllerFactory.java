package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideConfigurationControllerFactory implements Factory<ConfigurationController> {
    private final Provider<Context> contextProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProvideConfigurationControllerFactory(DependencyProvider dependencyProvider, Provider<Context> provider) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public ConfigurationController get() {
        return provideConfigurationController(this.module, this.contextProvider.get());
    }

    public static DependencyProvider_ProvideConfigurationControllerFactory create(DependencyProvider dependencyProvider, Provider<Context> provider) {
        return new DependencyProvider_ProvideConfigurationControllerFactory(dependencyProvider, provider);
    }

    public static ConfigurationController provideConfigurationController(DependencyProvider dependencyProvider, Context context) {
        return (ConfigurationController) Preconditions.checkNotNullFromProvides(dependencyProvider.provideConfigurationController(context));
    }
}
