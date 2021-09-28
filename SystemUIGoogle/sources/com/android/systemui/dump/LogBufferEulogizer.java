package com.android.systemui.dump;

import android.util.Log;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.time.SystemClock;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LogBufferEulogizer.kt */
/* loaded from: classes.dex */
public final class LogBufferEulogizer {
    private final DumpManager dumpManager;
    private final Files files;
    private final Path logPath;
    private final long maxLogAgeToDump;
    private final long minWriteGap;
    private final SystemClock systemClock;

    public LogBufferEulogizer(DumpManager dumpManager, SystemClock systemClock, Files files, Path path, long j, long j2) {
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(files, "files");
        Intrinsics.checkNotNullParameter(path, "logPath");
        this.dumpManager = dumpManager;
        this.systemClock = systemClock;
        this.files = files;
        this.logPath = path;
        this.minWriteGap = j;
        this.maxLogAgeToDump = j2;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public LogBufferEulogizer(android.content.Context r11, com.android.systemui.dump.DumpManager r12, com.android.systemui.util.time.SystemClock r13, com.android.systemui.util.io.Files r14) {
        /*
            r10 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.lang.String r0 = "dumpManager"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            java.lang.String r0 = "systemClock"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            java.lang.String r0 = "files"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            java.io.File r11 = r11.getFilesDir()
            java.nio.file.Path r11 = r11.toPath()
            java.lang.String r11 = r11.toString()
            java.lang.String r0 = "log_buffers.txt"
            java.lang.String[] r0 = new java.lang.String[]{r0}
            java.nio.file.Path r5 = java.nio.file.Paths.get(r11, r0)
            java.lang.String r11 = "get(context.filesDir.toPath().toString(), \"log_buffers.txt\")"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r11)
            long r6 = com.android.systemui.dump.LogBufferEulogizerKt.access$getMIN_WRITE_GAP$p()
            long r8 = com.android.systemui.dump.LogBufferEulogizerKt.access$getMAX_AGE_TO_DUMP$p()
            r1 = r10
            r2 = r12
            r3 = r13
            r4 = r14
            r1.<init>(r2, r3, r4, r5, r6, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.LogBufferEulogizer.<init>(android.content.Context, com.android.systemui.dump.DumpManager, com.android.systemui.util.time.SystemClock, com.android.systemui.util.io.Files):void");
    }

    public final <T extends Exception> T record(T t) {
        Intrinsics.checkNotNullParameter(t, "reason");
        long uptimeMillis = this.systemClock.uptimeMillis();
        Log.i("BufferEulogizer", "Performing emergency dump of log buffers");
        long millisSinceLastWrite = getMillisSinceLastWrite(this.logPath);
        if (millisSinceLastWrite < this.minWriteGap) {
            Log.w("BufferEulogizer", "Cannot dump logs, last write was only " + millisSinceLastWrite + " ms ago");
            return t;
        }
        long j = 0;
        try {
            BufferedWriter newBufferedWriter = this.files.newBufferedWriter(this.logPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            PrintWriter printWriter = new PrintWriter(newBufferedWriter);
            printWriter.println(LogBufferEulogizerKt.access$getDATE_FORMAT$p().format(Long.valueOf(this.systemClock.currentTimeMillis())));
            printWriter.println();
            printWriter.println("Dump triggered by exception:");
            t.printStackTrace(printWriter);
            this.dumpManager.dumpBuffers(printWriter, 0);
            j = this.systemClock.uptimeMillis() - uptimeMillis;
            printWriter.println();
            printWriter.println("Buffer eulogy took " + j + "ms");
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(newBufferedWriter, null);
        } catch (Exception e) {
            Log.e("BufferEulogizer", "Exception while attempting to dump buffers, bailing", e);
        }
        Log.i("BufferEulogizer", "Buffer eulogy took " + j + "ms");
        return t;
    }

    public final void readEulogyIfPresent(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        try {
            long millisSinceLastWrite = getMillisSinceLastWrite(this.logPath);
            if (millisSinceLastWrite > this.maxLogAgeToDump) {
                Log.i("BufferEulogizer", "Not eulogizing buffers; they are " + TimeUnit.HOURS.convert(millisSinceLastWrite, TimeUnit.MILLISECONDS) + " hours old");
                return;
            }
            Stream<String> lines = this.files.lines(this.logPath);
            th = null;
            try {
                printWriter.println();
                printWriter.println();
                printWriter.println("=============== BUFFERS FROM MOST RECENT CRASH ===============");
                lines.forEach(new Consumer<String>(printWriter) { // from class: com.android.systemui.dump.LogBufferEulogizer$readEulogyIfPresent$1$1
                    final /* synthetic */ PrintWriter $pw;

                    /* access modifiers changed from: package-private */
                    {
                        this.$pw = r1;
                    }

                    public final void accept(String str) {
                        this.$pw.println(str);
                    }
                });
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th) {
                try {
                    throw th;
                } finally {
                    AutoCloseableKt.closeFinally(lines, th);
                }
            }
        } catch (IOException unused) {
        } catch (UncheckedIOException e) {
            Log.e("BufferEulogizer", "UncheckedIOException while dumping the core", e);
        }
    }

    private final long getMillisSinceLastWrite(Path path) {
        BasicFileAttributes basicFileAttributes;
        FileTime fileTime = null;
        try {
            basicFileAttributes = this.files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
        } catch (IOException unused) {
            basicFileAttributes = null;
        }
        long currentTimeMillis = this.systemClock.currentTimeMillis();
        if (basicFileAttributes != null) {
            fileTime = basicFileAttributes.lastModifiedTime();
        }
        return currentTimeMillis - (fileTime == null ? 0 : fileTime.toMillis());
    }
}
