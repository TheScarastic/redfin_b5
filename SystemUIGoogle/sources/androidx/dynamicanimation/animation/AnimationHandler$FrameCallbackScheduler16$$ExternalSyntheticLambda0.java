package androidx.dynamicanimation.animation;

import android.view.Choreographer;
import androidx.dynamicanimation.animation.AnimationHandler;
/* loaded from: classes.dex */
public final /* synthetic */ class AnimationHandler$FrameCallbackScheduler16$$ExternalSyntheticLambda0 implements Choreographer.FrameCallback {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ AnimationHandler$FrameCallbackScheduler16$$ExternalSyntheticLambda0(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // android.view.Choreographer.FrameCallback
    public final void doFrame(long j) {
        AnimationHandler.FrameCallbackScheduler16.$r8$lambda$K1fsod5wSogyfAjedC12ENrpgmA(this.f$0, j);
    }
}
