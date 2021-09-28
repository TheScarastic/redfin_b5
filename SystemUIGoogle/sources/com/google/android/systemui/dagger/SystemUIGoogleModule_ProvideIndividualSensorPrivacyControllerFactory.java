package com.google.android.systemui.dagger;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideIndividualSensorPrivacyControllerFactory implements Factory<IndividualSensorPrivacyController> {
    private final Provider<SensorPrivacyManager> sensorPrivacyManagerProvider;

    public SystemUIGoogleModule_ProvideIndividualSensorPrivacyControllerFactory(Provider<SensorPrivacyManager> provider) {
        this.sensorPrivacyManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public IndividualSensorPrivacyController get() {
        return provideIndividualSensorPrivacyController(this.sensorPrivacyManagerProvider.get());
    }

    public static SystemUIGoogleModule_ProvideIndividualSensorPrivacyControllerFactory create(Provider<SensorPrivacyManager> provider) {
        return new SystemUIGoogleModule_ProvideIndividualSensorPrivacyControllerFactory(provider);
    }

    public static IndividualSensorPrivacyController provideIndividualSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        return (IndividualSensorPrivacyController) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideIndividualSensorPrivacyController(sensorPrivacyManager));
    }
}
