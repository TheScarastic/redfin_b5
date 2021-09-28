package com.android.systemui.util.sensors;

import android.hardware.SensorManager;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SensorModule_ProvidePrimaryProxSensorFactory implements Factory<ThresholdSensor> {
    private final Provider<SensorManager> sensorManagerProvider;
    private final Provider<ThresholdSensorImpl.Builder> thresholdSensorBuilderProvider;

    public SensorModule_ProvidePrimaryProxSensorFactory(Provider<SensorManager> provider, Provider<ThresholdSensorImpl.Builder> provider2) {
        this.sensorManagerProvider = provider;
        this.thresholdSensorBuilderProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ThresholdSensor get() {
        return providePrimaryProxSensor(this.sensorManagerProvider.get(), this.thresholdSensorBuilderProvider.get());
    }

    public static SensorModule_ProvidePrimaryProxSensorFactory create(Provider<SensorManager> provider, Provider<ThresholdSensorImpl.Builder> provider2) {
        return new SensorModule_ProvidePrimaryProxSensorFactory(provider, provider2);
    }

    public static ThresholdSensor providePrimaryProxSensor(SensorManager sensorManager, Object obj) {
        return (ThresholdSensor) Preconditions.checkNotNullFromProvides(SensorModule.providePrimaryProxSensor(sensorManager, (ThresholdSensorImpl.Builder) obj));
    }
}
