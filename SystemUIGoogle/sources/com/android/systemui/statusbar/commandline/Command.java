package com.android.systemui.statusbar.commandline;

import java.io.PrintWriter;
import java.util.List;
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public interface Command {
    void execute(PrintWriter printWriter, List<String> list);
}
