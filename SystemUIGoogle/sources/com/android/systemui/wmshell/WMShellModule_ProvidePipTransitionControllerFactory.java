package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvidePipTransitionControllerFactory implements Factory<PipTransitionController> {
    private final Provider<Context> contextProvider;
    private final Provider<PipAnimationController> pipAnimationControllerProvider;
    private final Provider<PipBoundsAlgorithm> pipBoundsAlgorithmProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PhonePipMenuController> pipMenuControllerProvider;
    private final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    private final Provider<Transitions> transitionsProvider;

    public WMShellModule_ProvidePipTransitionControllerFactory(Provider<Context> provider, Provider<Transitions> provider2, Provider<ShellTaskOrganizer> provider3, Provider<PipAnimationController> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PipBoundsState> provider6, Provider<PhonePipMenuController> provider7) {
        this.contextProvider = provider;
        this.transitionsProvider = provider2;
        this.shellTaskOrganizerProvider = provider3;
        this.pipAnimationControllerProvider = provider4;
        this.pipBoundsAlgorithmProvider = provider5;
        this.pipBoundsStateProvider = provider6;
        this.pipMenuControllerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public PipTransitionController get() {
        return providePipTransitionController(this.contextProvider.get(), this.transitionsProvider.get(), this.shellTaskOrganizerProvider.get(), this.pipAnimationControllerProvider.get(), this.pipBoundsAlgorithmProvider.get(), this.pipBoundsStateProvider.get(), this.pipMenuControllerProvider.get());
    }

    public static WMShellModule_ProvidePipTransitionControllerFactory create(Provider<Context> provider, Provider<Transitions> provider2, Provider<ShellTaskOrganizer> provider3, Provider<PipAnimationController> provider4, Provider<PipBoundsAlgorithm> provider5, Provider<PipBoundsState> provider6, Provider<PhonePipMenuController> provider7) {
        return new WMShellModule_ProvidePipTransitionControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static PipTransitionController providePipTransitionController(Context context, Transitions transitions, ShellTaskOrganizer shellTaskOrganizer, PipAnimationController pipAnimationController, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PhonePipMenuController phonePipMenuController) {
        return (PipTransitionController) Preconditions.checkNotNullFromProvides(WMShellModule.providePipTransitionController(context, transitions, shellTaskOrganizer, pipAnimationController, pipBoundsAlgorithm, pipBoundsState, phonePipMenuController));
    }
}
