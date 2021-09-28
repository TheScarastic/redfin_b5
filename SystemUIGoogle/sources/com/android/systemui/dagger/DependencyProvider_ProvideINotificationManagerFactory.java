package com.android.systemui.dagger;

import android.app.INotificationManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideINotificationManagerFactory implements Factory<INotificationManager> {
    private final DependencyProvider module;

    public DependencyProvider_ProvideINotificationManagerFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public INotificationManager get() {
        return provideINotificationManager(this.module);
    }

    public static DependencyProvider_ProvideINotificationManagerFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvideINotificationManagerFactory(dependencyProvider);
    }

    public static INotificationManager provideINotificationManager(DependencyProvider dependencyProvider) {
        return (INotificationManager) Preconditions.checkNotNullFromProvides(dependencyProvider.provideINotificationManager());
    }
}
