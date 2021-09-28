package com.android.systemui.shared.recents.utilities;

import android.os.Trace;
/* loaded from: classes.dex */
public class AppTrace {
    public static void beginSection(String str) {
        Trace.beginSection(str);
    }

    public static void count(String str, int i) {
        Trace.traceCounter(4096, str, i);
    }

    public static void end(String str) {
        Trace.asyncTraceEnd(4096, str, 0);
    }

    public static void endSection() {
        Trace.endSection();
    }

    public static void start(String str, int i) {
        Trace.asyncTraceBegin(4096, str, i);
    }

    public static void end(String str, int i) {
        Trace.asyncTraceEnd(4096, str, i);
    }

    public static void start(String str) {
        Trace.asyncTraceBegin(4096, str, 0);
    }
}
