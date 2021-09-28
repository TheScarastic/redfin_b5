package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class PrivacyLogger$logPrivacyItemsToHold$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logPrivacyItemsToHold$2 INSTANCE = new PrivacyLogger$logPrivacyItemsToHold$2();

    PrivacyLogger$logPrivacyItemsToHold$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Holding items: ", logMessage.getStr1());
    }
}
