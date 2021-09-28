package com.android.systemui.wmshell;

import android.animation.AnimationHandler;
import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvideLegacySplitScreenFactory implements Factory<LegacySplitScreenController> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<DisplayImeController> displayImeControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<AnimationHandler> sfVsyncAnimationHandlerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<SyncTransactionQueue> syncQueueProvider;
    private final Provider<SystemWindows> systemWindowsProvider;
    private final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    private final Provider<TransactionPool> transactionPoolProvider;
    private final Provider<Transitions> transitionsProvider;

    public WMShellModule_ProvideLegacySplitScreenFactory(Provider<Context> provider, Provider<DisplayController> provider2, Provider<SystemWindows> provider3, Provider<DisplayImeController> provider4, Provider<TransactionPool> provider5, Provider<ShellTaskOrganizer> provider6, Provider<SyncTransactionQueue> provider7, Provider<TaskStackListenerImpl> provider8, Provider<Transitions> provider9, Provider<ShellExecutor> provider10, Provider<AnimationHandler> provider11) {
        this.contextProvider = provider;
        this.displayControllerProvider = provider2;
        this.systemWindowsProvider = provider3;
        this.displayImeControllerProvider = provider4;
        this.transactionPoolProvider = provider5;
        this.shellTaskOrganizerProvider = provider6;
        this.syncQueueProvider = provider7;
        this.taskStackListenerProvider = provider8;
        this.transitionsProvider = provider9;
        this.mainExecutorProvider = provider10;
        this.sfVsyncAnimationHandlerProvider = provider11;
    }

    @Override // javax.inject.Provider
    public LegacySplitScreenController get() {
        return provideLegacySplitScreen(this.contextProvider.get(), this.displayControllerProvider.get(), this.systemWindowsProvider.get(), this.displayImeControllerProvider.get(), this.transactionPoolProvider.get(), this.shellTaskOrganizerProvider.get(), this.syncQueueProvider.get(), this.taskStackListenerProvider.get(), this.transitionsProvider.get(), this.mainExecutorProvider.get(), this.sfVsyncAnimationHandlerProvider.get());
    }

    public static WMShellModule_ProvideLegacySplitScreenFactory create(Provider<Context> provider, Provider<DisplayController> provider2, Provider<SystemWindows> provider3, Provider<DisplayImeController> provider4, Provider<TransactionPool> provider5, Provider<ShellTaskOrganizer> provider6, Provider<SyncTransactionQueue> provider7, Provider<TaskStackListenerImpl> provider8, Provider<Transitions> provider9, Provider<ShellExecutor> provider10, Provider<AnimationHandler> provider11) {
        return new WMShellModule_ProvideLegacySplitScreenFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static LegacySplitScreenController provideLegacySplitScreen(Context context, DisplayController displayController, SystemWindows systemWindows, DisplayImeController displayImeController, TransactionPool transactionPool, ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, TaskStackListenerImpl taskStackListenerImpl, Transitions transitions, ShellExecutor shellExecutor, AnimationHandler animationHandler) {
        return (LegacySplitScreenController) Preconditions.checkNotNullFromProvides(WMShellModule.provideLegacySplitScreen(context, displayController, systemWindows, displayImeController, transactionPool, shellTaskOrganizer, syncTransactionQueue, taskStackListenerImpl, transitions, shellExecutor, animationHandler));
    }
}
