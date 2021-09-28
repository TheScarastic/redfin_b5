package android.support.v4.util;

import android.support.v4.util.MapCollections;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V> {
    public MapCollections<K, V> mCollections;

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        MapCollections<K, V> collection = getCollection();
        if (collection.mEntrySet == null) {
            collection.mEntrySet = new MapCollections.EntrySet();
        }
        return collection.mEntrySet;
    }

    public final MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<K, V>() { // from class: android.support.v4.util.ArrayMap.1
                @Override // android.support.v4.util.MapCollections
                public Object colGetEntry(int i, int i2) {
                    return ArrayMap.this.mArray[(i << 1) + i2];
                }

                @Override // android.support.v4.util.MapCollections
                public int colIndexOfKey(Object obj) {
                    return ArrayMap.this.indexOfKey(obj);
                }
            };
        }
        return this.mCollections;
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        MapCollections<K, V> collection = getCollection();
        if (collection.mKeySet == null) {
            collection.mKeySet = new MapCollections.KeySet();
        }
        return collection.mKeySet;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: android.support.v4.util.ArrayMap<K, V> */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        int size = map.size() + this.mSize;
        int i = this.mSize;
        int[] iArr = this.mHashes;
        if (iArr.length < size) {
            Object[] objArr = this.mArray;
            allocArrays(size);
            if (this.mSize > 0) {
                System.arraycopy(iArr, 0, this.mHashes, 0, i);
                System.arraycopy(objArr, 0, this.mArray, 0, i << 1);
            }
            SimpleArrayMap.freeArrays(iArr, objArr, i);
        }
        if (this.mSize == i) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return;
        }
        throw new ConcurrentModificationException();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        MapCollections<K, V> collection = getCollection();
        if (collection.mValues == null) {
            collection.mValues = new MapCollections.ValuesCollection();
        }
        return collection.mValues;
    }
}
