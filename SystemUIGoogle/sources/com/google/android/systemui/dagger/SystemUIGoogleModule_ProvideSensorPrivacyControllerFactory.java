package com.google.android.systemui.dagger;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideSensorPrivacyControllerFactory implements Factory<SensorPrivacyController> {
    private final Provider<SensorPrivacyManager> sensorPrivacyManagerProvider;

    public SystemUIGoogleModule_ProvideSensorPrivacyControllerFactory(Provider<SensorPrivacyManager> provider) {
        this.sensorPrivacyManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public SensorPrivacyController get() {
        return provideSensorPrivacyController(this.sensorPrivacyManagerProvider.get());
    }

    public static SystemUIGoogleModule_ProvideSensorPrivacyControllerFactory create(Provider<SensorPrivacyManager> provider) {
        return new SystemUIGoogleModule_ProvideSensorPrivacyControllerFactory(provider);
    }

    public static SensorPrivacyController provideSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        return (SensorPrivacyController) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideSensorPrivacyController(sensorPrivacyManager));
    }
}
