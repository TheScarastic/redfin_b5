package com.android.wm.shell.transition;

import android.os.IBinder;
import android.window.TransitionRequestInfo;
import com.android.wm.shell.transition.Transitions;
/* loaded from: classes2.dex */
public final /* synthetic */ class Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Transitions.TransitionPlayerImpl f$0;
    public final /* synthetic */ IBinder f$1;
    public final /* synthetic */ TransitionRequestInfo f$2;

    public /* synthetic */ Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda1(Transitions.TransitionPlayerImpl transitionPlayerImpl, IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        this.f$0 = transitionPlayerImpl;
        this.f$1 = iBinder;
        this.f$2 = transitionRequestInfo;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$requestStartTransition$1(this.f$1, this.f$2);
    }
}
