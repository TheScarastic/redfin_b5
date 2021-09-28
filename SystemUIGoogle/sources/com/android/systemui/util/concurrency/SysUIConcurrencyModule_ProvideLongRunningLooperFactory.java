package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class SysUIConcurrencyModule_ProvideLongRunningLooperFactory implements Factory<Looper> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SysUIConcurrencyModule_ProvideLongRunningLooperFactory INSTANCE = new SysUIConcurrencyModule_ProvideLongRunningLooperFactory();
    }

    @Override // javax.inject.Provider
    public Looper get() {
        return provideLongRunningLooper();
    }

    public static SysUIConcurrencyModule_ProvideLongRunningLooperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Looper provideLongRunningLooper() {
        return (Looper) Preconditions.checkNotNullFromProvides(SysUIConcurrencyModule.provideLongRunningLooper());
    }
}
