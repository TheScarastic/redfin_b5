package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideDelayableExecutorFactory implements Factory<DelayableExecutor> {
    private final Provider<Looper> looperProvider;

    public SysUIConcurrencyModule_ProvideDelayableExecutorFactory(Provider<Looper> provider) {
        this.looperProvider = provider;
    }

    @Override // javax.inject.Provider
    public DelayableExecutor get() {
        return provideDelayableExecutor(this.looperProvider.get());
    }

    public static SysUIConcurrencyModule_ProvideDelayableExecutorFactory create(Provider<Looper> provider) {
        return new SysUIConcurrencyModule_ProvideDelayableExecutorFactory(provider);
    }

    public static DelayableExecutor provideDelayableExecutor(Looper looper) {
        return (DelayableExecutor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideDelayableExecutor(looper));
    }
}
