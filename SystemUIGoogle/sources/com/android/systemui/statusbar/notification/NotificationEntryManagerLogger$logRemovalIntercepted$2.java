package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationEntryManagerLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotificationEntryManagerLogger$logRemovalIntercepted$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logRemovalIntercepted$2 INSTANCE = new NotificationEntryManagerLogger$logRemovalIntercepted$2();

    NotificationEntryManagerLogger$logRemovalIntercepted$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("NOTIF REMOVE INTERCEPTED for ", logMessage.getStr1());
    }
}
