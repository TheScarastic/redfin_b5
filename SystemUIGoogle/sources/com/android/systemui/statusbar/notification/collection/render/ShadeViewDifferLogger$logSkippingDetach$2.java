package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeViewDifferLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeViewDifferLogger$logSkippingDetach$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logSkippingDetach$2 INSTANCE = new ShadeViewDifferLogger$logSkippingDetach$2();

    ShadeViewDifferLogger$logSkippingDetach$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Skipping detach of " + ((Object) logMessage.getStr1()) + " because its parent " + ((Object) logMessage.getStr2()) + " is also being detached";
    }
}
