package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationEntryManagerLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger$logLifetimeExtended$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logLifetimeExtended$2 INSTANCE = new NotificationEntryManagerLogger$logLifetimeExtended$2();

    NotificationEntryManagerLogger$logLifetimeExtended$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "NOTIF LIFETIME EXTENDED " + ((Object) logMessage.getStr1()) + " extender=" + ((Object) logMessage.getStr2()) + " status=" + ((Object) logMessage.getStr3());
    }
}
