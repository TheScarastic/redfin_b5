package com.android.systemui.wmshell;

import android.os.Handler;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory implements Factory<ShellExecutor> {
    private final Provider<Handler> sysuiMainHandlerProvider;

    public WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory(Provider<Handler> provider) {
        this.sysuiMainHandlerProvider = provider;
    }

    @Override // javax.inject.Provider
    public ShellExecutor get() {
        return provideSysUIMainExecutor(this.sysuiMainHandlerProvider.get());
    }

    public static WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory create(Provider<Handler> provider) {
        return new WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory(provider);
    }

    public static ShellExecutor provideSysUIMainExecutor(Handler handler) {
        return (ShellExecutor) Preconditions.checkNotNullFromProvides(WMShellConcurrencyModule.provideSysUIMainExecutor(handler));
    }
}
