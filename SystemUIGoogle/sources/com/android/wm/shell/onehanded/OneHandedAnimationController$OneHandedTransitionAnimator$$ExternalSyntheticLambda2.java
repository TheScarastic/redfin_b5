package com.android.wm.shell.onehanded;

import com.android.wm.shell.onehanded.OneHandedAnimationController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ OneHandedAnimationController.OneHandedTransitionAnimator f$0;

    public /* synthetic */ OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda2(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
        this.f$0 = oneHandedTransitionAnimator;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onAnimationCancel$2((OneHandedAnimationCallback) obj);
    }
}
