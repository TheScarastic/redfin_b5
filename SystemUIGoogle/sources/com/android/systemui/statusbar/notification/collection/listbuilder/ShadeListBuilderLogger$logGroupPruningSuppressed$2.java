package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logGroupPruningSuppressed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logGroupPruningSuppressed$2 INSTANCE = new ShadeListBuilderLogger$logGroupPruningSuppressed$2();

    ShadeListBuilderLogger$logGroupPruningSuppressed$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "(Build " + logMessage.getLong1() + ")     Group pruning suppressed; keeping parent '" + ((Object) logMessage.getStr1()) + '\'';
    }
}
