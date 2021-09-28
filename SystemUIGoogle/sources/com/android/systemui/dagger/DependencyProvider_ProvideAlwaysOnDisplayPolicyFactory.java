package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideAlwaysOnDisplayPolicyFactory implements Factory<AlwaysOnDisplayPolicy> {
    private final Provider<Context> contextProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProvideAlwaysOnDisplayPolicyFactory(DependencyProvider dependencyProvider, Provider<Context> provider) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public AlwaysOnDisplayPolicy get() {
        return provideAlwaysOnDisplayPolicy(this.module, this.contextProvider.get());
    }

    public static DependencyProvider_ProvideAlwaysOnDisplayPolicyFactory create(DependencyProvider dependencyProvider, Provider<Context> provider) {
        return new DependencyProvider_ProvideAlwaysOnDisplayPolicyFactory(dependencyProvider, provider);
    }

    public static AlwaysOnDisplayPolicy provideAlwaysOnDisplayPolicy(DependencyProvider dependencyProvider, Context context) {
        return (AlwaysOnDisplayPolicy) Preconditions.checkNotNullFromProvides(dependencyProvider.provideAlwaysOnDisplayPolicy(context));
    }
}
