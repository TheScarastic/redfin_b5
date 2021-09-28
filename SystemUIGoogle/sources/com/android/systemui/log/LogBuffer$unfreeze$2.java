package com.android.systemui.log;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: LogBuffer.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class LogBuffer$unfreeze$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LogBuffer$unfreeze$2 INSTANCE = new LogBuffer$unfreeze$2();

    LogBuffer$unfreeze$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus(logMessage.getStr1(), " unfrozen");
    }
}
