package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideBackgroundExecutorFactory implements Factory<Executor> {
    private final Provider<Looper> looperProvider;

    public SysUIConcurrencyModule_ProvideBackgroundExecutorFactory(Provider<Looper> provider) {
        this.looperProvider = provider;
    }

    @Override // javax.inject.Provider
    public Executor get() {
        return provideBackgroundExecutor(this.looperProvider.get());
    }

    public static SysUIConcurrencyModule_ProvideBackgroundExecutorFactory create(Provider<Looper> provider) {
        return new SysUIConcurrencyModule_ProvideBackgroundExecutorFactory(provider);
    }

    public static Executor provideBackgroundExecutor(Looper looper) {
        return (Executor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideBackgroundExecutor(looper));
    }
}
