package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logDozeSuppressed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logDozeSuppressed$2 INSTANCE = new DozeLogger$logDozeSuppressed$2();

    DozeLogger$logDozeSuppressed$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Doze state suppressed, state=", logMessage.getStr1());
    }
}
