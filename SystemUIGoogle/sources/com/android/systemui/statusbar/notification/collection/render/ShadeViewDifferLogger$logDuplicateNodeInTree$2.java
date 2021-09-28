package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeViewDifferLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeViewDifferLogger$logDuplicateNodeInTree$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logDuplicateNodeInTree$2 INSTANCE = new ShadeViewDifferLogger$logDuplicateNodeInTree$2();

    ShadeViewDifferLogger$logDuplicateNodeInTree$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return ((Object) logMessage.getStr1()) + " when mapping tree: " + ((Object) logMessage.getStr2());
    }
}
