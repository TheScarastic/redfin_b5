package com.android.systemui.dump;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DumpHandler.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DumpHandler$parseArgs$2 extends Lambda implements Function1<String, Integer> {
    public static final DumpHandler$parseArgs$2 INSTANCE = new DumpHandler$parseArgs$2();

    DumpHandler$parseArgs$2() {
        super(1);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Integer invoke(String str) {
        return Integer.valueOf(invoke(str));
    }

    public final int invoke(String str) {
        Intrinsics.checkNotNullParameter(str, "it");
        return Integer.parseInt(str);
    }
}
