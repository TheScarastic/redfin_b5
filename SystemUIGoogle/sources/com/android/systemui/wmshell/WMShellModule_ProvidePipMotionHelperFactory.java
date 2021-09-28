package com.android.systemui.wmshell;

import android.content.Context;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipMotionHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellModule_ProvidePipMotionHelperFactory implements Factory<PipMotionHelper> {
    private final Provider<Context> contextProvider;
    private final Provider<FloatingContentCoordinator> floatingContentCoordinatorProvider;
    private final Provider<PhonePipMenuController> menuControllerProvider;
    private final Provider<PipBoundsState> pipBoundsStateProvider;
    private final Provider<PipSnapAlgorithm> pipSnapAlgorithmProvider;
    private final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;
    private final Provider<PipTransitionController> pipTransitionControllerProvider;

    public WMShellModule_ProvidePipMotionHelperFactory(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipTaskOrganizer> provider3, Provider<PhonePipMenuController> provider4, Provider<PipSnapAlgorithm> provider5, Provider<PipTransitionController> provider6, Provider<FloatingContentCoordinator> provider7) {
        this.contextProvider = provider;
        this.pipBoundsStateProvider = provider2;
        this.pipTaskOrganizerProvider = provider3;
        this.menuControllerProvider = provider4;
        this.pipSnapAlgorithmProvider = provider5;
        this.pipTransitionControllerProvider = provider6;
        this.floatingContentCoordinatorProvider = provider7;
    }

    @Override // javax.inject.Provider
    public PipMotionHelper get() {
        return providePipMotionHelper(this.contextProvider.get(), this.pipBoundsStateProvider.get(), this.pipTaskOrganizerProvider.get(), this.menuControllerProvider.get(), this.pipSnapAlgorithmProvider.get(), this.pipTransitionControllerProvider.get(), this.floatingContentCoordinatorProvider.get());
    }

    public static WMShellModule_ProvidePipMotionHelperFactory create(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipTaskOrganizer> provider3, Provider<PhonePipMenuController> provider4, Provider<PipSnapAlgorithm> provider5, Provider<PipTransitionController> provider6, Provider<FloatingContentCoordinator> provider7) {
        return new WMShellModule_ProvidePipMotionHelperFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static PipMotionHelper providePipMotionHelper(Context context, PipBoundsState pipBoundsState, PipTaskOrganizer pipTaskOrganizer, PhonePipMenuController phonePipMenuController, PipSnapAlgorithm pipSnapAlgorithm, PipTransitionController pipTransitionController, FloatingContentCoordinator floatingContentCoordinator) {
        return (PipMotionHelper) Preconditions.checkNotNullFromProvides(WMShellModule.providePipMotionHelper(context, pipBoundsState, pipTaskOrganizer, phonePipMenuController, pipSnapAlgorithm, pipTransitionController, floatingContentCoordinator));
    }
}
