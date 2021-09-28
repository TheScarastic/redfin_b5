package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideBgLooperFactory implements Factory<Looper> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SysUIConcurrencyModule_ProvideBgLooperFactory INSTANCE = new SysUIConcurrencyModule_ProvideBgLooperFactory();
    }

    @Override // javax.inject.Provider
    public Looper get() {
        return provideBgLooper();
    }

    public static SysUIConcurrencyModule_ProvideBgLooperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Looper provideBgLooper() {
        return (Looper) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideBgLooper());
    }
}
