package com.android.systemui.log;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: LogcatEchoTrackerProd.kt */
/* loaded from: classes.dex */
public final class LogcatEchoTrackerProd implements LogcatEchoTracker {
    @Override // com.android.systemui.log.LogcatEchoTracker
    public boolean isBufferLoggable(String str, LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(str, "bufferName");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        return logLevel.compareTo(LogLevel.WARNING) >= 0;
    }

    @Override // com.android.systemui.log.LogcatEchoTracker
    public boolean isTagLoggable(String str, LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(str, "tagName");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        return logLevel.compareTo(LogLevel.WARNING) >= 0;
    }
}
