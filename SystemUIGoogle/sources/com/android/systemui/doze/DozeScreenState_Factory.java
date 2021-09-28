package com.android.systemui.doze;

import android.os.Handler;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeScreenState_Factory implements Factory<DozeScreenState> {
    private final Provider<Handler> handlerProvider;
    private final Provider<DozeHost> hostProvider;
    private final Provider<DozeParameters> parametersProvider;
    private final Provider<DozeMachine.Service> serviceProvider;
    private final Provider<WakeLock> wakeLockProvider;

    public DozeScreenState_Factory(Provider<DozeMachine.Service> provider, Provider<Handler> provider2, Provider<DozeHost> provider3, Provider<DozeParameters> provider4, Provider<WakeLock> provider5) {
        this.serviceProvider = provider;
        this.handlerProvider = provider2;
        this.hostProvider = provider3;
        this.parametersProvider = provider4;
        this.wakeLockProvider = provider5;
    }

    @Override // javax.inject.Provider
    public DozeScreenState get() {
        return newInstance(this.serviceProvider.get(), this.handlerProvider.get(), this.hostProvider.get(), this.parametersProvider.get(), this.wakeLockProvider.get());
    }

    public static DozeScreenState_Factory create(Provider<DozeMachine.Service> provider, Provider<Handler> provider2, Provider<DozeHost> provider3, Provider<DozeParameters> provider4, Provider<WakeLock> provider5) {
        return new DozeScreenState_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static DozeScreenState newInstance(DozeMachine.Service service, Handler handler, DozeHost dozeHost, DozeParameters dozeParameters, WakeLock wakeLock) {
        return new DozeScreenState(service, handler, dozeHost, dozeParameters, wakeLock);
    }
}
