package com.google.common.base;

import java.io.IOException;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class Joiner {
    public final String separator;

    public Joiner(String str) {
        Objects.requireNonNull(str);
        this.separator = str;
    }

    public <A extends Appendable> A appendTo(A a, Iterator<?> it) throws IOException {
        Objects.requireNonNull(a);
        if (it.hasNext()) {
            Object next = it.next();
            Objects.requireNonNull(next);
            a.append(next instanceof CharSequence ? (CharSequence) next : next.toString());
            while (it.hasNext()) {
                a.append(this.separator);
                Object next2 = it.next();
                Objects.requireNonNull(next2);
                a.append(next2 instanceof CharSequence ? (CharSequence) next2 : next2.toString());
            }
        }
        return a;
    }

    public final String join(Iterable<?> iterable) {
        Iterator<?> it = iterable.iterator();
        StringBuilder sb = new StringBuilder();
        try {
            appendTo(sb, it);
            return sb.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public final String join(final Object obj, final Object obj2, final Object... objArr) {
        return join(new AbstractList<Object>() { // from class: com.google.common.base.Joiner.3
            @Override // java.util.AbstractList, java.util.List
            public Object get(int i) {
                if (i == 0) {
                    return obj;
                }
                if (i != 1) {
                    return objArr[i - 2];
                }
                return obj2;
            }

            @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
            public int size() {
                return objArr.length + 2;
            }
        });
    }
}
