package com.android.wm.shell.splitscreen;

import com.android.wm.shell.splitscreen.SplitScreenController;
/* loaded from: classes2.dex */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SplitScreenController.ISplitScreenImpl.AnonymousClass2 f$0;
    public final /* synthetic */ SplitScreenController f$1;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$2$$ExternalSyntheticLambda0(SplitScreenController.ISplitScreenImpl.AnonymousClass2 r1, SplitScreenController splitScreenController) {
        this.f$0 = r1;
        this.f$1 = splitScreenController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$binderDied$0(this.f$1);
    }
}
