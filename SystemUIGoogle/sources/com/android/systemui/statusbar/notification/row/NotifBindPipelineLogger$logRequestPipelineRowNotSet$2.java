package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifBindPipelineLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotifBindPipelineLogger$logRequestPipelineRowNotSet$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logRequestPipelineRowNotSet$2 INSTANCE = new NotifBindPipelineLogger$logRequestPipelineRowNotSet$2();

    NotifBindPipelineLogger$logRequestPipelineRowNotSet$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Row is not set so pipeline will not run. notif = ", logMessage.getStr1());
    }
}
