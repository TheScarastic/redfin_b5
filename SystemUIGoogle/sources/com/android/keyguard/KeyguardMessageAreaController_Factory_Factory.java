package com.android.keyguard;

import com.android.keyguard.KeyguardMessageAreaController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardMessageAreaController_Factory_Factory implements Factory<KeyguardMessageAreaController.Factory> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;

    public KeyguardMessageAreaController_Factory_Factory(Provider<KeyguardUpdateMonitor> provider, Provider<ConfigurationController> provider2) {
        this.keyguardUpdateMonitorProvider = provider;
        this.configurationControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public KeyguardMessageAreaController.Factory get() {
        return newInstance(this.keyguardUpdateMonitorProvider.get(), this.configurationControllerProvider.get());
    }

    public static KeyguardMessageAreaController_Factory_Factory create(Provider<KeyguardUpdateMonitor> provider, Provider<ConfigurationController> provider2) {
        return new KeyguardMessageAreaController_Factory_Factory(provider, provider2);
    }

    public static KeyguardMessageAreaController.Factory newInstance(KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController) {
        return new KeyguardMessageAreaController.Factory(keyguardUpdateMonitor, configurationController);
    }
}
