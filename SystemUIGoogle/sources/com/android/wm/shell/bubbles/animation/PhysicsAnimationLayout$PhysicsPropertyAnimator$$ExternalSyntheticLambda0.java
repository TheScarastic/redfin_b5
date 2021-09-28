package com.android.wm.shell.bubbles.animation;

import android.animation.ValueAnimator;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
/* loaded from: classes2.dex */
public final /* synthetic */ class PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda0(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        PhysicsAnimationLayout.PhysicsPropertyAnimator.lambda$startPathAnimation$4(this.f$0, valueAnimator);
    }
}
