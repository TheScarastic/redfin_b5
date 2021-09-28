package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ScreenTouch_Factory implements Factory<ScreenTouch> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<PowerState> powerStateProvider;

    public ScreenTouch_Factory(Provider<Context> provider, Provider<PowerState> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.powerStateProvider = provider2;
        this.handlerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public ScreenTouch get() {
        return newInstance(this.contextProvider.get(), this.powerStateProvider.get(), this.handlerProvider.get());
    }

    public static ScreenTouch_Factory create(Provider<Context> provider, Provider<PowerState> provider2, Provider<Handler> provider3) {
        return new ScreenTouch_Factory(provider, provider2, provider3);
    }

    public static ScreenTouch newInstance(Context context, PowerState powerState, Handler handler) {
        return new ScreenTouch(context, powerState, handler);
    }
}
