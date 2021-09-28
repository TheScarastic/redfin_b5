package com.android.systemui.dump;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DumpHandler.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ParsedArgs {
    private String command;
    private String dumpPriority;
    private boolean listOnly;
    private final List<String> nonFlagArgs;
    private final String[] rawArgs;
    private int tailLength;

    public ParsedArgs(String[] strArr, List<String> list) {
        Intrinsics.checkNotNullParameter(strArr, "rawArgs");
        Intrinsics.checkNotNullParameter(list, "nonFlagArgs");
        this.rawArgs = strArr;
        this.nonFlagArgs = list;
    }

    public final String[] getRawArgs() {
        return this.rawArgs;
    }

    public final List<String> getNonFlagArgs() {
        return this.nonFlagArgs;
    }

    public final String getDumpPriority() {
        return this.dumpPriority;
    }

    public final void setDumpPriority(String str) {
        this.dumpPriority = str;
    }

    public final int getTailLength() {
        return this.tailLength;
    }

    public final void setTailLength(int i) {
        this.tailLength = i;
    }

    public final String getCommand() {
        return this.command;
    }

    public final void setCommand(String str) {
        this.command = str;
    }

    public final boolean getListOnly() {
        return this.listOnly;
    }

    public final void setListOnly(boolean z) {
        this.listOnly = z;
    }
}
