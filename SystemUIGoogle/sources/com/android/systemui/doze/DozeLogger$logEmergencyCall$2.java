package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logEmergencyCall$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logEmergencyCall$2 INSTANCE = new DozeLogger$logEmergencyCall$2();

    DozeLogger$logEmergencyCall$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Emergency call";
    }
}
