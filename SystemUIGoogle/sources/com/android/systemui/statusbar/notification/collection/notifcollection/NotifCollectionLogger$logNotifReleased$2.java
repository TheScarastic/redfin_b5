package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifCollectionLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotifCollectionLogger$logNotifReleased$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logNotifReleased$2 INSTANCE = new NotifCollectionLogger$logNotifReleased$2();

    NotifCollectionLogger$logNotifReleased$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("RELEASED ", logMessage.getStr1());
    }
}
