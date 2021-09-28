package com.android.systemui.sensorprivacy;

import android.os.Handler;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SensorUseStartedActivity_Factory implements Factory<SensorUseStartedActivity> {
    private final Provider<Handler> bgHandlerProvider;
    private final Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<IndividualSensorPrivacyController> sensorPrivacyControllerProvider;

    public SensorUseStartedActivity_Factory(Provider<IndividualSensorPrivacyController> provider, Provider<KeyguardStateController> provider2, Provider<KeyguardDismissUtil> provider3, Provider<Handler> provider4) {
        this.sensorPrivacyControllerProvider = provider;
        this.keyguardStateControllerProvider = provider2;
        this.keyguardDismissUtilProvider = provider3;
        this.bgHandlerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public SensorUseStartedActivity get() {
        return newInstance(this.sensorPrivacyControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.keyguardDismissUtilProvider.get(), this.bgHandlerProvider.get());
    }

    public static SensorUseStartedActivity_Factory create(Provider<IndividualSensorPrivacyController> provider, Provider<KeyguardStateController> provider2, Provider<KeyguardDismissUtil> provider3, Provider<Handler> provider4) {
        return new SensorUseStartedActivity_Factory(provider, provider2, provider3, provider4);
    }

    public static SensorUseStartedActivity newInstance(IndividualSensorPrivacyController individualSensorPrivacyController, KeyguardStateController keyguardStateController, KeyguardDismissUtil keyguardDismissUtil, Handler handler) {
        return new SensorUseStartedActivity(individualSensorPrivacyController, keyguardStateController, keyguardDismissUtil, handler);
    }
}
