package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationSectionsLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotificationSectionsLogger$logPosition$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationSectionsLogger$logPosition$2 INSTANCE = new NotificationSectionsLogger$logPosition$2();

    NotificationSectionsLogger$logPosition$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return logMessage.getInt1() + ": " + ((Object) logMessage.getStr1()) + ((Object) logMessage.getStr2());
    }
}
