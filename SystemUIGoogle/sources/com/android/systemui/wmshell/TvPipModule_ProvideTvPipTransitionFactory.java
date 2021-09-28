package com.android.systemui.wmshell;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TvPipModule_ProvideTvPipTransitionFactory implements Factory<PipTransitionController> {
    private final Provider<PipAnimationController> pipAnimationControllerProvider;
    private final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<TvPipMenuController> pipMenuControllerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Transitions> transitionsProvider;

    public TvPipModule_ProvideTvPipTransitionFactory(Provider<Transitions> provider, Provider<ShellTaskOrganizer> provider2, Provider<PipAnimationController> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PipBoundsState> provider5, Provider<TvPipMenuController> provider6) {
        this.transitionsProvider = provider;
        this.shellTaskOrganizerProvider = provider2;
        this.pipAnimationControllerProvider = provider3;
        this.pipBoundsAlgorithmProvider = provider4;
        this.pipBoundsStateProvider = provider5;
        this.pipMenuControllerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public PipTransitionController get() {
        return provideTvPipTransition(this.transitionsProvider.get(), this.shellTaskOrganizerProvider.get(), this.pipAnimationControllerProvider.get(), this.pipBoundsAlgorithmProvider.get(), this.pipBoundsStateProvider.get(), this.pipMenuControllerProvider.get());
    }

    public static TvPipModule_ProvideTvPipTransitionFactory create(Provider<Transitions> provider, Provider<ShellTaskOrganizer> provider2, Provider<PipAnimationController> provider3, Provider<PipBoundsAlgorithm> provider4, Provider<PipBoundsState> provider5, Provider<TvPipMenuController> provider6) {
        return new TvPipModule_ProvideTvPipTransitionFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static PipTransitionController provideTvPipTransition(Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipAnimationController pipAnimationController, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, TvPipMenuController tvPipMenuController) {
        return (PipTransitionController) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipTransition(transitions, shellTaskOrganizer, pipAnimationController, pipBoundsAlgorithm, pipBoundsState, tvPipMenuController));
    }
}
