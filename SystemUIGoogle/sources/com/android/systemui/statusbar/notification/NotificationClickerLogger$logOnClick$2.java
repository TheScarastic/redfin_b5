package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationClickerLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotificationClickerLogger$logOnClick$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationClickerLogger$logOnClick$2 INSTANCE = new NotificationClickerLogger$logOnClick$2();

    NotificationClickerLogger$logOnClick$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "CLICK " + ((Object) logMessage.getStr1()) + " (channel=" + ((Object) logMessage.getStr2()) + ')';
    }
}
