package com.android.systemui.dagger;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideSensorPrivacyControllerFactory implements Factory<SensorPrivacyController> {
    private final Provider<SensorPrivacyManager> sensorPrivacyManagerProvider;

    public SystemUIDefaultModule_ProvideSensorPrivacyControllerFactory(Provider<SensorPrivacyManager> provider) {
        this.sensorPrivacyManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public SensorPrivacyController get() {
        return provideSensorPrivacyController(this.sensorPrivacyManagerProvider.get());
    }

    public static SystemUIDefaultModule_ProvideSensorPrivacyControllerFactory create(Provider<SensorPrivacyManager> provider) {
        return new SystemUIDefaultModule_ProvideSensorPrivacyControllerFactory(provider);
    }

    public static SensorPrivacyController provideSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        return (SensorPrivacyController) Preconditions.checkNotNullFromProvides(SystemUIDefaultModule.provideSensorPrivacyController(sensorPrivacyManager));
    }
}
