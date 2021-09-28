package com.android.systemui.util.sensors;

import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ProximitySensor_Factory implements Factory<ProximitySensor> {
    private final Provider<DelayableExecutor> delayableExecutorProvider;
    private final Provider<Execution> executionProvider;
    private final Provider<ThresholdSensor> primaryProvider;
    private final Provider<ThresholdSensor> secondaryProvider;

    public ProximitySensor_Factory(Provider<ThresholdSensor> provider, Provider<ThresholdSensor> provider2, Provider<DelayableExecutor> provider3, Provider<Execution> provider4) {
        this.primaryProvider = provider;
        this.secondaryProvider = provider2;
        this.delayableExecutorProvider = provider3;
        this.executionProvider = provider4;
    }

    @Override // javax.inject.Provider
    public ProximitySensor get() {
        return newInstance(this.primaryProvider.get(), this.secondaryProvider.get(), this.delayableExecutorProvider.get(), this.executionProvider.get());
    }

    public static ProximitySensor_Factory create(Provider<ThresholdSensor> provider, Provider<ThresholdSensor> provider2, Provider<DelayableExecutor> provider3, Provider<Execution> provider4) {
        return new ProximitySensor_Factory(provider, provider2, provider3, provider4);
    }

    public static ProximitySensor newInstance(ThresholdSensor thresholdSensor, ThresholdSensor thresholdSensor2, DelayableExecutor delayableExecutor, Execution execution) {
        return new ProximitySensor(thresholdSensor, thresholdSensor2, delayableExecutor, execution);
    }
}
