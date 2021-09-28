package com.android.systemui.wmshell;

import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideWindowManagerShellWrapperFactory implements Factory<WindowManagerShellWrapper> {
    private final Provider<ShellExecutor> mainExecutorProvider;

    public WMShellBaseModule_ProvideWindowManagerShellWrapperFactory(Provider<ShellExecutor> provider) {
        this.mainExecutorProvider = provider;
    }

    @Override // javax.inject.Provider
    public WindowManagerShellWrapper get() {
        return provideWindowManagerShellWrapper(this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideWindowManagerShellWrapperFactory create(Provider<ShellExecutor> provider) {
        return new WMShellBaseModule_ProvideWindowManagerShellWrapperFactory(provider);
    }

    public static WindowManagerShellWrapper provideWindowManagerShellWrapper(ShellExecutor shellExecutor) {
        return (WindowManagerShellWrapper) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideWindowManagerShellWrapper(shellExecutor));
    }
}
