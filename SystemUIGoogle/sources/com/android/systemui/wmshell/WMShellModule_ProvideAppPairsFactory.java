package com.android.systemui.wmshell;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvideAppPairsFactory implements Factory<AppPairsController> {
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayImeController> displayImeControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<SyncTransactionQueue> syncQueueProvider;

    public WMShellModule_ProvideAppPairsFactory(Provider<ShellTaskOrganizer> provider, Provider<SyncTransactionQueue> provider2, Provider<DisplayController> provider3, Provider<ShellExecutor> provider4, Provider<DisplayImeController> provider5) {
        this.shellTaskOrganizerProvider = provider;
        this.syncQueueProvider = provider2;
        this.displayControllerProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.displayImeControllerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public AppPairsController get() {
        return provideAppPairs(this.shellTaskOrganizerProvider.get(), this.syncQueueProvider.get(), this.displayControllerProvider.get(), this.mainExecutorProvider.get(), this.displayImeControllerProvider.get());
    }

    public static WMShellModule_ProvideAppPairsFactory create(Provider<ShellTaskOrganizer> provider, Provider<SyncTransactionQueue> provider2, Provider<DisplayController> provider3, Provider<ShellExecutor> provider4, Provider<DisplayImeController> provider5) {
        return new WMShellModule_ProvideAppPairsFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static AppPairsController provideAppPairs(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, ShellExecutor shellExecutor, DisplayImeController displayImeController) {
        return (AppPairsController) Preconditions.checkNotNullFromProvides(WMShellModule.provideAppPairs(shellTaskOrganizer, syncTransactionQueue, displayController, shellExecutor, displayImeController));
    }
}
