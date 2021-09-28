package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class PrivacyLogger$logUpdatedItemFromAppOps$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logUpdatedItemFromAppOps$2 INSTANCE = new PrivacyLogger$logUpdatedItemFromAppOps$2();

    PrivacyLogger$logUpdatedItemFromAppOps$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "App Op: " + logMessage.getInt1() + " for " + ((Object) logMessage.getStr1()) + '(' + logMessage.getInt2() + "), active=" + logMessage.getBool1();
    }
}
