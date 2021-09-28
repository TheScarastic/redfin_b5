package com.google.common.collect;

import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
/* loaded from: classes.dex */
public final class SortedIterables {
    public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> iterable) {
        Object obj;
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(iterable);
        if (iterable instanceof SortedSet) {
            obj = ((SortedSet) iterable).comparator();
            if (obj == null) {
                obj = NaturalOrdering.INSTANCE;
            }
        } else if (!(iterable instanceof SortedIterable)) {
            return false;
        } else {
            obj = ((SortedIterable) iterable).comparator();
        }
        return comparator.equals(obj);
    }
}
