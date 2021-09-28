package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifBindPipelineLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotifBindPipelineLogger$logStageSet$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logStageSet$2 INSTANCE = new NotifBindPipelineLogger$logStageSet$2();

    NotifBindPipelineLogger$logStageSet$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Stage set: ", logMessage.getStr1());
    }
}
