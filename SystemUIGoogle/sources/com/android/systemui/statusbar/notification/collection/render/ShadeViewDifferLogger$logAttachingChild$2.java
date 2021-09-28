package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeViewDifferLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeViewDifferLogger$logAttachingChild$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logAttachingChild$2 INSTANCE = new ShadeViewDifferLogger$logAttachingChild$2();

    ShadeViewDifferLogger$logAttachingChild$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "Attaching view " + ((Object) logMessage.getStr1()) + " to " + ((Object) logMessage.getStr2());
    }
}
