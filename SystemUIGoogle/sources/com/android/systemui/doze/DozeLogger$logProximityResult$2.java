package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DozeLogger$logProximityResult$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logProximityResult$2 INSTANCE = new DozeLogger$logProximityResult$2();

    DozeLogger$logProximityResult$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Proximity result reason=" + ((Object) DozeLog.reasonToString(logMessage.getInt1())) + " near=" + logMessage.getBool1() + " millis=" + logMessage.getLong1();
    }
}
