package com.google.android.systemui.columbus.sensors.config;

import dagger.internal.Factory;
import java.util.List;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GestureConfiguration_Factory implements Factory<GestureConfiguration> {
    private final Provider<List<Adjustment>> adjustmentsProvider;
    private final Provider<SensorConfiguration> sensorConfigurationProvider;

    public GestureConfiguration_Factory(Provider<List<Adjustment>> provider, Provider<SensorConfiguration> provider2) {
        this.adjustmentsProvider = provider;
        this.sensorConfigurationProvider = provider2;
    }

    @Override // javax.inject.Provider
    public GestureConfiguration get() {
        return newInstance(this.adjustmentsProvider.get(), this.sensorConfigurationProvider.get());
    }

    public static GestureConfiguration_Factory create(Provider<List<Adjustment>> provider, Provider<SensorConfiguration> provider2) {
        return new GestureConfiguration_Factory(provider, provider2);
    }

    public static GestureConfiguration newInstance(List<Adjustment> list, SensorConfiguration sensorConfiguration) {
        return new GestureConfiguration(list, sensorConfiguration);
    }
}
