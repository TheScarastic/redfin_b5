package com.google.android.systemui.columbus.sensors.config;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SensorConfiguration_Factory implements Factory<SensorConfiguration> {
    private final Provider<Context> contextProvider;

    public SensorConfiguration_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public SensorConfiguration get() {
        return newInstance(this.contextProvider.get());
    }

    public static SensorConfiguration_Factory create(Provider<Context> provider) {
        return new SensorConfiguration_Factory(provider);
    }

    public static SensorConfiguration newInstance(Context context) {
        return new SensorConfiguration(context);
    }
}
