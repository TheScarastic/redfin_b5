package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyLogger$logReinflationDropped$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationVoiceReplyLogger$logReinflationDropped$2 INSTANCE = new NotificationVoiceReplyLogger$logReinflationDropped$2();

    NotificationVoiceReplyLogger$logReinflationDropped$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "dropping STATE UPDATE: Reinflation - key=" + ((Object) logMessage.getStr1()) + ", reason=" + ((Object) logMessage.getStr2());
    }
}
