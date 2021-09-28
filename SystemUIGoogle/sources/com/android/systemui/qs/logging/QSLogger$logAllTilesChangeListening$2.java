package com.android.systemui.qs.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: QSLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class QSLogger$logAllTilesChangeListening$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logAllTilesChangeListening$2 INSTANCE = new QSLogger$logAllTilesChangeListening$2();

    QSLogger$logAllTilesChangeListening$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Tiles listening=" + logMessage.getBool1() + " in " + ((Object) logMessage.getStr1()) + ". " + ((Object) logMessage.getStr2());
    }
}
