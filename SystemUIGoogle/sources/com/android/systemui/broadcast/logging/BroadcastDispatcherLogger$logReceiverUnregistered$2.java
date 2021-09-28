package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
final class BroadcastDispatcherLogger$logReceiverUnregistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logReceiverUnregistered$2 INSTANCE = new BroadcastDispatcherLogger$logReceiverUnregistered$2();

    BroadcastDispatcherLogger$logReceiverUnregistered$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Receiver " + ((Object) logMessage.getStr1()) + " unregistered for user " + logMessage.getInt1();
    }
}
