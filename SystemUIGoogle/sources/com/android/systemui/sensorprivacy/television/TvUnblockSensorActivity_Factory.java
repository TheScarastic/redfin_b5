package com.android.systemui.sensorprivacy.television;

import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvUnblockSensorActivity_Factory implements Factory<TvUnblockSensorActivity> {
    private final Provider<IndividualSensorPrivacyController> individualSensorPrivacyControllerProvider;

    public TvUnblockSensorActivity_Factory(Provider<IndividualSensorPrivacyController> provider) {
        this.individualSensorPrivacyControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public TvUnblockSensorActivity get() {
        return newInstance(this.individualSensorPrivacyControllerProvider.get());
    }

    public static TvUnblockSensorActivity_Factory create(Provider<IndividualSensorPrivacyController> provider) {
        return new TvUnblockSensorActivity_Factory(provider);
    }

    public static TvUnblockSensorActivity newInstance(IndividualSensorPrivacyController individualSensorPrivacyController) {
        return new TvUnblockSensorActivity(individualSensorPrivacyController);
    }
}
