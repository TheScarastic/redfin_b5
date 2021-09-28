package com.android.systemui.wmshell;

import android.content.Context;
import android.view.IWindowManager;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideDisplayControllerFactory implements Factory<DisplayController> {
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<IWindowManager> wmServiceProvider;

    public WMShellBaseModule_ProvideDisplayControllerFactory(Provider<Context> provider, Provider<IWindowManager> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.wmServiceProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    @Override // javax.inject.Provider
    public DisplayController get() {
        return provideDisplayController(this.contextProvider.get(), this.wmServiceProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideDisplayControllerFactory create(Provider<Context> provider, Provider<IWindowManager> provider2, Provider<ShellExecutor> provider3) {
        return new WMShellBaseModule_ProvideDisplayControllerFactory(provider, provider2, provider3);
    }

    public static DisplayController provideDisplayController(Context context, IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        return (DisplayController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDisplayController(context, iWindowManager, shellExecutor));
    }
}
