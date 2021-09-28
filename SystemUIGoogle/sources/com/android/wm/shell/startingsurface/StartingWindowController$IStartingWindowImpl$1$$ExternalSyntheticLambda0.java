package com.android.wm.shell.startingsurface;

import com.android.wm.shell.startingsurface.StartingWindowController;
/* loaded from: classes2.dex */
public final /* synthetic */ class StartingWindowController$IStartingWindowImpl$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StartingWindowController.IStartingWindowImpl.AnonymousClass1 f$0;
    public final /* synthetic */ StartingWindowController f$1;

    public /* synthetic */ StartingWindowController$IStartingWindowImpl$1$$ExternalSyntheticLambda0(StartingWindowController.IStartingWindowImpl.AnonymousClass1 r1, StartingWindowController startingWindowController) {
        this.f$0 = r1;
        this.f$1 = startingWindowController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$binderDied$0(this.f$1);
    }
}
