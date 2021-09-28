package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ChargingState_Factory implements Factory<ChargingState> {
    private final Provider<Context> contextProvider;
    private final Provider<Long> gateDurationProvider;
    private final Provider<Handler> handlerProvider;

    public ChargingState_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<Long> provider3) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.gateDurationProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ChargingState get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.gateDurationProvider.get().longValue());
    }

    public static ChargingState_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<Long> provider3) {
        return new ChargingState_Factory(provider, provider2, provider3);
    }

    public static ChargingState newInstance(Context context, Handler handler, long j) {
        return new ChargingState(context, handler, j);
    }
}
