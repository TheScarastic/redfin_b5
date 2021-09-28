package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifBindPipelineLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotifBindPipelineLogger$logRequestPipelineRun$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logRequestPipelineRun$2 INSTANCE = new NotifBindPipelineLogger$logRequestPipelineRun$2();

    NotifBindPipelineLogger$logRequestPipelineRun$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Request pipeline run for notif: ", logMessage.getStr1());
    }
}
