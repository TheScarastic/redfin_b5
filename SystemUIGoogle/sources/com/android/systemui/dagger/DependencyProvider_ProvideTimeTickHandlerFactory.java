package com.android.systemui.dagger;

import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideTimeTickHandlerFactory implements Factory<Handler> {
    private final DependencyProvider module;

    public DependencyProvider_ProvideTimeTickHandlerFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public Handler get() {
        return provideTimeTickHandler(this.module);
    }

    public static DependencyProvider_ProvideTimeTickHandlerFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvideTimeTickHandlerFactory(dependencyProvider);
    }

    public static Handler provideTimeTickHandler(DependencyProvider dependencyProvider) {
        return (Handler) Preconditions.checkNotNullFromProvides(dependencyProvider.provideTimeTickHandler());
    }
}
