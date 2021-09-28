package com.android.keyguard;

import android.graphics.fonts.FontVariationAxis;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt;
/* compiled from: Comparisons.kt */
/* renamed from: com.android.keyguard.FontInterpolator$VarFontKey$_init_$lambda-1$$inlined$sortBy$1  reason: invalid class name */
/* loaded from: classes.dex */
public final class FontInterpolator$VarFontKey$_init_$lambda1$$inlined$sortBy$1<T> implements Comparator<T> {
    @Override // java.util.Comparator
    public final int compare(T t, T t2) {
        return ComparisonsKt.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
    }
}
