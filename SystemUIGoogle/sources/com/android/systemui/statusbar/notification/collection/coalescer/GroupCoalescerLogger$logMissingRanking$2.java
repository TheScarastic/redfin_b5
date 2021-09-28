package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: GroupCoalescerLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class GroupCoalescerLogger$logMissingRanking$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logMissingRanking$2 INSTANCE = new GroupCoalescerLogger$logMissingRanking$2();

    GroupCoalescerLogger$logMissingRanking$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("RankingMap is missing an entry for coalesced notification ", logMessage.getStr1());
    }
}
