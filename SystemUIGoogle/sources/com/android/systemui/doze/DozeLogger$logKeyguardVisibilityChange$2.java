package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DozeLogger$logKeyguardVisibilityChange$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logKeyguardVisibilityChange$2 INSTANCE = new DozeLogger$logKeyguardVisibilityChange$2();

    DozeLogger$logKeyguardVisibilityChange$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Keyguard visibility change, isShowing=", Boolean.valueOf(logMessage.getBool1()));
    }
}
