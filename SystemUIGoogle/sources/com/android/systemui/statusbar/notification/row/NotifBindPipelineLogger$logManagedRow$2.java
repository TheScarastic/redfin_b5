package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifBindPipelineLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotifBindPipelineLogger$logManagedRow$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logManagedRow$2 INSTANCE = new NotifBindPipelineLogger$logManagedRow$2();

    NotifBindPipelineLogger$logManagedRow$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Row set for notif: ", logMessage.getStr1());
    }
}
