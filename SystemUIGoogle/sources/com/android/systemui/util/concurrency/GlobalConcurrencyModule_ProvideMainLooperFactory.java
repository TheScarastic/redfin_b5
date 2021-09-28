package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class GlobalConcurrencyModule_ProvideMainLooperFactory implements Factory<Looper> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final GlobalConcurrencyModule_ProvideMainLooperFactory INSTANCE = new GlobalConcurrencyModule_ProvideMainLooperFactory();
    }

    @Override // javax.inject.Provider
    public Looper get() {
        return provideMainLooper();
    }

    public static GlobalConcurrencyModule_ProvideMainLooperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Looper provideMainLooper() {
        return (Looper) Preconditions.checkNotNullFromProvides(GlobalConcurrencyModule.provideMainLooper());
    }
}
