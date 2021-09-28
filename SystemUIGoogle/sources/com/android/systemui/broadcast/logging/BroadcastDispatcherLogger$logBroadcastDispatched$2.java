package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
final class BroadcastDispatcherLogger$logBroadcastDispatched$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logBroadcastDispatched$2 INSTANCE = new BroadcastDispatcherLogger$logBroadcastDispatched$2();

    BroadcastDispatcherLogger$logBroadcastDispatched$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Broadcast " + logMessage.getInt1() + " (" + ((Object) logMessage.getStr1()) + ") dispatched to " + ((Object) logMessage.getStr2());
    }
}
