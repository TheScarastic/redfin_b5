package com.android.systemui.dump;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: DumpHandler.kt */
/* loaded from: classes.dex */
public final class ArgParseException extends Exception {
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ArgParseException(String str) {
        super(str);
        Intrinsics.checkNotNullParameter(str, "message");
    }
}
