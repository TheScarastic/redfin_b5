package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
/* loaded from: classes.dex */
final class StatusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2();

    StatusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("No Fullscreen intent: not important enough: ", logMessage.getStr1());
    }
}
