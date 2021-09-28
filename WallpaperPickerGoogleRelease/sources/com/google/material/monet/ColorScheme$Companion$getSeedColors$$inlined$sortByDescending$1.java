package com.google.material.monet;

import java.util.Comparator;
import java.util.Map;
import kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt;
/* loaded from: classes.dex */
public final class ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1<T> implements Comparator<T> {
    @Override // java.util.Comparator
    public final int compare(T t, T t2) {
        return ComparisonsKt___ComparisonsJvmKt.compareValues((Double) ((Map.Entry) t2).getValue(), (Double) ((Map.Entry) t).getValue());
    }
}
