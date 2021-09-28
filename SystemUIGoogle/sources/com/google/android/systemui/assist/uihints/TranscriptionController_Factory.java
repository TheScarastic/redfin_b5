package com.google.android.systemui.assist.uihints;

import android.view.ViewGroup;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TranscriptionController_Factory implements Factory<TranscriptionController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<TouchInsideHandler> defaultOnTapProvider;
    private final Provider<FlingVelocityWrapper> flingVelocityProvider;
    private final Provider<ViewGroup> parentProvider;

    public TranscriptionController_Factory(Provider<ViewGroup> provider, Provider<TouchInsideHandler> provider2, Provider<FlingVelocityWrapper> provider3, Provider<ConfigurationController> provider4) {
        this.parentProvider = provider;
        this.defaultOnTapProvider = provider2;
        this.flingVelocityProvider = provider3;
        this.configurationControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public TranscriptionController get() {
        return newInstance(this.parentProvider.get(), this.defaultOnTapProvider.get(), this.flingVelocityProvider.get(), this.configurationControllerProvider.get());
    }

    public static TranscriptionController_Factory create(Provider<ViewGroup> provider, Provider<TouchInsideHandler> provider2, Provider<FlingVelocityWrapper> provider3, Provider<ConfigurationController> provider4) {
        return new TranscriptionController_Factory(provider, provider2, provider3, provider4);
    }

    public static TranscriptionController newInstance(ViewGroup viewGroup, TouchInsideHandler touchInsideHandler, Object obj, ConfigurationController configurationController) {
        return new TranscriptionController(viewGroup, touchInsideHandler, (FlingVelocityWrapper) obj, configurationController);
    }
}
