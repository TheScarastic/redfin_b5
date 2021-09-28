package com.android.wm.shell.pip;

import com.android.wm.shell.pip.PipAnimationController;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ PipAnimationController.PipTransitionAnimator f$0;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda4(PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
        this.f$0 = pipTransitionAnimator;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.clearContentOverlay();
    }
}
