package com.android.systemui.wmshell;

import android.view.IWindowManager;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvideDisplayImeControllerFactory implements Factory<DisplayImeController> {
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<TransactionPool> transactionPoolProvider;
    private final Provider<IWindowManager> wmServiceProvider;

    public WMShellModule_ProvideDisplayImeControllerFactory(Provider<IWindowManager> provider, Provider<DisplayController> provider2, Provider<ShellExecutor> provider3, Provider<TransactionPool> provider4) {
        this.wmServiceProvider = provider;
        this.displayControllerProvider = provider2;
        this.mainExecutorProvider = provider3;
        this.transactionPoolProvider = provider4;
    }

    @Override // javax.inject.Provider
    public DisplayImeController get() {
        return provideDisplayImeController(this.wmServiceProvider.get(), this.displayControllerProvider.get(), this.mainExecutorProvider.get(), this.transactionPoolProvider.get());
    }

    public static WMShellModule_ProvideDisplayImeControllerFactory create(Provider<IWindowManager> provider, Provider<DisplayController> provider2, Provider<ShellExecutor> provider3, Provider<TransactionPool> provider4) {
        return new WMShellModule_ProvideDisplayImeControllerFactory(provider, provider2, provider3, provider4);
    }

    public static DisplayImeController provideDisplayImeController(IWindowManager iWindowManager, DisplayController displayController, ShellExecutor shellExecutor, TransactionPool transactionPool) {
        return (DisplayImeController) Preconditions.checkNotNullFromProvides(WMShellModule.provideDisplayImeController(iWindowManager, displayController, shellExecutor, transactionPool));
    }
}
