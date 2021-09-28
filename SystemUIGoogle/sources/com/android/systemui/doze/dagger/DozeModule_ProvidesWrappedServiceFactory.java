package com.android.systemui.doze.dagger;

import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeModule_ProvidesWrappedServiceFactory implements Factory<DozeMachine.Service> {
    private final Provider<DozeHost> dozeHostProvider;
    private final Provider<DozeMachine.Service> dozeMachineServiceProvider;
    private final Provider<DozeParameters> dozeParametersProvider;

    public DozeModule_ProvidesWrappedServiceFactory(Provider<DozeMachine.Service> provider, Provider<DozeHost> provider2, Provider<DozeParameters> provider3) {
        this.dozeMachineServiceProvider = provider;
        this.dozeHostProvider = provider2;
        this.dozeParametersProvider = provider3;
    }

    @Override // javax.inject.Provider
    public DozeMachine.Service get() {
        return providesWrappedService(this.dozeMachineServiceProvider.get(), this.dozeHostProvider.get(), this.dozeParametersProvider.get());
    }

    public static DozeModule_ProvidesWrappedServiceFactory create(Provider<DozeMachine.Service> provider, Provider<DozeHost> provider2, Provider<DozeParameters> provider3) {
        return new DozeModule_ProvidesWrappedServiceFactory(provider, provider2, provider3);
    }

    public static DozeMachine.Service providesWrappedService(DozeMachine.Service service, DozeHost dozeHost, DozeParameters dozeParameters) {
        return (DozeMachine.Service) Preconditions.checkNotNullFromProvides(DozeModule.providesWrappedService(service, dozeHost, dozeParameters));
    }
}
