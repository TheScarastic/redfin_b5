package com.android.systemui;

import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public interface Dumpable {
    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);
}
