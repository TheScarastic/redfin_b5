package com.google.android.systemui.columbus.sensors.config;

import android.content.Context;
import com.google.android.systemui.columbus.ColumbusSettings;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class LowSensitivitySettingAdjustment_Factory implements Factory<LowSensitivitySettingAdjustment> {
    private final Provider<ColumbusSettings> columbusSettingsProvider;
    private final Provider<Context> contextProvider;
    private final Provider<SensorConfiguration> sensorConfigurationProvider;

    public LowSensitivitySettingAdjustment_Factory(Provider<Context> provider, Provider<ColumbusSettings> provider2, Provider<SensorConfiguration> provider3) {
        this.contextProvider = provider;
        this.columbusSettingsProvider = provider2;
        this.sensorConfigurationProvider = provider3;
    }

    @Override // javax.inject.Provider
    public LowSensitivitySettingAdjustment get() {
        return newInstance(this.contextProvider.get(), this.columbusSettingsProvider.get(), this.sensorConfigurationProvider.get());
    }

    public static LowSensitivitySettingAdjustment_Factory create(Provider<Context> provider, Provider<ColumbusSettings> provider2, Provider<SensorConfiguration> provider3) {
        return new LowSensitivitySettingAdjustment_Factory(provider, provider2, provider3);
    }

    public static LowSensitivitySettingAdjustment newInstance(Context context, ColumbusSettings columbusSettings, SensorConfiguration sensorConfiguration) {
        return new LowSensitivitySettingAdjustment(context, columbusSettings, sensorConfiguration);
    }
}
