package com.android.systemui.dump;

import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DumpHandler.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DumpHandler$parseArgs$1 extends Lambda implements Function1<String, String> {
    public static final DumpHandler$parseArgs$1 INSTANCE = new DumpHandler$parseArgs$1();

    DumpHandler$parseArgs$1() {
        super(1);
    }

    public final String invoke(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        if (ArraysKt___ArraysKt.contains(DumpHandlerKt.PRIORITY_OPTIONS, str)) {
            return str;
        }
        throw new IllegalArgumentException();
    }
}
