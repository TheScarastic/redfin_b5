package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.AnimationHandler;
/* loaded from: classes.dex */
public final /* synthetic */ class AnimationHandler$FrameCallbackScheduler14$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AnimationHandler.FrameCallbackScheduler14 f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ AnimationHandler$FrameCallbackScheduler14$$ExternalSyntheticLambda0(AnimationHandler.FrameCallbackScheduler14 frameCallbackScheduler14, Runnable runnable) {
        this.f$0 = frameCallbackScheduler14;
        this.f$1 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AnimationHandler.FrameCallbackScheduler14.$r8$lambda$igM0FoKbXBn0Emy1e_JJLMAMsj8(this.f$0, this.f$1);
    }
}
