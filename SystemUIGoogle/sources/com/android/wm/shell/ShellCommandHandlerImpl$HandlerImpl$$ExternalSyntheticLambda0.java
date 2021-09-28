package com.android.wm.shell;

import com.android.wm.shell.ShellCommandHandlerImpl;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public final /* synthetic */ class ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ShellCommandHandlerImpl.HandlerImpl f$0;
    public final /* synthetic */ PrintWriter f$1;

    public /* synthetic */ ShellCommandHandlerImpl$HandlerImpl$$ExternalSyntheticLambda0(ShellCommandHandlerImpl.HandlerImpl handlerImpl, PrintWriter printWriter) {
        this.f$0 = handlerImpl;
        this.f$1 = printWriter;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$dump$0(this.f$1);
    }
}
