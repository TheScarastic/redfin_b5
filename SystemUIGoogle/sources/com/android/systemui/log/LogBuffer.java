package com.android.systemui.log;

import android.util.Log;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LogBuffer.kt */
/* loaded from: classes.dex */
public final class LogBuffer {
    private final ArrayDeque<LogMessageImpl> buffer = new ArrayDeque<>();
    private boolean frozen;
    private final LogcatEchoTracker logcatEchoTracker;
    private final int maxLogs;
    private final String name;
    private final int poolSize;

    /* compiled from: LogBuffer.kt */
    /* loaded from: classes.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[LogLevel.values().length];
            iArr[LogLevel.VERBOSE.ordinal()] = 1;
            iArr[LogLevel.DEBUG.ordinal()] = 2;
            iArr[LogLevel.INFO.ordinal()] = 3;
            iArr[LogLevel.WARNING.ordinal()] = 4;
            iArr[LogLevel.ERROR.ordinal()] = 5;
            iArr[LogLevel.WTF.ordinal()] = 6;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public LogBuffer(String str, int i, int i2, LogcatEchoTracker logcatEchoTracker) {
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(logcatEchoTracker, "logcatEchoTracker");
        this.name = str;
        this.maxLogs = i;
        this.poolSize = i2;
        this.logcatEchoTracker = logcatEchoTracker;
    }

    public final boolean getFrozen() {
        return this.frozen;
    }

    public final synchronized LogMessageImpl obtain(String str, LogLevel logLevel, Function1<? super LogMessage, String> function1) {
        LogMessageImpl logMessageImpl;
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        Intrinsics.checkNotNullParameter(function1, "printer");
        if (this.frozen) {
            logMessageImpl = LogMessageImpl.Factory.create();
        } else if (this.buffer.size() > this.maxLogs - this.poolSize) {
            logMessageImpl = this.buffer.removeFirst();
        } else {
            logMessageImpl = LogMessageImpl.Factory.create();
        }
        logMessageImpl.reset(str, logLevel, System.currentTimeMillis(), function1);
        Intrinsics.checkNotNullExpressionValue(logMessageImpl, "message");
        return logMessageImpl;
    }

    public final synchronized void push(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "message");
        if (!this.frozen) {
            if (this.buffer.size() == this.maxLogs) {
                Log.e("LogBuffer", "LogBuffer " + this.name + " has exceeded its pool size");
                this.buffer.removeFirst();
            }
            this.buffer.add((LogMessageImpl) logMessage);
            if (this.logcatEchoTracker.isBufferLoggable(this.name, ((LogMessageImpl) logMessage).getLevel()) || this.logcatEchoTracker.isTagLoggable(((LogMessageImpl) logMessage).getTag(), ((LogMessageImpl) logMessage).getLevel())) {
                echoToLogcat(logMessage);
            }
        }
    }

    public final synchronized void dump(PrintWriter printWriter, int i) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        int i2 = 0;
        int size = i <= 0 ? 0 : this.buffer.size() - i;
        Iterator<LogMessageImpl> it = this.buffer.iterator();
        while (it.hasNext()) {
            int i3 = i2 + 1;
            LogMessageImpl next = it.next();
            if (i2 >= size) {
                Intrinsics.checkNotNullExpressionValue(next, "message");
                dumpMessage(next, printWriter);
            }
            i2 = i3;
        }
    }

    public final synchronized void freeze() {
        if (!this.frozen) {
            LogLevel logLevel = LogLevel.DEBUG;
            LogBuffer$freeze$2 logBuffer$freeze$2 = LogBuffer$freeze$2.INSTANCE;
            if (!getFrozen()) {
                LogMessageImpl obtain = obtain("LogBuffer", logLevel, logBuffer$freeze$2);
                obtain.setStr1(this.name);
                push(obtain);
            }
            this.frozen = true;
        }
    }

    public final synchronized void unfreeze() {
        if (this.frozen) {
            LogLevel logLevel = LogLevel.DEBUG;
            LogBuffer$unfreeze$2 logBuffer$unfreeze$2 = LogBuffer$unfreeze$2.INSTANCE;
            if (!getFrozen()) {
                LogMessageImpl obtain = obtain("LogBuffer", logLevel, logBuffer$unfreeze$2);
                obtain.setStr1(this.name);
                push(obtain);
            }
            this.frozen = false;
        }
    }

    private final void dumpMessage(LogMessage logMessage, PrintWriter printWriter) {
        printWriter.print(LogBufferKt.access$getDATE_FORMAT$p().format(Long.valueOf(logMessage.getTimestamp())));
        printWriter.print(" ");
        printWriter.print(logMessage.getLevel().getShortString());
        printWriter.print(" ");
        printWriter.print(logMessage.getTag());
        printWriter.print(": ");
        printWriter.println(logMessage.getPrinter().invoke(logMessage));
    }

    private final void echoToLogcat(LogMessage logMessage) {
        String invoke = logMessage.getPrinter().invoke(logMessage);
        switch (WhenMappings.$EnumSwitchMapping$0[logMessage.getLevel().ordinal()]) {
            case 1:
                Log.v(logMessage.getTag(), invoke);
                return;
            case 2:
                Log.d(logMessage.getTag(), invoke);
                return;
            case 3:
                Log.i(logMessage.getTag(), invoke);
                return;
            case 4:
                Log.w(logMessage.getTag(), invoke);
                return;
            case 5:
                Log.e(logMessage.getTag(), invoke);
                return;
            case 6:
                Log.wtf(logMessage.getTag(), invoke);
                return;
            default:
                return;
        }
    }
}
