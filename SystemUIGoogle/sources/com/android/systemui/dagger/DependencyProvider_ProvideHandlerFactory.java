package com.android.systemui.dagger;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideHandlerFactory implements Factory<Handler> {
    private final DependencyProvider module;

    public DependencyProvider_ProvideHandlerFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public Handler get() {
        return provideHandler(this.module);
    }

    public static DependencyProvider_ProvideHandlerFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvideHandlerFactory(dependencyProvider);
    }

    public static Handler provideHandler(DependencyProvider dependencyProvider) {
        return (Handler) Preconditions.checkNotNullFromProvides(dependencyProvider.provideHandler());
    }
}
