package com.android.systemui.volume;

import android.content.Context;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class VolumeDialogComponent_Factory implements Factory<VolumeDialogComponent> {
    private final Provider<Context> contextProvider;
    private final Provider<DemoModeController> demoModeControllerProvider;
    private final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    private final Provider<VolumeDialogControllerImpl> volumeDialogControllerProvider;

    public VolumeDialogComponent_Factory(Provider<Context> provider, Provider<KeyguardViewMediator> provider2, Provider<VolumeDialogControllerImpl> provider3, Provider<DemoModeController> provider4) {
        this.contextProvider = provider;
        this.keyguardViewMediatorProvider = provider2;
        this.volumeDialogControllerProvider = provider3;
        this.demoModeControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public VolumeDialogComponent get() {
        return newInstance(this.contextProvider.get(), this.keyguardViewMediatorProvider.get(), this.volumeDialogControllerProvider.get(), this.demoModeControllerProvider.get());
    }

    public static VolumeDialogComponent_Factory create(Provider<Context> provider, Provider<KeyguardViewMediator> provider2, Provider<VolumeDialogControllerImpl> provider3, Provider<DemoModeController> provider4) {
        return new VolumeDialogComponent_Factory(provider, provider2, provider3, provider4);
    }

    public static VolumeDialogComponent newInstance(Context context, KeyguardViewMediator keyguardViewMediator, VolumeDialogControllerImpl volumeDialogControllerImpl, DemoModeController demoModeController) {
        return new VolumeDialogComponent(context, keyguardViewMediator, volumeDialogControllerImpl, demoModeController);
    }
}
