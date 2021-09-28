package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class PrivacyLogger$logShowDialogContents$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logShowDialogContents$2 INSTANCE = new PrivacyLogger$logShowDialogContents$2();

    PrivacyLogger$logShowDialogContents$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return Intrinsics.stringPlus("Privacy dialog shown. Contents: ", logMessage.getStr1());
    }
}
