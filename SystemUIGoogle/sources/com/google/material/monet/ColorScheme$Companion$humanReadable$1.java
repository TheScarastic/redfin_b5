package com.google.material.monet;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ColorScheme.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class ColorScheme$Companion$humanReadable$1 extends Lambda implements Function1<Integer, CharSequence> {
    public static final ColorScheme$Companion$humanReadable$1 INSTANCE = new ColorScheme$Companion$humanReadable$1();

    ColorScheme$Companion$humanReadable$1() {
        super(1);
    }

    public final CharSequence invoke(int i) {
        return Intrinsics.stringPlus("#", Integer.toHexString(i));
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ CharSequence invoke(Integer num) {
        return invoke(num.intValue());
    }
}
