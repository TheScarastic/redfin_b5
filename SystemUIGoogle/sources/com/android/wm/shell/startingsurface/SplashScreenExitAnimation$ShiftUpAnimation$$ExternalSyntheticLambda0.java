package com.android.wm.shell.startingsurface;

import android.view.SurfaceControl;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplashScreenExitAnimation$ShiftUpAnimation$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SurfaceControl f$0;

    public /* synthetic */ SplashScreenExitAnimation$ShiftUpAnimation$$ExternalSyntheticLambda0(SurfaceControl surfaceControl) {
        this.f$0 = surfaceControl;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.release();
    }
}
