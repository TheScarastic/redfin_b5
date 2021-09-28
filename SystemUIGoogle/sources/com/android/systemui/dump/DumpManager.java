package com.android.systemui.dump;

import android.util.ArrayMap;
import com.android.systemui.Dumpable;
import com.android.systemui.log.LogBuffer;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
/* compiled from: DumpManager.kt */
/* loaded from: classes.dex */
public final class DumpManager {
    private final Map<String, RegisteredDumpable<Dumpable>> dumpables = new ArrayMap();
    private final Map<String, RegisteredDumpable<LogBuffer>> buffers = new ArrayMap();

    public final synchronized void registerDumpable(String str, Dumpable dumpable) {
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(dumpable, "module");
        if (canAssignToNameLocked(str, dumpable)) {
            this.dumpables.put(str, new RegisteredDumpable<>(str, dumpable));
        } else {
            throw new IllegalArgumentException('\'' + str + "' is already registered");
        }
    }

    public final synchronized void unregisterDumpable(String str) {
        Intrinsics.checkNotNullParameter(str, "name");
        this.dumpables.remove(str);
    }

    public final synchronized void registerBuffer(String str, LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        if (canAssignToNameLocked(str, logBuffer)) {
            this.buffers.put(str, new RegisteredDumpable<>(str, logBuffer));
        } else {
            throw new IllegalArgumentException('\'' + str + "' is already registered");
        }
    }

    public final synchronized void dumpTarget(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, int i) {
        Intrinsics.checkNotNullParameter(str, "target");
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        for (RegisteredDumpable<Dumpable> registeredDumpable : this.dumpables.values()) {
            if (StringsKt.endsWith$default(registeredDumpable.getName(), str, false, 2, null)) {
                dumpDumpable(registeredDumpable, fileDescriptor, printWriter, strArr);
                return;
            }
        }
        for (RegisteredDumpable<LogBuffer> registeredDumpable2 : this.buffers.values()) {
            if (StringsKt.endsWith$default(registeredDumpable2.getName(), str, false, 2, null)) {
                dumpBuffer(registeredDumpable2, printWriter, i);
                return;
            }
        }
    }

    public final synchronized void dumpDumpables(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        for (RegisteredDumpable<Dumpable> registeredDumpable : this.dumpables.values()) {
            dumpDumpable(registeredDumpable, fileDescriptor, printWriter, strArr);
        }
    }

    public final synchronized void listDumpables(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        for (RegisteredDumpable<Dumpable> registeredDumpable : this.dumpables.values()) {
            printWriter.println(registeredDumpable.getName());
        }
    }

    public final synchronized void dumpBuffers(PrintWriter printWriter, int i) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        for (RegisteredDumpable<LogBuffer> registeredDumpable : this.buffers.values()) {
            dumpBuffer(registeredDumpable, printWriter, i);
        }
    }

    public final synchronized void listBuffers(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        for (RegisteredDumpable<LogBuffer> registeredDumpable : this.buffers.values()) {
            printWriter.println(registeredDumpable.getName());
        }
    }

    public final synchronized void freezeBuffers() {
        for (RegisteredDumpable<LogBuffer> registeredDumpable : this.buffers.values()) {
            registeredDumpable.getDumpable().freeze();
        }
    }

    public final synchronized void unfreezeBuffers() {
        for (RegisteredDumpable<LogBuffer> registeredDumpable : this.buffers.values()) {
            registeredDumpable.getDumpable().unfreeze();
        }
    }

    private final void dumpDumpable(RegisteredDumpable<Dumpable> registeredDumpable, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println(Intrinsics.stringPlus(registeredDumpable.getName(), ":"));
        printWriter.println("----------------------------------------------------------------------------");
        registeredDumpable.getDumpable().dump(fileDescriptor, printWriter, strArr);
    }

    private final void dumpBuffer(RegisteredDumpable<LogBuffer> registeredDumpable, PrintWriter printWriter, int i) {
        printWriter.println();
        printWriter.println();
        printWriter.println("BUFFER " + registeredDumpable.getName() + ':');
        printWriter.println("============================================================================");
        registeredDumpable.getDumpable().dump(printWriter, i);
    }

    private final boolean canAssignToNameLocked(String str, Object obj) {
        LogBuffer logBuffer;
        RegisteredDumpable<Dumpable> registeredDumpable = this.dumpables.get(str);
        if (registeredDumpable == null) {
            RegisteredDumpable<LogBuffer> registeredDumpable2 = this.buffers.get(str);
            logBuffer = registeredDumpable2 == null ? null : registeredDumpable2.getDumpable();
        } else {
            logBuffer = registeredDumpable.getDumpable();
        }
        return logBuffer == null || Intrinsics.areEqual(obj, logBuffer);
    }
}
