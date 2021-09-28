package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logSectionChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logSectionChanged$2 INSTANCE = new ShadeListBuilderLogger$logSectionChanged$2();

    ShadeListBuilderLogger$logSectionChanged$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        if (logMessage.getStr1() == null) {
            return "(Build " + logMessage.getLong1() + ")     Section assigned: " + ((Object) logMessage.getStr2());
        }
        return "(Build " + logMessage.getLong1() + ")     Section changed: " + ((Object) logMessage.getStr1()) + " -> " + ((Object) logMessage.getStr2());
    }
}
