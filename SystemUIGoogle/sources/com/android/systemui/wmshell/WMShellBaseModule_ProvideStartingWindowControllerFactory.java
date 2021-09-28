package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideStartingWindowControllerFactory implements Factory<StartingWindowController> {
    private final Provider<Context> contextProvider;
    private final Provider<TransactionPool> poolProvider;
    private final Provider<ShellExecutor> splashScreenExecutorProvider;
    private final Provider<StartingWindowTypeAlgorithm> startingWindowTypeAlgorithmProvider;

    public WMShellBaseModule_ProvideStartingWindowControllerFactory(Provider<Context> provider, Provider<ShellExecutor> provider2, Provider<StartingWindowTypeAlgorithm> provider3, Provider<TransactionPool> provider4) {
        this.contextProvider = provider;
        this.splashScreenExecutorProvider = provider2;
        this.startingWindowTypeAlgorithmProvider = provider3;
        this.poolProvider = provider4;
    }

    @Override // javax.inject.Provider
    public StartingWindowController get() {
        return provideStartingWindowController(this.contextProvider.get(), this.splashScreenExecutorProvider.get(), this.startingWindowTypeAlgorithmProvider.get(), this.poolProvider.get());
    }

    public static WMShellBaseModule_ProvideStartingWindowControllerFactory create(Provider<Context> provider, Provider<ShellExecutor> provider2, Provider<StartingWindowTypeAlgorithm> provider3, Provider<TransactionPool> provider4) {
        return new WMShellBaseModule_ProvideStartingWindowControllerFactory(provider, provider2, provider3, provider4);
    }

    public static StartingWindowController provideStartingWindowController(Context context, ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, TransactionPool transactionPool) {
        return (StartingWindowController) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideStartingWindowController(context, shellExecutor, startingWindowTypeAlgorithm, transactionPool));
    }
}
