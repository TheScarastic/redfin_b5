package com.android.systemui.tuner;

import com.android.systemui.tuner.TunablePadding;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TunablePadding_TunablePaddingService_Factory implements Factory<TunablePadding.TunablePaddingService> {
    private final Provider<TunerService> tunerServiceProvider;

    public TunablePadding_TunablePaddingService_Factory(Provider<TunerService> provider) {
        this.tunerServiceProvider = provider;
    }

    @Override // javax.inject.Provider
    public TunablePadding.TunablePaddingService get() {
        return newInstance(this.tunerServiceProvider.get());
    }

    public static TunablePadding_TunablePaddingService_Factory create(Provider<TunerService> provider) {
        return new TunablePadding_TunablePaddingService_Factory(provider);
    }

    public static TunablePadding.TunablePaddingService newInstance(TunerService tunerService) {
        return new TunablePadding.TunablePaddingService(tunerService);
    }
}
