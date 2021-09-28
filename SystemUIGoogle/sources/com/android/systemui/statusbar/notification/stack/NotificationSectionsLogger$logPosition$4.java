package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationSectionsLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotificationSectionsLogger$logPosition$4 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationSectionsLogger$logPosition$4 INSTANCE = new NotificationSectionsLogger$logPosition$4();

    NotificationSectionsLogger$logPosition$4() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return logMessage.getInt1() + ": " + ((Object) logMessage.getStr1());
    }
}
