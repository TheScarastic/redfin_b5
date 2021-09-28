package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.phone.PipAppOpsListener;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvidePipAppOpsListenerFactory implements Factory<PipAppOpsListener> {
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<PipTouchHandler> pipTouchHandlerProvider;

    public WMShellBaseModule_ProvidePipAppOpsListenerFactory(Provider<Context> provider, Provider<PipTouchHandler> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.pipTouchHandlerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    @Override // javax.inject.Provider
    public PipAppOpsListener get() {
        return providePipAppOpsListener(this.contextProvider.get(), this.pipTouchHandlerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvidePipAppOpsListenerFactory create(Provider<Context> provider, Provider<PipTouchHandler> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellBaseModule_ProvidePipAppOpsListenerFactory(provider, provider2, provider3);
    }

    public static PipAppOpsListener providePipAppOpsListener(Context context, PipTouchHandler pipTouchHandler, ShellExecutor shellExecutor) {
        return (PipAppOpsListener) Preconditions.checkNotNullFromProvides(WMShellBaseModule.providePipAppOpsListener(context, pipTouchHandler, shellExecutor));
    }
}
