package com.android.systemui.shared.system;

import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import com.android.systemui.shared.system.RemoteTransitionCompat;
/* loaded from: classes.dex */
public final /* synthetic */ class RemoteTransitionCompat$1$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ RemoteTransitionRunner f$0;
    public final /* synthetic */ IBinder f$1;
    public final /* synthetic */ TransitionInfo f$2;
    public final /* synthetic */ SurfaceControl.Transaction f$3;
    public final /* synthetic */ Runnable f$4;

    public /* synthetic */ RemoteTransitionCompat$1$$ExternalSyntheticLambda2(RemoteTransitionRunner remoteTransitionRunner, IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, Runnable runnable) {
        this.f$0 = remoteTransitionRunner;
        this.f$1 = iBinder;
        this.f$2 = transitionInfo;
        this.f$3 = transaction;
        this.f$4 = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        RemoteTransitionCompat.AnonymousClass1.lambda$startAnimation$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
