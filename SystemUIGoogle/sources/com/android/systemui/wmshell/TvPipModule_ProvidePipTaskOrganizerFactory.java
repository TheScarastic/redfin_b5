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
import com.android.wm.shell.pip.tv.TvPipMenuController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvPipModule_ProvidePipTaskOrganizerFactory implements Factory<PipTaskOrganizer> {
    private final Provider<Context> contextProvider;
    private final Provider<DisplayController> displayControllerProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<PipAnimationController> pipAnimationControllerProvider;
    private final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;
    private final Provider<PipTransitionController> pipTransitionControllerProvider;
    private final Provider<PipUiEventLogger> pipUiEventLoggerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Optional<LegacySplitScreenController>> splitScreenOptionalProvider;
    private final Provider<SyncTransactionQueue> syncTransactionQueueProvider;
    private final Provider<TvPipMenuController> tvPipMenuControllerProvider;

    public TvPipModule_ProvidePipTaskOrganizerFactory(Provider<Context> provider, Provider<TvPipMenuController> provider2, Provider<SyncTransactionQueue> provider3, Provider<PipBoundsState> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PipAnimationController> provider6, Provider<PipTransitionController> provider7, Provider<PipSurfaceTransactionHelper> provider8, Provider<Optional<LegacySplitScreenController>> provider9, Provider<DisplayController> provider10, Provider<PipUiEventLogger> provider11, Provider<ShellTaskOrganizer> provider12, Provider<ShellExecutor> provider13) {
        this.contextProvider = provider;
        this.tvPipMenuControllerProvider = provider2;
        this.syncTransactionQueueProvider = provider3;
        this.pipBoundsStateProvider = provider4;
        this.pipBoundsAlgorithmProvider = provider5;
        this.pipAnimationControllerProvider = provider6;
        this.pipTransitionControllerProvider = provider7;
        this.pipSurfaceTransactionHelperProvider = provider8;
        this.splitScreenOptionalProvider = provider9;
        this.displayControllerProvider = provider10;
        this.pipUiEventLoggerProvider = provider11;
        this.shellTaskOrganizerProvider = provider12;
        this.mainExecutorProvider = provider13;
    }

    @Override // javax.inject.Provider
    public PipTaskOrganizer get() {
        return providePipTaskOrganizer(this.contextProvider.get(), this.tvPipMenuControllerProvider.get(), this.syncTransactionQueueProvider.get(), this.pipBoundsStateProvider.get(), this.pipBoundsAlgorithmProvider.get(), this.pipAnimationControllerProvider.get(), this.pipTransitionControllerProvider.get(), this.pipSurfaceTransactionHelperProvider.get(), this.splitScreenOptionalProvider.get(), this.displayControllerProvider.get(), this.pipUiEventLoggerProvider.get(), this.shellTaskOrganizerProvider.get(), this.mainExecutorProvider.get());
    }

    public static TvPipModule_ProvidePipTaskOrganizerFactory create(Provider<Context> provider, Provider<TvPipMenuController> provider2, Provider<SyncTransactionQueue> provider3, Provider<PipBoundsState> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PipAnimationController> provider6, Provider<PipTransitionController> provider7, Provider<PipSurfaceTransactionHelper> provider8, Provider<Optional<LegacySplitScreenController>> provider9, Provider<DisplayController> provider10, Provider<PipUiEventLogger> provider11, Provider<ShellTaskOrganizer> provider12, Provider<ShellExecutor> provider13) {
        return new TvPipModule_ProvidePipTaskOrganizerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static PipTaskOrganizer providePipTaskOrganizer(Context context, TvPipMenuController tvPipMenuController, SyncTransactionQueue syncTransactionQueue, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipAnimationController pipAnimationController, PipTransitionController pipTransitionController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, Optional<LegacySplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return (PipTaskOrganizer) Preconditions.checkNotNullFromProvides(TvPipModule.providePipTaskOrganizer(context, tvPipMenuController, syncTransactionQueue, pipBoundsState, pipBoundsAlgorithm, pipAnimationController, pipTransitionController, pipSurfaceTransactionHelper, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor));
    }
}
