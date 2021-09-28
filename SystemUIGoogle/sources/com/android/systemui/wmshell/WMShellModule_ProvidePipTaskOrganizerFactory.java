package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvidePipTaskOrganizerFactory implements Factory<PipTaskOrganizer> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<PhonePipMenuController> menuPhoneControllerProvider;
    private final Provider<PipAnimationController> pipAnimationControllerProvider;
    private final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;
    private final Provider<PipTransitionController> pipTransitionControllerProvider;
    private final Provider<PipUiEventLogger> pipUiEventLoggerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Optional<LegacySplitScreenController>> splitScreenOptionalProvider;
    private final Provider<SyncTransactionQueue> syncTransactionQueueProvider;

    public WMShellModule_ProvidePipTaskOrganizerFactory(Provider<Context> provider, Provider<SyncTransactionQueue> provider2, Provider<PipBoundsState> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PhonePipMenuController> provider5, Provider<PipAnimationController> provider6, Provider<PipSurfaceTransactionHelper> provider7, Provider<PipTransitionController> provider8, Provider<Optional<LegacySplitScreenController>> provider9, Provider<DisplayController> provider10, Provider<PipUiEventLogger> provider11, Provider<ShellTaskOrganizer> provider12, Provider<ShellExecutor> provider13) {
        this.contextProvider = provider;
        this.syncTransactionQueueProvider = provider2;
        this.pipBoundsStateProvider = provider3;
        this.pipBoundsAlgorithmProvider = provider4;
        this.menuPhoneControllerProvider = provider5;
        this.pipAnimationControllerProvider = provider6;
        this.pipSurfaceTransactionHelperProvider = provider7;
        this.pipTransitionControllerProvider = provider8;
        this.splitScreenOptionalProvider = provider9;
        this.displayControllerProvider = provider10;
        this.pipUiEventLoggerProvider = provider11;
        this.shellTaskOrganizerProvider = provider12;
        this.mainExecutorProvider = provider13;
    }

    @Override // javax.inject.Provider
    public PipTaskOrganizer get() {
        return providePipTaskOrganizer(this.contextProvider.get(), this.syncTransactionQueueProvider.get(), this.pipBoundsStateProvider.get(), this.pipBoundsAlgorithmProvider.get(), this.menuPhoneControllerProvider.get(), this.pipAnimationControllerProvider.get(), this.pipSurfaceTransactionHelperProvider.get(), this.pipTransitionControllerProvider.get(), this.splitScreenOptionalProvider.get(), this.displayControllerProvider.get(), this.pipUiEventLoggerProvider.get(), this.shellTaskOrganizerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellModule_ProvidePipTaskOrganizerFactory create(Provider<Context> provider, Provider<SyncTransactionQueue> provider2, Provider<PipBoundsState> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PhonePipMenuController> provider5, Provider<PipAnimationController> provider6, Provider<PipSurfaceTransactionHelper> provider7, Provider<PipTransitionController> provider8, Provider<Optional<LegacySplitScreenController>> provider9, Provider<DisplayController> provider10, Provider<PipUiEventLogger> provider11, Provider<ShellTaskOrganizer> provider12, Provider<ShellExecutor> provider13) {
        return new WMShellModule_ProvidePipTaskOrganizerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static PipTaskOrganizer providePipTaskOrganizer(Context context, SyncTransactionQueue syncTransactionQueue, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PhonePipMenuController phonePipMenuController, PipAnimationController pipAnimationController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, PipTransitionController pipTransitionController, Optional<LegacySplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return (PipTaskOrganizer) Preconditions.checkNotNullFromProvides(WMShellModule.providePipTaskOrganizer(context, syncTransactionQueue, pipBoundsState, pipBoundsAlgorithm, phonePipMenuController, pipAnimationController, pipSurfaceTransactionHelper, pipTransitionController, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor));
    }
}
