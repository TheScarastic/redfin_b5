package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideLongRunningExecutorFactory implements Factory<Executor> {
    private final Provider<Looper> looperProvider;

    public SysUIConcurrencyModule_ProvideLongRunningExecutorFactory(Provider<Looper> provider) {
        this.looperProvider = provider;
    }

    @Override // javax.inject.Provider
    public Executor get() {
        return provideLongRunningExecutor(this.looperProvider.get());
    }

    public static SysUIConcurrencyModule_ProvideLongRunningExecutorFactory create(Provider<Looper> provider) {
        return new SysUIConcurrencyModule_ProvideLongRunningExecutorFactory(provider);
    }

    public static Executor provideLongRunningExecutor(Looper looper) {
        return (Executor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideLongRunningExecutor(looper));
    }
}
