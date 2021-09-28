package com.google.android.systemui.assist.uihints;

import android.view.ViewGroup;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ScrimController_Factory implements Factory<ScrimController> {
    private final Provider<LightnessProvider> lightnessProvider;
    private final Provider<OverlappedElementController> overlappedElementControllerProvider;
    private final Provider<ViewGroup> parentProvider;
    private final Provider<TouchInsideHandler> touchInsideHandlerProvider;

    public ScrimController_Factory(Provider<ViewGroup> provider, Provider<OverlappedElementController> provider2, Provider<LightnessProvider> provider3, Provider<TouchInsideHandler> provider4) {
        this.parentProvider = provider;
        this.overlappedElementControllerProvider = provider2;
        this.lightnessProvider = provider3;
        this.touchInsideHandlerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ScrimController get() {
        return newInstance(this.parentProvider.get(), this.overlappedElementControllerProvider.get(), this.lightnessProvider.get(), this.touchInsideHandlerProvider.get());
    }

    public static ScrimController_Factory create(Provider<ViewGroup> provider, Provider<OverlappedElementController> provider2, Provider<LightnessProvider> provider3, Provider<TouchInsideHandler> provider4) {
        return new ScrimController_Factory(provider, provider2, provider3, provider4);
    }

    public static ScrimController newInstance(ViewGroup viewGroup, Object obj, Object obj2, TouchInsideHandler touchInsideHandler) {
        return new ScrimController(viewGroup, (OverlappedElementController) obj, (LightnessProvider) obj2, touchInsideHandler);
    }
}
