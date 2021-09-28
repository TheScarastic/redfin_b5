package com.android.wm.shell;

import com.android.wm.shell.ShellCommandHandlerImpl;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public final /* synthetic */ class ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ShellCommandHandlerImpl.HandlerImpl f$0;
    public final /* synthetic */ boolean[] f$1;
    public final /* synthetic */ String[] f$2;
    public final /* synthetic */ PrintWriter f$3;

    public /* synthetic */ ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda1(ShellCommandHandlerImpl.HandlerImpl handlerImpl, boolean[] zArr, String[] strArr, PrintWriter printWriter) {
        this.f$0 = handlerImpl;
        this.f$1 = zArr;
        this.f$2 = strArr;
        this.f$3 = printWriter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$handleCommand$1(this.f$1, this.f$2, this.f$3);
    }
}
