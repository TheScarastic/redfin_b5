package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyLogger$logStartVoiceReply$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logStartVoiceReply$2 INSTANCE = new NotificationVoiceReplyLogger$logStartVoiceReply$2();

    NotificationVoiceReplyLogger$logStartVoiceReply$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "BINDER: startVoiceReply [userId=" + logMessage.getInt1() + ", sessionToken=" + logMessage.getInt2() + ", hasContent=" + logMessage.getBool1() + ']';
    }
}
