package com.android.systemui.log;

import kotlin.jvm.functions.Function1;
/* compiled from: LogMessage.kt */
/* loaded from: classes.dex */
public interface LogMessage {
    boolean getBool1();

    boolean getBool2();

    boolean getBool3();

    boolean getBool4();

    int getInt1();

    int getInt2();

    LogLevel getLevel();

    long getLong1();

    long getLong2();

    Function1<LogMessage, String> getPrinter();

    String getStr1();

    String getStr2();

    String getStr3();

    String getTag();

    long getTimestamp();

    void setBool1(boolean z);

    void setBool2(boolean z);

    void setBool3(boolean z);

    void setBool4(boolean z);

    void setInt1(int i);

    void setInt2(int i);

    void setLong1(long j);

    void setLong2(long j);

    void setStr1(String str);

    void setStr2(String str);

    void setStr3(String str);
}
