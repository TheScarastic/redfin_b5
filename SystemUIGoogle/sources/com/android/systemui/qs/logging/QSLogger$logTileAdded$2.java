package com.android.systemui.qs.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: QSLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class QSLogger$logTileAdded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logTileAdded$2 INSTANCE = new QSLogger$logTileAdded$2();

    QSLogger$logTileAdded$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return '[' + ((Object) logMessage.getStr1()) + "] Tile added";
    }
}
