package com.android.keyguard.dagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardBouncerModule_ProvidesRootViewFactory implements Factory<ViewGroup> {
    private final Provider<LayoutInflater> layoutInflaterProvider;

    public KeyguardBouncerModule_ProvidesRootViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    @Override // javax.inject.Provider
    public ViewGroup get() {
        return providesRootView(this.layoutInflaterProvider.get());
    }

    public static KeyguardBouncerModule_ProvidesRootViewFactory create(Provider<LayoutInflater> provider) {
        return new KeyguardBouncerModule_ProvidesRootViewFactory(provider);
    }

    public static ViewGroup providesRootView(LayoutInflater layoutInflater) {
        return (ViewGroup) Preconditions.checkNotNullFromProvides(KeyguardBouncerModule.providesRootView(layoutInflater));
    }
}
