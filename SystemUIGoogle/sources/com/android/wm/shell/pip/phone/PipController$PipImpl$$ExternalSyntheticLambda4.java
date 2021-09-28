package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipController;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda4(PipController.PipImpl pipImpl, int i) {
        this.f$0 = pipImpl;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setPinnedStackAnimationType$8(this.f$1);
    }
}
