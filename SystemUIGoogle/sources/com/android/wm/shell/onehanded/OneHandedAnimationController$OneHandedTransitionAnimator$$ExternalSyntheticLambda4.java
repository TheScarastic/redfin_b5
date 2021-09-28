package com.android.wm.shell.onehanded;

import android.view.SurfaceControl;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ OneHandedAnimationController.OneHandedTransitionAnimator f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;

    public /* synthetic */ OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda4(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator, SurfaceControl.Transaction transaction) {
        this.f$0 = oneHandedTransitionAnimator;
        this.f$1 = transaction;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onAnimationEnd$1(this.f$1, (OneHandedAnimationCallback) obj);
    }
}
