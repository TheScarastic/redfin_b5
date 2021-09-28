package com.android.systemui.dagger;

import com.android.systemui.shared.system.ActivityManagerWrapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideActivityManagerWrapperFactory implements Factory<ActivityManagerWrapper> {
    private final DependencyProvider module;

    public DependencyProvider_ProvideActivityManagerWrapperFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public ActivityManagerWrapper get() {
        return provideActivityManagerWrapper(this.module);
    }

    public static DependencyProvider_ProvideActivityManagerWrapperFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvideActivityManagerWrapperFactory(dependencyProvider);
    }

    public static ActivityManagerWrapper provideActivityManagerWrapper(DependencyProvider dependencyProvider) {
        return (ActivityManagerWrapper) Preconditions.checkNotNullFromProvides(dependencyProvider.provideActivityManagerWrapper());
    }
}
