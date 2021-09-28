package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.accessibility.ModeSwitchesController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvidesModeSwitchesControllerFactory implements Factory<ModeSwitchesController> {
    private final Provider<Context> contextProvider;
    private final DependencyProvider module;

    public DependencyProvider_ProvidesModeSwitchesControllerFactory(DependencyProvider dependencyProvider, Provider<Context> provider) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public ModeSwitchesController get() {
        return providesModeSwitchesController(this.module, this.contextProvider.get());
    }

    public static DependencyProvider_ProvidesModeSwitchesControllerFactory create(DependencyProvider dependencyProvider, Provider<Context> provider) {
        return new DependencyProvider_ProvidesModeSwitchesControllerFactory(dependencyProvider, provider);
    }

    public static ModeSwitchesController providesModeSwitchesController(DependencyProvider dependencyProvider, Context context) {
        return (ModeSwitchesController) Preconditions.checkNotNullFromProvides(dependencyProvider.providesModeSwitchesController(context));
    }
}
