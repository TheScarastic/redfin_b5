package com.android.wm.shell.pip;

import com.android.wm.shell.pip.PinnedStackListenerForwarder;
/* loaded from: classes2.dex */
public final /* synthetic */ class PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ PinnedStackListenerForwarder.PinnedTaskListenerImpl f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda4(PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl, boolean z, int i) {
        this.f$0 = pinnedTaskListenerImpl;
        this.f$1 = z;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onImeVisibilityChanged$1(this.f$1, this.f$2);
    }
}
