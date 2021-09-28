package com.android.systemui.util.sensors;

import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ProximitySensor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ProximitySensor_ProximityCheck_Factory implements Factory<ProximitySensor.ProximityCheck> {
    private final Provider<DelayableExecutor> delayableExecutorProvider;
    private final Provider<ProximitySensor> sensorProvider;

    public ProximitySensor_ProximityCheck_Factory(Provider<ProximitySensor> provider, Provider<DelayableExecutor> provider2) {
        this.sensorProvider = provider;
        this.delayableExecutorProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ProximitySensor.ProximityCheck get() {
        return newInstance(this.sensorProvider.get(), this.delayableExecutorProvider.get());
    }

    public static ProximitySensor_ProximityCheck_Factory create(Provider<ProximitySensor> provider, Provider<DelayableExecutor> provider2) {
        return new ProximitySensor_ProximityCheck_Factory(provider, provider2);
    }

    public static ProximitySensor.ProximityCheck newInstance(ProximitySensor proximitySensor, DelayableExecutor delayableExecutor) {
        return new ProximitySensor.ProximityCheck(proximitySensor, delayableExecutor);
    }
}
