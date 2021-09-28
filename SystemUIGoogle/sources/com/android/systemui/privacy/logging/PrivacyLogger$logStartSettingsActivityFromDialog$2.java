package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class PrivacyLogger$logStartSettingsActivityFromDialog$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logStartSettingsActivityFromDialog$2 INSTANCE = new PrivacyLogger$logStartSettingsActivityFromDialog$2();

    PrivacyLogger$logStartSettingsActivityFromDialog$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Start settings activity from dialog for packageName=" + ((Object) logMessage.getStr1()) + ", userId=" + logMessage.getInt1() + ' ';
    }
}
