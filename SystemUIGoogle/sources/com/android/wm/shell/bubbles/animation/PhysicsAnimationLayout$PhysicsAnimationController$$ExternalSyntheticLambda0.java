package com.android.wm.shell.bubbles.animation;

import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import java.util.List;
import java.util.Set;
/* loaded from: classes2.dex */
public final /* synthetic */ class PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0 implements PhysicsAnimationLayout.PhysicsAnimationController.MultiAnimationStarter {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsAnimationController f$0;
    public final /* synthetic */ Set f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0(PhysicsAnimationLayout.PhysicsAnimationController physicsAnimationController, Set set, List list) {
        this.f$0 = physicsAnimationController;
        this.f$1 = set;
        this.f$2 = list;
    }

    @Override // com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout.PhysicsAnimationController.MultiAnimationStarter
    public final void startAll(Runnable[] runnableArr) {
        PhysicsAnimationLayout.PhysicsAnimationController.m504$r8$lambda$ATd6gfbvs3DBQNz6JpiSu_FJk(this.f$0, this.f$1, this.f$2, runnableArr);
    }
}
