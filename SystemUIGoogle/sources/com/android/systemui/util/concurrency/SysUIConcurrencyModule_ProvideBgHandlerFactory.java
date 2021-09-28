package com.android.systemui.util.concurrency;

import android.os.Handler;
import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideBgHandlerFactory implements Factory<Handler> {
    private final Provider<Looper> bgLooperProvider;

    public SysUIConcurrencyModule_ProvideBgHandlerFactory(Provider<Looper> provider) {
        this.bgLooperProvider = provider;
    }

    @Override // javax.inject.Provider
    public Handler get() {
        return provideBgHandler(this.bgLooperProvider.get());
    }

    public static SysUIConcurrencyModule_ProvideBgHandlerFactory create(Provider<Looper> provider) {
        return new SysUIConcurrencyModule_ProvideBgHandlerFactory(provider);
    }

    public static Handler provideBgHandler(Looper looper) {
        return (Handler) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideBgHandler(looper));
    }
}
