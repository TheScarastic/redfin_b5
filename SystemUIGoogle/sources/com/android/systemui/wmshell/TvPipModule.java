package com.android.systemui.wmshell;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.tv.TvPipController;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import com.android.wm.shell.pip.tv.TvPipTransition;
import com.android.wm.shell.transition.Transitions;
import java.util.Optional;
/* loaded from: classes2.dex */
public abstract class TvPipModule {
    /* access modifiers changed from: package-private */
    public static Optional<Pip> providePip(Context context, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipTaskOrganizer pipTaskOrganizer, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, PipTransitionController pipTransitionController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        return Optional.of(TvPipController.create(context, pipBoundsState, pipBoundsAlgorithm, pipTaskOrganizer, pipTransitionController, tvPipMenuController, pipMediaController, tvPipNotificationController, taskStackListenerImpl, windowManagerShellWrapper, shellExecutor));
    }

    /* access modifiers changed from: package-private */
    public static PipSnapAlgorithm providePipSnapAlgorithm() {
        return new PipSnapAlgorithm();
    }

    /* access modifiers changed from: package-private */
    public static PipBoundsAlgorithm providePipBoundsAlgorithm(Context context, PipBoundsState pipBoundsState, PipSnapAlgorithm pipSnapAlgorithm) {
        return new PipBoundsAlgorithm(context, pipBoundsState, pipSnapAlgorithm);
    }

    /* access modifiers changed from: package-private */
    public static PipBoundsState providePipBoundsState(Context context) {
        return new PipBoundsState(context);
    }

    /* access modifiers changed from: package-private */
    public static PipTransitionController provideTvPipTransition(Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipAnimationController pipAnimationController, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, TvPipMenuController tvPipMenuController) {
        return new TvPipTransition(pipBoundsState, tvPipMenuController, pipBoundsAlgorithm, pipAnimationController, transitions, shellTaskOrganizer);
    }

    /* access modifiers changed from: package-private */
    public static TvPipMenuController providesTvPipMenuController(Context context, PipBoundsState pipBoundsState, SystemWindows systemWindows, PipMediaController pipMediaController, Handler handler) {
        return new TvPipMenuController(context, pipBoundsState, systemWindows, pipMediaController, handler);
    }

    /* access modifiers changed from: package-private */
    public static TvPipNotificationController provideTvPipNotificationController(Context context, PipMediaController pipMediaController, Handler handler) {
        return new TvPipNotificationController(context, pipMediaController, handler);
    }

    /* access modifiers changed from: package-private */
    public static PipAnimationController providePipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        return new PipAnimationController(pipSurfaceTransactionHelper);
    }

    /* access modifiers changed from: package-private */
    public static PipTaskOrganizer providePipTaskOrganizer(Context context, TvPipMenuController tvPipMenuController, SyncTransactionQueue syncTransactionQueue, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipAnimationController pipAnimationController, PipTransitionController pipTransitionController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, Optional<LegacySplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return new PipTaskOrganizer(context, syncTransactionQueue, pipBoundsState, pipBoundsAlgorithm, tvPipMenuController, pipAnimationController, pipSurfaceTransactionHelper, pipTransitionController, optional, displayController, pipUiEventLogger, shellTaskOrganizer, shellExecutor);
    }
}
