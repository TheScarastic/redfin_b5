package com.android.systemui.wmshell;

import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory implements Factory<ShellExecutor> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory INSTANCE = new WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory();
    }

    @Override // javax.inject.Provider
    public ShellExecutor get() {
        return provideSplashScreenExecutor();
    }

    public static WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ShellExecutor provideSplashScreenExecutor() {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideSplashScreenExecutor());
    }
}
