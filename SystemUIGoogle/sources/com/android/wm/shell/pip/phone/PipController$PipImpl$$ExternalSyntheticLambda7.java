package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipController;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda7(PipController.PipImpl pipImpl, Runnable runnable, Runnable runnable2) {
        this.f$0 = pipImpl;
        this.f$1 = runnable;
        this.f$2 = runnable2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$hidePipMenu$0(this.f$1, this.f$2);
    }
}
