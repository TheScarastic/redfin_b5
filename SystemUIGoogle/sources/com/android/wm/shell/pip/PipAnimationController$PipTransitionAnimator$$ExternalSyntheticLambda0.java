package com.android.wm.shell.pip;

import android.view.SurfaceControl;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipAnimationController$PipTransitionAnimator$$ExternalSyntheticLambda0 implements PipSurfaceTransactionHelper.SurfaceControlTransactionFactory {
    public static final /* synthetic */ PipAnimationController$PipTransitionAnimator$$ExternalSyntheticLambda0 INSTANCE = new PipAnimationController$PipTransitionAnimator$$ExternalSyntheticLambda0();

    private /* synthetic */ PipAnimationController$PipTransitionAnimator$$ExternalSyntheticLambda0() {
    }

    @Override // com.android.wm.shell.pip.PipSurfaceTransactionHelper.SurfaceControlTransactionFactory
    public final SurfaceControl.Transaction getTransaction() {
        return new SurfaceControl.Transaction();
    }
}
