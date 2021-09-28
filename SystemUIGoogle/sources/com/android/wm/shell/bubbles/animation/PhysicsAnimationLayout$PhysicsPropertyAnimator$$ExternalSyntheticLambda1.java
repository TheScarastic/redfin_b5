package com.android.wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
/* loaded from: classes2.dex */
public final /* synthetic */ class PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SpringForce f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;
    public final /* synthetic */ SpringAnimation f$4;
    public final /* synthetic */ float f$5;

    public /* synthetic */ PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1(SpringForce springForce, float f, float f2, float f3, SpringAnimation springAnimation, float f4) {
        this.f$0 = springForce;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = f3;
        this.f$4 = springAnimation;
        this.f$5 = f4;
    }

    @Override // java.lang.Runnable
    public final void run() {
        PhysicsAnimationLayout.PhysicsPropertyAnimator.lambda$animateValueForChild$2(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
