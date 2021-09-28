package com.android.systemui.util.concurrency;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory implements Factory<Executor> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory INSTANCE = new SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory();
    }

    @Override // javax.inject.Provider
    public Executor get() {
        return provideUiBackgroundExecutor();
    }

    public static SysUIConcurrencyModule_ProvideUiBackgroundExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Executor provideUiBackgroundExecutor() {
        return (Executor) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideUiBackgroundExecutor());
    }
}
