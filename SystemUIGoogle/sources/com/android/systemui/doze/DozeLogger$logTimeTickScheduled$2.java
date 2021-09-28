package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import java.util.Date;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DozeLogger$logTimeTickScheduled$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logTimeTickScheduled$2 INSTANCE = new DozeLogger$logTimeTickScheduled$2();

    DozeLogger$logTimeTickScheduled$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Time tick scheduledAt=" + ((Object) DozeLoggerKt.getDATE_FORMAT().format(new Date(logMessage.getLong1()))) + " triggerAt=" + ((Object) DozeLoggerKt.getDATE_FORMAT().format(new Date(logMessage.getLong2())));
    }
}
