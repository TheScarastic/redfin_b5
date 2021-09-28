package com.android.wm.shell.transition;

import android.window.IRemoteTransition;
import com.android.wm.shell.transition.Transitions;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ IRemoteTransition f$0;

    public /* synthetic */ Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda0(IRemoteTransition iRemoteTransition) {
        this.f$0 = iRemoteTransition;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        Transitions.IShellTransitionsImpl.lambda$unregisterRemote$1(this.f$0, (Transitions) obj);
    }
}
