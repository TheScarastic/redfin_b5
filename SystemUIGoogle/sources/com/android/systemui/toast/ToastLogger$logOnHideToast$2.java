package com.android.systemui.toast;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ToastLogger.kt */
/* loaded from: classes2.dex */
final class ToastLogger$logOnHideToast$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ToastLogger$logOnHideToast$2 INSTANCE = new ToastLogger$logOnHideToast$2();

    ToastLogger$logOnHideToast$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return '[' + ((Object) logMessage.getStr2()) + "] Hide toast for [" + ((Object) logMessage.getStr1()) + ']';
    }
}
