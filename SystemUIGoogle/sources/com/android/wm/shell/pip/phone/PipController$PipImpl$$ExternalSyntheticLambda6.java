package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipController;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ PrintWriter f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda6(PipController.PipImpl pipImpl, PrintWriter printWriter) {
        this.f$0 = pipImpl;
        this.f$1 = printWriter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$dump$11(this.f$1);
    }
}
