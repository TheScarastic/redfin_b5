package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ShadeListBuilderLogger.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger$logDuplicateTopLevelKey$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logDuplicateTopLevelKey$2 INSTANCE = new ShadeListBuilderLogger$logDuplicateTopLevelKey$2();

    ShadeListBuilderLogger$logDuplicateTopLevelKey$2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "(Build " + logMessage.getInt1() + ") Duplicate top-level key: " + ((Object) logMessage.getStr1());
    }
}
