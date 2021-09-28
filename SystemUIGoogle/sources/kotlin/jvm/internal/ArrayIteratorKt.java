package kotlin.jvm.internal;

import java.util.Iterator;
/* compiled from: ArrayIterator.kt */
/* loaded from: classes2.dex */
public final class ArrayIteratorKt {
    public static final <T> Iterator<T> iterator(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "array");
        return new ArrayIterator(tArr);
    }
}
