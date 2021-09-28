package com.android.systemui.controls.ui;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsActivity_Factory implements Factory<ControlsActivity> {
    private final Provider<ControlsUiController> uiControllerProvider;

    public ControlsActivity_Factory(Provider<ControlsUiController> provider) {
        this.uiControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public ControlsActivity get() {
        return newInstance(this.uiControllerProvider.get());
    }

    public static ControlsActivity_Factory create(Provider<ControlsUiController> provider) {
        return new ControlsActivity_Factory(provider);
    }

    public static ControlsActivity newInstance(ControlsUiController controlsUiController) {
        return new ControlsActivity(controlsUiController);
    }
}
