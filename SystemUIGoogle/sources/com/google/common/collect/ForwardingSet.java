package com.google.common.collect;

import java.util.Set;
/* loaded from: classes2.dex */
public abstract class ForwardingSet<E> extends ForwardingCollection<E> implements Set<E> {
    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    protected abstract Set<E> delegate();

    @Override // java.util.Collection, java.lang.Object, java.util.Set
    public boolean equals(Object obj) {
        return obj == this || delegate().equals(obj);
    }

    @Override // java.util.Collection, java.lang.Object, java.util.Set
    public int hashCode() {
        return delegate().hashCode();
    }
}
