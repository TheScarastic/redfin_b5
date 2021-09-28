package com.android.systemui.controls.dagger;

import android.content.pm.PackageManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsModule_ProvidesControlsFeatureEnabledFactory implements Factory<Boolean> {
    private final Provider<PackageManager> pmProvider;

    public ControlsModule_ProvidesControlsFeatureEnabledFactory(Provider<PackageManager> provider) {
        this.pmProvider = provider;
    }

    @Override // javax.inject.Provider
    public Boolean get() {
        return Boolean.valueOf(providesControlsFeatureEnabled(this.pmProvider.get()));
    }

    public static ControlsModule_ProvidesControlsFeatureEnabledFactory create(Provider<PackageManager> provider) {
        return new ControlsModule_ProvidesControlsFeatureEnabledFactory(provider);
    }

    public static boolean providesControlsFeatureEnabled(PackageManager packageManager) {
        return ControlsModule.providesControlsFeatureEnabled(packageManager);
    }
}
