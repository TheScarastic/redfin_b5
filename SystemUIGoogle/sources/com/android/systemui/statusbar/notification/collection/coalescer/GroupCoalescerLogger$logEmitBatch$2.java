package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: GroupCoalescerLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class GroupCoalescerLogger$logEmitBatch$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logEmitBatch$2 INSTANCE = new GroupCoalescerLogger$logEmitBatch$2();

    GroupCoalescerLogger$logEmitBatch$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Emitting event batch for group ", logMessage.getStr1());
    }
}
