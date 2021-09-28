package com.android.systemui.dagger;

import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.keyguard.KeyguardViewMediator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvidesViewMediatorCallbackFactory implements Factory<ViewMediatorCallback> {
    private final DependencyProvider module;
    private final Provider<KeyguardViewMediator> viewMediatorProvider;

    public DependencyProvider_ProvidesViewMediatorCallbackFactory(DependencyProvider dependencyProvider, Provider<KeyguardViewMediator> provider) {
        this.module = dependencyProvider;
        this.viewMediatorProvider = provider;
    }

    @Override // javax.inject.Provider
    public ViewMediatorCallback get() {
        return providesViewMediatorCallback(this.module, this.viewMediatorProvider.get());
    }

    public static DependencyProvider_ProvidesViewMediatorCallbackFactory create(DependencyProvider dependencyProvider, Provider<KeyguardViewMediator> provider) {
        return new DependencyProvider_ProvidesViewMediatorCallbackFactory(dependencyProvider, provider);
    }

    public static ViewMediatorCallback providesViewMediatorCallback(DependencyProvider dependencyProvider, KeyguardViewMediator keyguardViewMediator) {
        return (ViewMediatorCallback) Preconditions.checkNotNullFromProvides(dependencyProvider.providesViewMediatorCallback(keyguardViewMediator));
    }
}
