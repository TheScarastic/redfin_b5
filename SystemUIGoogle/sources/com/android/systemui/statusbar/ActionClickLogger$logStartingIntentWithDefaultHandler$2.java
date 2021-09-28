package com.android.systemui.statusbar;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ActionClickLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ActionClickLogger$logStartingIntentWithDefaultHandler$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ActionClickLogger$logStartingIntentWithDefaultHandler$2 INSTANCE = new ActionClickLogger$logStartingIntentWithDefaultHandler$2();

    ActionClickLogger$logStartingIntentWithDefaultHandler$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "  [Action click] Launching intent " + ((Object) logMessage.getStr2()) + " via default handler (for " + ((Object) logMessage.getStr1()) + ')';
    }
}
