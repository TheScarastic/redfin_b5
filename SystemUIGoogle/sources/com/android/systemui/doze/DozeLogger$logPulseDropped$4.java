package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DozeLogger$logPulseDropped$4 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logPulseDropped$4 INSTANCE = new DozeLogger$logPulseDropped$4();

    DozeLogger$logPulseDropped$4() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Pulse dropped, why=", logMessage.getStr1());
    }
}
