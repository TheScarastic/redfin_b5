package com.android.keyguard;

import android.util.SparseArray;
import kotlin.jvm.functions.Function0;
/* compiled from: TextAnimator.kt */
/* loaded from: classes.dex */
public final class TextAnimatorKt {
    public static final <V> V getOrElse(SparseArray<V> sparseArray, int i, Function0<? extends V> function0) {
        V v = sparseArray.get(i);
        if (v != null) {
            return v;
        }
        V v2 = (V) function0.invoke();
        sparseArray.put(i, v2);
        return v2;
    }
}
