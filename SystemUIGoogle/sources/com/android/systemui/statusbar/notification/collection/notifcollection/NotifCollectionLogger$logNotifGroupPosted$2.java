package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifCollectionLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NotifCollectionLogger$logNotifGroupPosted$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logNotifGroupPosted$2 INSTANCE = new NotifCollectionLogger$logNotifGroupPosted$2();

    NotifCollectionLogger$logNotifGroupPosted$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "POSTED GROUP " + ((Object) logMessage.getStr1()) + " (" + logMessage.getInt1() + " events)";
    }
}
