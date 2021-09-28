package com.android.wm.shell.common;

import android.graphics.Rect;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
/* compiled from: Comparisons.kt */
/* loaded from: classes2.dex */
public final class FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1<T> implements Comparator<T> {
    final /* synthetic */ boolean $findAbove$inlined;

    public FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1(boolean z) {
        this.$findAbove$inlined = z;
    }

    @Override // java.util.Comparator
    public final int compare(T t, T t2) {
        int compareValues;
        boolean z = this.$findAbove$inlined;
        int i = ((Rect) t).top;
        if (z) {
            i = -i;
        }
        Rect rect = (Rect) t2;
        compareValues = ComparisonsKt__ComparisonsKt.compareValues(Integer.valueOf(i), Integer.valueOf(this.$findAbove$inlined ? -rect.top : rect.top));
        return compareValues;
    }
}
