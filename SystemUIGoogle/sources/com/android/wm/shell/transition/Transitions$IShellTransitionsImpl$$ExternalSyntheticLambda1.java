package com.android.wm.shell.transition;

import android.window.IRemoteTransition;
import android.window.TransitionFilter;
import com.android.wm.shell.transition.Transitions;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ TransitionFilter f$0;
    public final /* synthetic */ IRemoteTransition f$1;

    public /* synthetic */ Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1(TransitionFilter transitionFilter, IRemoteTransition iRemoteTransition) {
        this.f$0 = transitionFilter;
        this.f$1 = iRemoteTransition;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        Transitions.IShellTransitionsImpl.lambda$registerRemote$0(this.f$0, this.f$1, (Transitions) obj);
    }
}
