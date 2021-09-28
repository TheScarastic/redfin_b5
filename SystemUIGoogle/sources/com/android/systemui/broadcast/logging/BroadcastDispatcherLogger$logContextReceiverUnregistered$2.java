package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
final class BroadcastDispatcherLogger$logContextReceiverUnregistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logContextReceiverUnregistered$2 INSTANCE = new BroadcastDispatcherLogger$logContextReceiverUnregistered$2();

    BroadcastDispatcherLogger$logContextReceiverUnregistered$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Receiver unregistered with Context for user " + logMessage.getInt1() + ", action " + ((Object) logMessage.getStr1());
    }
}
