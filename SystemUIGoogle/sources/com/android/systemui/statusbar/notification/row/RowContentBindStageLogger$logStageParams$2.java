package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: RowContentBindStageLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RowContentBindStageLogger$logStageParams$2 extends Lambda implements Function1<LogMessage, String> {
    public static final RowContentBindStageLogger$logStageParams$2 INSTANCE = new RowContentBindStageLogger$logStageParams$2();

    RowContentBindStageLogger$logStageParams$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Invalidated notif " + ((Object) logMessage.getStr1()) + " with params: \n" + ((Object) logMessage.getStr2());
    }
}
