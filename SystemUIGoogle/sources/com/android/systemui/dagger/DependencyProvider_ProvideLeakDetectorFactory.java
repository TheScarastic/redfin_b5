package com.android.systemui.dagger;

import com.android.systemui.util.leak.LeakDetector;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideLeakDetectorFactory implements Factory<LeakDetector> {
    private final DependencyProvider module;

    public DependencyProvider_ProvideLeakDetectorFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public LeakDetector get() {
        return provideLeakDetector(this.module);
    }

    public static DependencyProvider_ProvideLeakDetectorFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvideLeakDetectorFactory(dependencyProvider);
    }

    public static LeakDetector provideLeakDetector(DependencyProvider dependencyProvider) {
        return (LeakDetector) Preconditions.checkNotNullFromProvides(dependencyProvider.provideLeakDetector());
    }
}
