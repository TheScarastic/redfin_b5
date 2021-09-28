package kotlin.collections;

import java.util.Iterator;
/* compiled from: Grouping.kt */
/* loaded from: classes2.dex */
public interface Grouping<T, K> {
    K keyOf(T t);

    Iterator<T> sourceIterator();
}
