package com.google.android.systemui.autorotate;

import android.content.Context;
import android.hardware.SensorManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AutorotateDataService_Factory implements Factory<AutorotateDataService> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DataLogger> dataLoggerProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<SensorManager> sensorManagerProvider;

    public AutorotateDataService_Factory(Provider<Context> provider, Provider<SensorManager> provider2, Provider<DataLogger> provider3, Provider<BroadcastDispatcher> provider4, Provider<DeviceConfigProxy> provider5, Provider<DelayableExecutor> provider6) {
        this.contextProvider = provider;
        this.sensorManagerProvider = provider2;
        this.dataLoggerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.deviceConfigProvider = provider5;
        this.mainExecutorProvider = provider6;
    }

    @Override // javax.inject.Provider
    public AutorotateDataService get() {
        return newInstance(this.contextProvider.get(), this.sensorManagerProvider.get(), this.dataLoggerProvider.get(), this.broadcastDispatcherProvider.get(), this.deviceConfigProvider.get(), this.mainExecutorProvider.get());
    }

    public static AutorotateDataService_Factory create(Provider<Context> provider, Provider<SensorManager> provider2, Provider<DataLogger> provider3, Provider<BroadcastDispatcher> provider4, Provider<DeviceConfigProxy> provider5, Provider<DelayableExecutor> provider6) {
        return new AutorotateDataService_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static AutorotateDataService newInstance(Context context, SensorManager sensorManager, Object obj, BroadcastDispatcher broadcastDispatcher, DeviceConfigProxy deviceConfigProxy, DelayableExecutor delayableExecutor) {
        return new AutorotateDataService(context, sensorManager, (DataLogger) obj, broadcastDispatcher, deviceConfigProxy, delayableExecutor);
    }
}
