package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2();

    StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "(4/4) Starting " + ((Object) logMessage.getStr2()) + " for notification " + ((Object) logMessage.getStr1());
    }
}
