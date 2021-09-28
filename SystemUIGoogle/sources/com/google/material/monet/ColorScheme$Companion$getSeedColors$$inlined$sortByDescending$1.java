package com.google.material.monet;

import java.util.Comparator;
import java.util.Map;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
/* compiled from: Comparisons.kt */
/* loaded from: classes2.dex */
public final class ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1<T> implements Comparator<T> {
    @Override // java.util.Comparator
    public final int compare(T t, T t2) {
        int compareValues;
        compareValues = ComparisonsKt__ComparisonsKt.compareValues((Double) ((Map.Entry) t2).getValue(), (Double) ((Map.Entry) t).getValue());
        return compareValues;
    }
}
