package kotlin.collections;

import java.util.AbstractList;
import java.util.List;
/* compiled from: AbstractMutableList.kt */
/* loaded from: classes2.dex */
public abstract class AbstractMutableList<E> extends AbstractList<E> implements List<E> {
    public abstract int getSize();

    public abstract E removeAt(int i);

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ E remove(int i) {
        return removeAt(i);
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public final /* bridge */ int size() {
        return getSize();
    }
}
