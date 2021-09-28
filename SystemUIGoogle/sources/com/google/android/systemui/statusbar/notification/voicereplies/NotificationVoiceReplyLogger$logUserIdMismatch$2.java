package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyLogger$logUserIdMismatch$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logUserIdMismatch$2 INSTANCE = new NotificationVoiceReplyLogger$logUserIdMismatch$2();

    NotificationVoiceReplyLogger$logUserIdMismatch$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "UserId mismatch, notifying handler of failure [handerId=" + logMessage.getInt1() + ", candidateId=" + logMessage.getInt2() + ']';
    }
}
