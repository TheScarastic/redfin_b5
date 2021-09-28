package com.android.systemui.util;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class CarrierConfigTracker_Factory implements Factory<CarrierConfigTracker> {
    private final Provider<Context> contextProvider;

    public CarrierConfigTracker_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public CarrierConfigTracker get() {
        return newInstance(this.contextProvider.get());
    }

    public static CarrierConfigTracker_Factory create(Provider<Context> provider) {
        return new CarrierConfigTracker_Factory(provider);
    }

    public static CarrierConfigTracker newInstance(Context context) {
        return new CarrierConfigTracker(context);
    }
}
