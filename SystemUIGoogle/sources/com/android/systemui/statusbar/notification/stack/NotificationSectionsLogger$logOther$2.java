package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationSectionsLogger.kt */
/* loaded from: classes.dex */
final class NotificationSectionsLogger$logOther$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationSectionsLogger$logOther$2 INSTANCE = new NotificationSectionsLogger$logOther$2();

    NotificationSectionsLogger$logOther$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return logMessage.getInt1() + ": other (" + ((Object) logMessage.getStr1()) + ')';
    }
}
