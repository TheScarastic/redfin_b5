package com.android.keyguard;

import android.graphics.fonts.FontVariationAxis;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
/* compiled from: Comparisons.kt */
/* loaded from: classes.dex */
public final class FontInterpolator$VarFontKey$set$$inlined$sortBy$1<T> implements Comparator<T> {
    @Override // java.util.Comparator
    public final int compare(T t, T t2) {
        int compareValues;
        compareValues = ComparisonsKt__ComparisonsKt.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
        return compareValues;
    }
}
