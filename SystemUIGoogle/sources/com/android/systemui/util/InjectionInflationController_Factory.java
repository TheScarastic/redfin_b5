package com.android.systemui.util;

import com.android.systemui.util.InjectionInflationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class InjectionInflationController_Factory implements Factory<InjectionInflationController> {
    private final Provider<InjectionInflationController.ViewInstanceCreator.Factory> viewInstanceCreatorFactoryProvider;

    public InjectionInflationController_Factory(Provider<InjectionInflationController.ViewInstanceCreator.Factory> provider) {
        this.viewInstanceCreatorFactoryProvider = provider;
    }

    @Override // javax.inject.Provider
    public InjectionInflationController get() {
        return newInstance(this.viewInstanceCreatorFactoryProvider.get());
    }

    public static InjectionInflationController_Factory create(Provider<InjectionInflationController.ViewInstanceCreator.Factory> provider) {
        return new InjectionInflationController_Factory(provider);
    }

    public static InjectionInflationController newInstance(InjectionInflationController.ViewInstanceCreator.Factory factory) {
        return new InjectionInflationController(factory);
    }
}
