package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logHandleClickAfterKeyguardDismissed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logHandleClickAfterKeyguardDismissed$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logHandleClickAfterKeyguardDismissed$2();

    StatusBarNotificationActivityStarterLogger$logHandleClickAfterKeyguardDismissed$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("(2/4) handleNotificationClickAfterKeyguardDismissed: ", logMessage.getStr1());
    }
}
