package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationEntryManagerLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger$logNotifRemoved$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logNotifRemoved$2 INSTANCE = new NotificationEntryManagerLogger$logNotifRemoved$2();

    NotificationEntryManagerLogger$logNotifRemoved$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "NOTIF REMOVED " + ((Object) logMessage.getStr1()) + " removedByUser=" + logMessage.getBool1();
    }
}
