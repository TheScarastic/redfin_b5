package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationEntryManagerLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger$logInflationAborted$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logInflationAborted$2 INSTANCE = new NotificationEntryManagerLogger$logInflationAborted$2();

    NotificationEntryManagerLogger$logInflationAborted$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "NOTIF INFLATION ABORTED " + ((Object) logMessage.getStr1()) + " notifStatus=" + ((Object) logMessage.getStr2()) + " reason=" + ((Object) logMessage.getStr3());
    }
}
