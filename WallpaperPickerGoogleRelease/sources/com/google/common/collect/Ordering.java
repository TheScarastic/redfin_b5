package com.google.common.collect;

import com.google.common.collect.MapMakerInternalMap;
import java.util.Comparator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public abstract class Ordering<T> implements Comparator<T> {

    /* loaded from: classes.dex */
    public static class ArbitraryOrdering extends Ordering<Object> {
        public final AtomicInteger counter = new AtomicInteger(0);
        public final ConcurrentMap<Object, Integer> uids;

        public ArbitraryOrdering() {
            MapMaker mapMaker = new MapMaker();
            mapMaker.setKeyStrength(MapMakerInternalMap.Strength.WEAK);
            this.uids = mapMaker.makeMap();
        }

        @Override // com.google.common.collect.Ordering, java.util.Comparator
        public int compare(Object obj, Object obj2) {
            if (obj == obj2) {
                return 0;
            }
            if (obj == null) {
                return -1;
            }
            if (obj2 == null) {
                return 1;
            }
            int identityHashCode = System.identityHashCode(obj);
            int identityHashCode2 = System.identityHashCode(obj2);
            if (identityHashCode != identityHashCode2) {
                return identityHashCode < identityHashCode2 ? -1 : 1;
            }
            int compareTo = getUid(obj).compareTo(getUid(obj2));
            if (compareTo != 0) {
                return compareTo;
            }
            throw new AssertionError();
        }

        public final Integer getUid(Object obj) {
            Integer num = this.uids.get(obj);
            if (num != null) {
                return num;
            }
            Integer valueOf = Integer.valueOf(this.counter.getAndIncrement());
            Integer putIfAbsent = this.uids.putIfAbsent(obj, valueOf);
            return putIfAbsent != null ? putIfAbsent : valueOf;
        }

        @Override // java.lang.Object
        public String toString() {
            return "Ordering.arbitrary()";
        }
    }

    /* loaded from: classes.dex */
    public static class IncomparableValueException extends ClassCastException {
        private static final long serialVersionUID = 0;
        public final Object value;
    }

    public static <T> Ordering<T> from(Comparator<T> comparator) {
        if (comparator instanceof Ordering) {
            return (Ordering) comparator;
        }
        return new ComparatorOrdering(comparator);
    }

    @Override // java.util.Comparator
    public abstract int compare(T t, T t2);

    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }
}
