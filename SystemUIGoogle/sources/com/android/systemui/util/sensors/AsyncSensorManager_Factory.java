package com.android.systemui.util.sensors;

import android.hardware.SensorManager;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.concurrency.ThreadFactory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AsyncSensorManager_Factory implements Factory<AsyncSensorManager> {
    private final Provider<PluginManager> pluginManagerProvider;
    private final Provider<SensorManager> sensorManagerProvider;
    private final Provider<ThreadFactory> threadFactoryProvider;

    public AsyncSensorManager_Factory(Provider<SensorManager> provider, Provider<ThreadFactory> provider2, Provider<PluginManager> provider3) {
        this.sensorManagerProvider = provider;
        this.threadFactoryProvider = provider2;
        this.pluginManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public AsyncSensorManager get() {
        return newInstance(this.sensorManagerProvider.get(), this.threadFactoryProvider.get(), this.pluginManagerProvider.get());
    }

    public static AsyncSensorManager_Factory create(Provider<SensorManager> provider, Provider<ThreadFactory> provider2, Provider<PluginManager> provider3) {
        return new AsyncSensorManager_Factory(provider, provider2, provider3);
    }

    public static AsyncSensorManager newInstance(SensorManager sensorManager, ThreadFactory threadFactory, PluginManager pluginManager) {
        return new AsyncSensorManager(sensorManager, threadFactory, pluginManager);
    }
}
