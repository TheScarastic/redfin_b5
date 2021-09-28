package com.android.wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
/* loaded from: classes2.dex */
public final /* synthetic */ class PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsAnimationController f$0;
    public final /* synthetic */ DynamicAnimation.ViewProperty[] f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda1(PhysicsAnimationLayout.PhysicsAnimationController physicsAnimationController, DynamicAnimation.ViewProperty[] viewPropertyArr, Runnable runnable) {
        this.f$0 = physicsAnimationController;
        this.f$1 = viewPropertyArr;
        this.f$2 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setEndActionForMultipleProperties$2(this.f$1, this.f$2);
    }
}
