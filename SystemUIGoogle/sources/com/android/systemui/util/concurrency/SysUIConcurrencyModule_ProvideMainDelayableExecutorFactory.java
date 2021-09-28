package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideMainDelayableExecutorFactory implements Factory<DelayableExecutor> {
    private final Provider<Looper> looperProvider;

    public SysUIConcurrencyModule_ProvideMainDelayableExecutorFactory(Provider<Looper> provider) {
        this.looperProvider = provider;
    }

    @Override // javax.inject.Provider
    public DelayableExecutor get() {
        return provideMainDelayableExecutor(this.looperProvider.get());
    }

    public static SysUIConcurrencyModule_ProvideMainDelayableExecutorFactory create(Provider<Looper> provider) {
        return new SysUIConcurrencyModule_ProvideMainDelayableExecutorFactory(provider);
    }

    public static DelayableExecutor provideMainDelayableExecutor(Looper looper) {
        return (DelayableExecutor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideMainDelayableExecutor(looper));
    }
}
