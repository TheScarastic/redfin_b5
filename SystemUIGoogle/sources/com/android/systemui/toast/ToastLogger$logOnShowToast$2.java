package com.android.systemui.toast;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ToastLogger.kt */
/* loaded from: classes2.dex */
final class ToastLogger$logOnShowToast$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ToastLogger$logOnShowToast$2 INSTANCE = new ToastLogger$logOnShowToast$2();

    ToastLogger$logOnShowToast$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return '[' + ((Object) logMessage.getStr3()) + "] Show toast for (" + ((Object) logMessage.getStr1()) + ", " + logMessage.getInt1() + "). msg='" + ((Object) logMessage.getStr2()) + '\'';
    }
}
