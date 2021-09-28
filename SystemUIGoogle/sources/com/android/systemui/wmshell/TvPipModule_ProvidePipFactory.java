package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.pip.tv.TvPipNotificationController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvPipModule_ProvidePipFactory implements Factory<Optional<Pip>> {
    private final Provider<Context> contextProvider;
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipMediaController> pipMediaControllerProvider;
    private final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;
    private final Provider<PipTransitionController> pipTransitionControllerProvider;
    private final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    private final Provider<TvPipMenuController> tvPipMenuControllerProvider;
    private final Provider<TvPipNotificationController> tvPipNotificationControllerProvider;
    private final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public TvPipModule_ProvidePipFactory(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipBoundsAlgorithm> provider3, Provider<PipTaskOrganizer> provider4, Provider<TvPipMenuController> provider5, Provider<PipMediaController> provider6, Provider<PipTransitionController> provider7, Provider<TvPipNotificationController> provider8, Provider<TaskStackListenerImpl> provider9, Provider<WindowManagerShellWrapper> provider10, Provider<ShellExecutor> provider11) {
        this.contextProvider = provider;
        this.pipBoundsStateProvider = provider2;
        this.pipBoundsAlgorithmProvider = provider3;
        this.pipTaskOrganizerProvider = provider4;
        this.tvPipMenuControllerProvider = provider5;
        this.pipMediaControllerProvider = provider6;
        this.pipTransitionControllerProvider = provider7;
        this.tvPipNotificationControllerProvider = provider8;
        this.taskStackListenerProvider = provider9;
        this.windowManagerShellWrapperProvider = provider10;
        this.mainExecutorProvider = provider11;
    }

    @Override // javax.inject.Provider
    public Optional<Pip> get() {
        return providePip(this.contextProvider.get(), this.pipBoundsStateProvider.get(), this.pipBoundsAlgorithmProvider.get(), this.pipTaskOrganizerProvider.get(), this.tvPipMenuControllerProvider.get(), this.pipMediaControllerProvider.get(), this.pipTransitionControllerProvider.get(), this.tvPipNotificationControllerProvider.get(), this.taskStackListenerProvider.get(), this.windowManagerShellWrapperProvider.get(), this.mainExecutorProvider.get());
    }

    public static TvPipModule_ProvidePipFactory create(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipBoundsAlgorithm> provider3, Provider<PipTaskOrganizer> provider4, Provider<TvPipMenuController> provider5, Provider<PipMediaController> provider6, Provider<PipTransitionController> provider7, Provider<TvPipNotificationController> provider8, Provider<TaskStackListenerImpl> provider9, Provider<WindowManagerShellWrapper> provider10, Provider<ShellExecutor> provider11) {
        return new TvPipModule_ProvidePipFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static Optional<Pip> providePip(Context context, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipTaskOrganizer pipTaskOrganizer, TvPipMenuController tvPipMenuController, PipMediaController pipMediaController, PipTransitionController pipTransitionController, TvPipNotificationController tvPipNotificationController, TaskStackListenerImpl taskStackListenerImpl, WindowManagerShellWrapper windowManagerShellWrapper, ShellExecutor shellExecutor) {
        return (Optional) Preconditions.checkNotNullFromProvides(TvPipModule.providePip(context, pipBoundsState, pipBoundsAlgorithm, pipTaskOrganizer, tvPipMenuController, pipMediaController, pipTransitionController, tvPipNotificationController, taskStackListenerImpl, windowManagerShellWrapper, shellExecutor));
    }
}
