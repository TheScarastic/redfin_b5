package com.android.systemui.dagger;

import com.android.systemui.shared.system.TaskStackChangeListeners;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideTaskStackChangeListenersFactory implements Factory<TaskStackChangeListeners> {
    private final DependencyProvider module;

    public DependencyProvider_ProvideTaskStackChangeListenersFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public TaskStackChangeListeners get() {
        return provideTaskStackChangeListeners(this.module);
    }

    public static DependencyProvider_ProvideTaskStackChangeListenersFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvideTaskStackChangeListenersFactory(dependencyProvider);
    }

    public static TaskStackChangeListeners provideTaskStackChangeListeners(DependencyProvider dependencyProvider) {
        return (TaskStackChangeListeners) Preconditions.checkNotNullFromProvides(dependencyProvider.provideTaskStackChangeListeners());
    }
}
