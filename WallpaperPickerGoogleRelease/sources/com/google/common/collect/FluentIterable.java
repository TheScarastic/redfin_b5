package com.google.common.collect;

import com.google.common.base.Absent;
import com.google.common.base.Optional;
/* loaded from: classes.dex */
public abstract class FluentIterable<E> implements Iterable<E> {
    public final Optional<Iterable<E>> iterableDelegate = Absent.INSTANCE;

    @Override // java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean z = true;
        for (E e : this.iterableDelegate.or(this)) {
            if (!z) {
                sb.append(", ");
            }
            z = false;
            sb.append(e);
        }
        sb.append(']');
        return sb.toString();
    }
}
