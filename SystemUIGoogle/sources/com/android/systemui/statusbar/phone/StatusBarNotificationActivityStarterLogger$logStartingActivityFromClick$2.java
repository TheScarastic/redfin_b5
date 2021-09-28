package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logStartingActivityFromClick$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logStartingActivityFromClick$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logStartingActivityFromClick$2();

    StatusBarNotificationActivityStarterLogger$logStartingActivityFromClick$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("(1/4) onNotificationClicked: ", logMessage.getStr1());
    }
}
