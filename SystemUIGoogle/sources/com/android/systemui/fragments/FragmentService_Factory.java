package com.android.systemui.fragments;

import com.android.systemui.fragments.FragmentService;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FragmentService_Factory implements Factory<FragmentService> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;

    public FragmentService_Factory(Provider<FragmentService.FragmentCreator.Factory> provider, Provider<ConfigurationController> provider2) {
        this.fragmentCreatorFactoryProvider = provider;
        this.configurationControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public FragmentService get() {
        return newInstance(this.fragmentCreatorFactoryProvider.get(), this.configurationControllerProvider.get());
    }

    public static FragmentService_Factory create(Provider<FragmentService.FragmentCreator.Factory> provider, Provider<ConfigurationController> provider2) {
        return new FragmentService_Factory(provider, provider2);
    }

    public static FragmentService newInstance(FragmentService.FragmentCreator.Factory factory, ConfigurationController configurationController) {
        return new FragmentService(factory, configurationController);
    }
}
