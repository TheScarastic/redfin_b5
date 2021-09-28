package com.android.wm.shell.pip;

import android.content.pm.ParceledListSlice;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
/* loaded from: classes2.dex */
public final /* synthetic */ class PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PinnedStackListenerForwarder.PinnedTaskListenerImpl f$0;
    public final /* synthetic */ ParceledListSlice f$1;

    public /* synthetic */ PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda2(PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl, ParceledListSlice parceledListSlice) {
        this.f$0 = pinnedTaskListenerImpl;
        this.f$1 = parceledListSlice;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onActionsChanged$2(this.f$1);
    }
}
