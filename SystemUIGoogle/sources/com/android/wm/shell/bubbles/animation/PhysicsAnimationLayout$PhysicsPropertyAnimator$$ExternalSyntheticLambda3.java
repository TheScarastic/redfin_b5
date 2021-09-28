package com.android.wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.SpringAnimation;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
/* loaded from: classes2.dex */
public final /* synthetic */ class PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsPropertyAnimator f$0;
    public final /* synthetic */ SpringAnimation f$1;
    public final /* synthetic */ SpringAnimation f$2;

    public /* synthetic */ PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda3(PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator, SpringAnimation springAnimation, SpringAnimation springAnimation2) {
        this.f$0 = physicsPropertyAnimator;
        this.f$1 = springAnimation;
        this.f$2 = springAnimation2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$start$1(this.f$1, this.f$2);
    }
}
