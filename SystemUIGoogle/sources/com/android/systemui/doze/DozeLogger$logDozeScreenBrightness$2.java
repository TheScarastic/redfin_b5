package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logDozeScreenBrightness$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logDozeScreenBrightness$2 INSTANCE = new DozeLogger$logDozeScreenBrightness$2();

    DozeLogger$logDozeScreenBrightness$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Doze screen brightness set, brightness=", Integer.valueOf(logMessage.getInt1()));
    }
}
