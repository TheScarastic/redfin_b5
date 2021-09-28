package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipController;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipController$IPipImpl$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PipController.IPipImpl.AnonymousClass1 f$0;
    public final /* synthetic */ PipController f$1;

    public /* synthetic */ PipController$IPipImpl$1$$ExternalSyntheticLambda0(PipController.IPipImpl.AnonymousClass1 r1, PipController pipController) {
        this.f$0 = r1;
        this.f$1 = pipController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$binderDied$0(this.f$1);
    }
}
