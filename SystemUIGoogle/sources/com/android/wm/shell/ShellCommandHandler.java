package com.android.wm.shell;

import java.io.PrintWriter;
/* loaded from: classes2.dex */
public interface ShellCommandHandler {
    void dump(PrintWriter printWriter);

    boolean handleCommand(String[] strArr, PrintWriter printWriter);
}
