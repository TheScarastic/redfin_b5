package com.android.systemui.wmshell;

import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory implements Factory<ShellExecutor> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory INSTANCE = new WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory();
    }

    @Override // javax.inject.Provider
    public ShellExecutor get() {
        return provideShellAnimationExecutor();
    }

    public static WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ShellExecutor provideShellAnimationExecutor() {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideShellAnimationExecutor());
    }
}
