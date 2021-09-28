package com.android.systemui.util;

import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequenceBuilderKt;
/* compiled from: ConvenienceExtensions.kt */
/* loaded from: classes2.dex */
public final class ConvenienceExtensionsKt {
    public static final Sequence<View> getChildren(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<this>");
        return SequencesKt__SequenceBuilderKt.sequence(new ConvenienceExtensionsKt$children$1(viewGroup, null));
    }

    public static final <T> Sequence<T> takeUntil(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        Intrinsics.checkNotNullParameter(function1, "pred");
        return SequencesKt__SequenceBuilderKt.sequence(new ConvenienceExtensionsKt$takeUntil$1(sequence, function1, null));
    }
}
