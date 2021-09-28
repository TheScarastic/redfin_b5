package com.google.common.collect;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Lists {
    public static int computeArrayListCapacity(int i) {
        CollectPreconditions.checkNonnegative(i, "arraySize");
        return Ints.saturatedCast(((long) i) + 5 + ((long) (i / 10)));
    }

    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> iterable) {
        Objects.requireNonNull(iterable);
        if (iterable instanceof Collection) {
            return new ArrayList<>((Collection) iterable);
        }
        Iterator<? extends E> it = iterable.iterator();
        ArrayList<E> arrayList = new ArrayList<>();
        Iterators.addAll(arrayList, it);
        return arrayList;
    }
}
