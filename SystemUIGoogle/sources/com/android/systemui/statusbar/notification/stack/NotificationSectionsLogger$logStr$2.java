package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationSectionsLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotificationSectionsLogger$logStr$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationSectionsLogger$logStr$2 INSTANCE = new NotificationSectionsLogger$logStr$2();

    NotificationSectionsLogger$logStr$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return String.valueOf(logMessage.getStr1());
    }
}
