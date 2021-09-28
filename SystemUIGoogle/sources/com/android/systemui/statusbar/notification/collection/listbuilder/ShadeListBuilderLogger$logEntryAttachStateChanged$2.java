package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logEntryAttachStateChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logEntryAttachStateChanged$2 INSTANCE = new ShadeListBuilderLogger$logEntryAttachStateChanged$2();

    ShadeListBuilderLogger$logEntryAttachStateChanged$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        String str;
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        if (logMessage.getStr2() == null && logMessage.getStr3() != null) {
            str = "ATTACHED";
        } else if (logMessage.getStr2() == null || logMessage.getStr3() != null) {
            str = (logMessage.getStr2() == null && logMessage.getStr3() == null) ? "MODIFIED (DETACHED)" : "MODIFIED (ATTACHED)";
        } else {
            str = "DETACHED";
        }
        return "(Build " + logMessage.getInt1() + ") " + str + " {" + ((Object) logMessage.getStr1()) + '}';
    }
}
