package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logSectionChangeSuppressed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logSectionChangeSuppressed$2 INSTANCE = new ShadeListBuilderLogger$logSectionChangeSuppressed$2();

    ShadeListBuilderLogger$logSectionChangeSuppressed$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "(Build " + logMessage.getLong1() + ")     Suppressing section change to " + ((Object) logMessage.getStr1()) + " (staying at " + ((Object) logMessage.getStr2()) + ')';
    }
}
