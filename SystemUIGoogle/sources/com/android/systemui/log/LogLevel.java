package com.android.systemui.log;
/* compiled from: LogLevel.kt */
/* loaded from: classes.dex */
public enum LogLevel {
    VERBOSE(2, "V"),
    DEBUG(3, "D"),
    INFO(4, "I"),
    WARNING(5, "W"),
    ERROR(6, "E"),
    WTF(7, "WTF");
    
    private final int nativeLevel;
    private final String shortString;

    LogLevel(int i, String str) {
        this.nativeLevel = i;
        this.shortString = str;
    }

    public final String getShortString() {
        return this.shortString;
    }
}
