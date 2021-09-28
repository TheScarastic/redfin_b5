package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {
    @Override // java.util.Map
    public void clear() {
        mo22delegate().clear();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return mo22delegate().containsKey(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return mo22delegate().containsValue(obj);
    }

    @Override // com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    public abstract Map<K, V> mo22delegate();

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return mo22delegate().entrySet();
    }

    @Override // java.util.Map, java.lang.Object
    public boolean equals(Object obj) {
        return obj == this || mo22delegate().equals(obj);
    }

    @Override // java.util.Map
    public V get(Object obj) {
        return mo22delegate().get(obj);
    }

    @Override // java.util.Map, java.lang.Object
    public int hashCode() {
        return mo22delegate().hashCode();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return mo22delegate().isEmpty();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return mo22delegate().keySet();
    }

    @Override // java.util.Map
    public V put(K k, V v) {
        return mo22delegate().put(k, v);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        mo22delegate().putAll(map);
    }

    @Override // java.util.Map
    public V remove(Object obj) {
        return mo22delegate().remove(obj);
    }

    @Override // java.util.Map
    public int size() {
        return mo22delegate().size();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return mo22delegate().values();
    }
}
