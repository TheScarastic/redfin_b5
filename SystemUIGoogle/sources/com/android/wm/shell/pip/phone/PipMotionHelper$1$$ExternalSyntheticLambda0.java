package com.android.wm.shell.pip.phone;

import android.view.Choreographer;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipMotionHelper$1$$ExternalSyntheticLambda0 implements Choreographer.FrameCallback {
    public final /* synthetic */ Runnable f$0;

    public /* synthetic */ PipMotionHelper$1$$ExternalSyntheticLambda0(Runnable runnable) {
        this.f$0 = runnable;
    }

    @Override // android.view.Choreographer.FrameCallback
    public final void doFrame(long j) {
        this.f$0.run();
    }
}
