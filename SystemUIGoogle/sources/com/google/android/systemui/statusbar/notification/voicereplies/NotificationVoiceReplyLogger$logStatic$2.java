package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyLogger$logStatic$2 extends Lambda implements Function1<LogMessage, String> {
    final /* synthetic */ String $s;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyLogger$logStatic$2(String str) {
        super(1);
        this.$s = str;
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return this.$s;
    }
}
