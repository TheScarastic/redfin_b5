package com.google.common.collect;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class Multimaps$Entries<K, V> extends AbstractCollection<Map.Entry<K, V>> {
    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        AbstractMultimap.this.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        Map.Entry entry = (Map.Entry) obj;
        AbstractMultimap abstractMultimap = AbstractMultimap.this;
        Object key = entry.getKey();
        Object value = entry.getValue();
        Collection<V> collection = abstractMultimap.asMap().get(key);
        if (collection == null || !collection.contains(value)) {
            return false;
        }
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean remove(Object obj) {
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        Map.Entry entry = (Map.Entry) obj;
        return AbstractMultimap.this.remove(entry.getKey(), entry.getValue());
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return ((AbstractMapBasedMultimap) AbstractMultimap.this).totalSize;
    }
}
