package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: GroupCoalescerLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class GroupCoalescerLogger$logMaxBatchTimeout$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logMaxBatchTimeout$2 INSTANCE = new GroupCoalescerLogger$logMaxBatchTimeout$2();

    GroupCoalescerLogger$logMaxBatchTimeout$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Modification of notif " + ((Object) logMessage.getStr1()) + " triggered TIMEOUT emit of batched group " + ((Object) logMessage.getStr2());
    }
}
