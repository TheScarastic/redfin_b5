package com.android.systemui.dagger;

import android.hardware.SensorPrivacyManager;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideIndividualSensorPrivacyControllerFactory implements Factory<IndividualSensorPrivacyController> {
    private final Provider<SensorPrivacyManager> sensorPrivacyManagerProvider;

    public SystemUIDefaultModule_ProvideIndividualSensorPrivacyControllerFactory(Provider<SensorPrivacyManager> provider) {
        this.sensorPrivacyManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public IndividualSensorPrivacyController get() {
        return provideIndividualSensorPrivacyController(this.sensorPrivacyManagerProvider.get());
    }

    public static SystemUIDefaultModule_ProvideIndividualSensorPrivacyControllerFactory create(Provider<SensorPrivacyManager> provider) {
        return new SystemUIDefaultModule_ProvideIndividualSensorPrivacyControllerFactory(provider);
    }

    public static IndividualSensorPrivacyController provideIndividualSensorPrivacyController(SensorPrivacyManager sensorPrivacyManager) {
        return (IndividualSensorPrivacyController) Preconditions.checkNotNullFromProvides(SystemUIDefaultModule.provideIndividualSensorPrivacyController(sensorPrivacyManager));
    }
}
