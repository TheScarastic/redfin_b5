package com.android.systemui.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideSharePreferencesFactory implements Factory<SharedPreferences> {
    private final Provider<Context> contextProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProvideSharePreferencesFactory(DependencyProvider dependencyProvider, Provider<Context> provider) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public SharedPreferences get() {
        return provideSharePreferences(this.module, this.contextProvider.get());
    }

    public static DependencyProvider_ProvideSharePreferencesFactory create(DependencyProvider dependencyProvider, Provider<Context> provider) {
        return new DependencyProvider_ProvideSharePreferencesFactory(dependencyProvider, provider);
    }

    public static SharedPreferences provideSharePreferences(DependencyProvider dependencyProvider, Context context) {
        return (SharedPreferences) Preconditions.checkNotNullFromProvides(dependencyProvider.provideSharePreferences(context));
    }
}
