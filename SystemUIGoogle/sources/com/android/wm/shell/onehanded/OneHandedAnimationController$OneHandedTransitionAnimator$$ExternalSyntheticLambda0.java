package com.android.wm.shell.onehanded;

import android.view.SurfaceControl;
import com.android.wm.shell.onehanded.OneHandedSurfaceTransactionHelper;
/* loaded from: classes2.dex */
public final /* synthetic */ class OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda0 implements OneHandedSurfaceTransactionHelper.SurfaceControlTransactionFactory {
    public static final /* synthetic */ OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda0 INSTANCE = new OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda0();

    private /* synthetic */ OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda0() {
    }

    @Override // com.android.wm.shell.onehanded.OneHandedSurfaceTransactionHelper.SurfaceControlTransactionFactory
    public final SurfaceControl.Transaction getTransaction() {
        return new SurfaceControl.Transaction();
    }
}
