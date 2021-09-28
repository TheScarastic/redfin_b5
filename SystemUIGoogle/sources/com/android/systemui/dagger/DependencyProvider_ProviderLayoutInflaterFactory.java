package com.android.systemui.dagger;

import android.content.Context;
import android.view.LayoutInflater;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProviderLayoutInflaterFactory implements Factory<LayoutInflater> {
    private final Provider<Context> contextProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProviderLayoutInflaterFactory(DependencyProvider dependencyProvider, Provider<Context> provider) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public LayoutInflater get() {
        return providerLayoutInflater(this.module, this.contextProvider.get());
    }

    public static DependencyProvider_ProviderLayoutInflaterFactory create(DependencyProvider dependencyProvider, Provider<Context> provider) {
        return new DependencyProvider_ProviderLayoutInflaterFactory(dependencyProvider, provider);
    }

    public static LayoutInflater providerLayoutInflater(DependencyProvider dependencyProvider, Context context) {
        return (LayoutInflater) Preconditions.checkNotNullFromProvides(dependencyProvider.providerLayoutInflater(context));
    }
}
