package com.android.systemui.log;
/* compiled from: LogcatEchoTracker.kt */
/* loaded from: classes.dex */
public interface LogcatEchoTracker {
    boolean isBufferLoggable(String str, LogLevel logLevel);

    boolean isTagLoggable(String str, LogLevel logLevel);
}
