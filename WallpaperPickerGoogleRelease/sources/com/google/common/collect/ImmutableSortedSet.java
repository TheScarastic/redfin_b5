package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.SortedSet;
/* loaded from: classes.dex */
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements NavigableSet<E>, SortedIterable<E> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final transient Comparator<? super E> comparator;
    public transient ImmutableSortedSet<E> descendingSet;

    /* loaded from: classes.dex */
    public static final class Builder<E> extends ImmutableSet.Builder<E> {
        public final Comparator<? super E> comparator;

        public Builder(Comparator<? super E> comparator) {
            Objects.requireNonNull(comparator);
            this.comparator = comparator;
        }

        /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: java.lang.Object */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder
        public ImmutableSet.Builder add(Object obj) {
            super.add((Builder<E>) obj);
            return this;
        }

        public ImmutableSortedSet<E> build() {
            RegularImmutableSortedSet regularImmutableSortedSet;
            Object[] objArr = this.contents;
            Comparator<? super E> comparator = this.comparator;
            int i = this.size;
            int i2 = ImmutableSortedSet.$r8$clinit;
            if (i == 0) {
                regularImmutableSortedSet = ImmutableSortedSet.emptySet(comparator);
            } else {
                for (int i3 = 0; i3 < i; i3++) {
                    ObjectArrays.checkElementNotNull(objArr[i3], i3);
                }
                Arrays.sort(objArr, 0, i, comparator);
                int i4 = 1;
                for (int i5 = 1; i5 < i; i5++) {
                    Object obj = objArr[i5];
                    if (comparator.compare(obj, objArr[i4 - 1]) != 0) {
                        objArr[i4] = obj;
                        i4++;
                    }
                }
                Arrays.fill(objArr, i4, i, (Object) null);
                if (i4 < objArr.length / 2) {
                    objArr = Arrays.copyOf(objArr, i4);
                }
                regularImmutableSortedSet = new RegularImmutableSortedSet(ImmutableList.asImmutableList(objArr, i4), comparator);
            }
            this.size = regularImmutableSortedSet.size();
            this.forceCopy = true;
            return regularImmutableSortedSet;
        }
    }

    /* loaded from: classes.dex */
    public static class SerializedForm<E> implements Serializable {
        private static final long serialVersionUID = 0;
        public final Comparator<? super E> comparator;
        public final Object[] elements;

        public SerializedForm(Comparator<? super E> comparator, Object[] objArr) {
            this.comparator = comparator;
            this.elements = objArr;
        }

        public Object readResolve() {
            Builder builder = new Builder(this.comparator);
            Object[] objArr = this.elements;
            if (builder.hashTable != null) {
                for (Object obj : objArr) {
                    builder.add(obj);
                }
            } else {
                int length = objArr.length;
                for (int i = 0; i < length; i++) {
                    ObjectArrays.checkElementNotNull(objArr[i], i);
                }
                builder.getReadyToExpandTo(builder.size + objArr.length);
                System.arraycopy(objArr, 0, builder.contents, builder.size, objArr.length);
                builder.size += objArr.length;
            }
            return builder.build();
        }
    }

    public ImmutableSortedSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
        if (NaturalOrdering.INSTANCE.equals(comparator)) {
            return (RegularImmutableSortedSet<E>) RegularImmutableSortedSet.NATURAL_EMPTY_SET;
        }
        return new RegularImmutableSortedSet<>(RegularImmutableList.EMPTY, comparator);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // java.util.NavigableSet
    public E ceiling(E e) {
        return (E) Iterators.getNext(tailSet((ImmutableSortedSet<E>) e, true).iterator(), null);
    }

    @Override // java.util.SortedSet, com.google.common.collect.SortedIterable
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    public abstract ImmutableSortedSet<E> createDescendingSet();

    @Override // java.util.NavigableSet
    public abstract UnmodifiableIterator<E> descendingIterator();

    @Override // java.util.SortedSet
    public E first() {
        return iterator().next();
    }

    @Override // java.util.NavigableSet
    public E floor(E e) {
        return (E) Iterators.getNext(headSet((ImmutableSortedSet<E>) e, true).descendingIterator(), null);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet, java.util.SortedSet
    public SortedSet headSet(Object obj) {
        return headSet((ImmutableSortedSet<E>) obj, false);
    }

    public abstract ImmutableSortedSet<E> headSetImpl(E e, boolean z);

    @Override // java.util.NavigableSet
    public E higher(E e) {
        return (E) Iterators.getNext(tailSet((ImmutableSortedSet<E>) e, false).iterator(), null);
    }

    @Override // java.util.SortedSet
    public E last() {
        return descendingIterator().next();
    }

    @Override // java.util.NavigableSet
    public E lower(E e) {
        return (E) Iterators.getNext(headSet((ImmutableSortedSet<E>) e, false).descendingIterator(), null);
    }

    @Override // java.util.NavigableSet
    @Deprecated
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableSet
    @Deprecated
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    public abstract ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2);

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet, java.util.SortedSet
    public SortedSet tailSet(Object obj) {
        return tailSet((ImmutableSortedSet<E>) obj, true);
    }

    public abstract ImmutableSortedSet<E> tailSetImpl(E e, boolean z);

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    public Object writeReplace() {
        return new SerializedForm(this.comparator, toArray());
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> immutableSortedSet = this.descendingSet;
        if (immutableSortedSet != null) {
            return immutableSortedSet;
        }
        ImmutableSortedSet<E> createDescendingSet = createDescendingSet();
        this.descendingSet = createDescendingSet;
        createDescendingSet.descendingSet = this;
        return createDescendingSet;
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet<E> headSet(E e, boolean z) {
        Objects.requireNonNull(e);
        return headSetImpl(e, z);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: java.lang.Object */
    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet, java.util.SortedSet
    public SortedSet subSet(Object obj, Object obj2) {
        return subSet((boolean) obj, true, (boolean) obj2, false);
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet<E> tailSet(E e, boolean z) {
        Objects.requireNonNull(e);
        return tailSetImpl(e, z);
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet<E> subSet(E e, boolean z, E e2, boolean z2) {
        Objects.requireNonNull(e);
        Objects.requireNonNull(e2);
        Preconditions.checkArgument(this.comparator.compare(e, e2) <= 0);
        return subSetImpl(e, z, e2, z2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    /* renamed from: headSet  reason: collision with other method in class */
    public NavigableSet mo17headSet(Object obj, boolean z) {
        Objects.requireNonNull(obj);
        return headSetImpl(obj, z);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    /* renamed from: tailSet  reason: collision with other method in class */
    public NavigableSet mo18tailSet(Object obj, boolean z) {
        Objects.requireNonNull(obj);
        return tailSetImpl(obj, z);
    }
}
