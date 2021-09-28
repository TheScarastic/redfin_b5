package com.android.systemui.dagger;

import com.android.internal.logging.MetricsLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideMetricsLoggerFactory implements Factory<MetricsLogger> {
    private final DependencyProvider module;

    public DependencyProvider_ProvideMetricsLoggerFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public MetricsLogger get() {
        return provideMetricsLogger(this.module);
    }

    public static DependencyProvider_ProvideMetricsLoggerFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvideMetricsLoggerFactory(dependencyProvider);
    }

    public static MetricsLogger provideMetricsLogger(DependencyProvider dependencyProvider) {
        return (MetricsLogger) Preconditions.checkNotNullFromProvides(dependencyProvider.provideMetricsLogger());
    }
}
