package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DozeLogger$logPulseStart$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logPulseStart$2 INSTANCE = new DozeLogger$logPulseStart$2();

    DozeLogger$logPulseStart$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Pulse start, reason=", DozeLog.reasonToString(logMessage.getInt1()));
    }
}
