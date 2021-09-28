package com.android.systemui.dagger;

import android.view.Choreographer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvidesChoreographerFactory implements Factory<Choreographer> {
    private final DependencyProvider module;

    public DependencyProvider_ProvidesChoreographerFactory(DependencyProvider dependencyProvider) {
        this.module = dependencyProvider;
    }

    @Override // javax.inject.Provider
    public Choreographer get() {
        return providesChoreographer(this.module);
    }

    public static DependencyProvider_ProvidesChoreographerFactory create(DependencyProvider dependencyProvider) {
        return new DependencyProvider_ProvidesChoreographerFactory(dependencyProvider);
    }

    public static Choreographer providesChoreographer(DependencyProvider dependencyProvider) {
        return (Choreographer) Preconditions.checkNotNullFromProvides(dependencyProvider.providesChoreographer());
    }
}
