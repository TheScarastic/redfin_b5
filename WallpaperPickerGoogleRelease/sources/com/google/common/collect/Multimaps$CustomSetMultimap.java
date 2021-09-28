package com.google.common.collect;

import com.google.common.base.Supplier;
import com.google.common.collect.AbstractMapBasedMultimap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
/* loaded from: classes.dex */
public class Multimaps$CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> {
    private static final long serialVersionUID = 0;
    public transient Supplier<? extends Set<V>> factory;

    public Multimaps$CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> supplier) {
        super(map);
        Objects.requireNonNull(supplier);
        this.factory = supplier;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.factory = (Supplier) objectInputStream.readObject();
        setMap((Map) objectInputStream.readObject());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.factory);
        objectOutputStream.writeObject(this.map);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    public Map<K, Collection<V>> createAsMap() {
        Map<K, Collection<V>> map = this.map;
        if (map instanceof NavigableMap) {
            return new AbstractMapBasedMultimap.NavigableAsMap((NavigableMap) this.map);
        }
        if (map instanceof SortedMap) {
            return new AbstractMapBasedMultimap.SortedAsMap((SortedMap) this.map);
        }
        return new AbstractMapBasedMultimap.AsMap(this.map);
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap
    public Collection createCollection() {
        return (Set) this.factory.get();
    }

    @Override // com.google.common.collect.AbstractMapBasedMultimap, com.google.common.collect.AbstractMultimap
    public Set<K> createKeySet() {
        Map<K, Collection<V>> map = this.map;
        if (map instanceof NavigableMap) {
            return new AbstractMapBasedMultimap.NavigableKeySet((NavigableMap) this.map);
        }
        if (map instanceof SortedMap) {
            return new AbstractMapBasedMultimap.SortedKeySet((SortedMap) this.map);
        }
        return new AbstractMapBasedMultimap.KeySet(this.map);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
    public <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
        if (collection instanceof NavigableSet) {
            return Sets.unmodifiableNavigableSet((NavigableSet) collection);
        }
        if (collection instanceof SortedSet) {
            return Collections.unmodifiableSortedSet((SortedSet) collection);
        }
        return Collections.unmodifiableSet((Set) collection);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMapBasedMultimap
    public Collection<V> wrapCollection(K k, Collection<V> collection) {
        if (collection instanceof NavigableSet) {
            return new AbstractMapBasedMultimap.WrappedNavigableSet(k, (NavigableSet) collection, null);
        }
        if (collection instanceof SortedSet) {
            return new AbstractMapBasedMultimap.WrappedSortedSet(k, (SortedSet) collection, null);
        }
        return new AbstractMapBasedMultimap.WrappedSet(k, (Set) collection);
    }
}
