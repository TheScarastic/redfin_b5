package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class ReverseOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Ordering<? super T> forwardOrder;

    /* access modifiers changed from: package-private */
    public ReverseOrdering(Ordering<? super T> ordering) {
        this.forwardOrder = (Ordering) Preconditions.checkNotNull(ordering);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(T t, T t2) {
        return this.forwardOrder.compare(t2, t);
    }

    /* JADX DEBUG: Type inference failed for r0v1. Raw type applied. Possible types: com.google.common.collect.Ordering<? super T>, com.google.common.collect.Ordering<S extends T> */
    @Override // com.google.common.collect.Ordering
    public <S extends T> Ordering<S> reverse() {
        return (Ordering<? super T>) this.forwardOrder;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return -this.forwardOrder.hashCode();
    }

    @Override // java.util.Comparator, java.lang.Object
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ReverseOrdering) {
            return this.forwardOrder.equals(((ReverseOrdering) obj).forwardOrder);
        }
        return false;
    }

    @Override // java.lang.Object
    public String toString() {
        return this.forwardOrder + ".reverse()";
    }
}
