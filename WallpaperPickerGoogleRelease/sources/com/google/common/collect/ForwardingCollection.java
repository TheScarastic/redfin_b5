package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
/* loaded from: classes.dex */
public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E> {
    @Override // java.util.Collection
    public boolean add(E e) {
        return mo22delegate().add(e);
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        return mo22delegate().addAll(collection);
    }

    @Override // java.util.Collection
    public void clear() {
        mo22delegate().clear();
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        return mo22delegate().contains(obj);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return mo22delegate().containsAll(collection);
    }

    @Override // com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    public abstract Collection<E> mo22delegate();

    @Override // java.util.Collection
    public boolean isEmpty() {
        return mo22delegate().isEmpty();
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return mo22delegate().iterator();
    }

    @Override // java.util.Collection
    public boolean remove(Object obj) {
        return mo22delegate().remove(obj);
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        return mo22delegate().removeAll(collection);
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        return mo22delegate().retainAll(collection);
    }

    @Override // java.util.Collection
    public int size() {
        return mo22delegate().size();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        return mo22delegate().toArray();
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) mo22delegate().toArray(tArr);
    }
}
