package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logReorderingAllowedInvalidated$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logReorderingAllowedInvalidated$2 INSTANCE = new ShadeListBuilderLogger$logReorderingAllowedInvalidated$2();

    ShadeListBuilderLogger$logReorderingAllowedInvalidated$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "ReorderingNowAllowed \"" + ((Object) logMessage.getStr1()) + "\" invalidated; pipeline state is " + logMessage.getInt1();
    }
}
