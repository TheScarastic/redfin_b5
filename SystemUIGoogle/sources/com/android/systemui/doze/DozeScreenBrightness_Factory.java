package com.android.systemui.doze;

import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeScreenBrightness_Factory implements Factory<DozeScreenBrightness> {
    private final Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DozeParameters> dozeParametersProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<DozeHost> hostProvider;
    private final Provider<Optional<Sensor>> lightSensorOptionalProvider;
    private final Provider<AsyncSensorManager> sensorManagerProvider;
    private final Provider<DozeMachine.Service> serviceProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public DozeScreenBrightness_Factory(Provider<Context> provider, Provider<DozeMachine.Service> provider2, Provider<AsyncSensorManager> provider3, Provider<Optional<Sensor>> provider4, Provider<DozeHost> provider5, Provider<Handler> provider6, Provider<AlwaysOnDisplayPolicy> provider7, Provider<WakefulnessLifecycle> provider8, Provider<DozeParameters> provider9) {
        this.contextProvider = provider;
        this.serviceProvider = provider2;
        this.sensorManagerProvider = provider3;
        this.lightSensorOptionalProvider = provider4;
        this.hostProvider = provider5;
        this.handlerProvider = provider6;
        this.alwaysOnDisplayPolicyProvider = provider7;
        this.wakefulnessLifecycleProvider = provider8;
        this.dozeParametersProvider = provider9;
    }

    @Override // javax.inject.Provider
    public DozeScreenBrightness get() {
        return newInstance(this.contextProvider.get(), this.serviceProvider.get(), this.sensorManagerProvider.get(), this.lightSensorOptionalProvider.get(), this.hostProvider.get(), this.handlerProvider.get(), this.alwaysOnDisplayPolicyProvider.get(), this.wakefulnessLifecycleProvider.get(), this.dozeParametersProvider.get());
    }

    public static DozeScreenBrightness_Factory create(Provider<Context> provider, Provider<DozeMachine.Service> provider2, Provider<AsyncSensorManager> provider3, Provider<Optional<Sensor>> provider4, Provider<DozeHost> provider5, Provider<Handler> provider6, Provider<AlwaysOnDisplayPolicy> provider7, Provider<WakefulnessLifecycle> provider8, Provider<DozeParameters> provider9) {
        return new DozeScreenBrightness_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static DozeScreenBrightness newInstance(Context context, DozeMachine.Service service, AsyncSensorManager asyncSensorManager, Optional<Sensor> optional, DozeHost dozeHost, Handler handler, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, WakefulnessLifecycle wakefulnessLifecycle, DozeParameters dozeParameters) {
        return new DozeScreenBrightness(context, service, asyncSensorManager, optional, dozeHost, handler, alwaysOnDisplayPolicy, wakefulnessLifecycle, dozeParameters);
    }
}
