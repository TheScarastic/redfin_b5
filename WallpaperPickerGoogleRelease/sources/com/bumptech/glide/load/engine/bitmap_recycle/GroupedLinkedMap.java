package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class GroupedLinkedMap<K extends Poolable, V> {
    public final LinkedEntry<K, V> head = new LinkedEntry<>(null);
    public final Map<K, LinkedEntry<K, V>> keyToEntry = new HashMap();

    /* loaded from: classes.dex */
    public static class LinkedEntry<K, V> {
        public final K key;
        public LinkedEntry<K, V> next;
        public LinkedEntry<K, V> prev;
        public List<V> values;

        public LinkedEntry() {
            this(null);
        }

        public V removeLast() {
            List<V> list = this.values;
            int size = list != null ? list.size() : 0;
            if (size > 0) {
                return this.values.remove(size - 1);
            }
            return null;
        }

        public LinkedEntry(K k) {
            this.prev = this;
            this.next = this;
            this.key = k;
        }
    }

    public V get(K k) {
        LinkedEntry<K, V> linkedEntry = this.keyToEntry.get(k);
        if (linkedEntry == null) {
            linkedEntry = new LinkedEntry<>(k);
            this.keyToEntry.put(k, linkedEntry);
        } else {
            k.offer();
        }
        LinkedEntry<K, V> linkedEntry2 = linkedEntry.prev;
        linkedEntry2.next = linkedEntry.next;
        linkedEntry.next.prev = linkedEntry2;
        LinkedEntry<K, V> linkedEntry3 = this.head;
        linkedEntry.prev = linkedEntry3;
        LinkedEntry<K, V> linkedEntry4 = linkedEntry3.next;
        linkedEntry.next = linkedEntry4;
        linkedEntry4.prev = linkedEntry;
        linkedEntry.prev.next = linkedEntry;
        return linkedEntry.removeLast();
    }

    public void put(K k, V v) {
        LinkedEntry<K, V> linkedEntry = this.keyToEntry.get(k);
        if (linkedEntry == null) {
            linkedEntry = new LinkedEntry<>(k);
            LinkedEntry<K, V> linkedEntry2 = linkedEntry.prev;
            linkedEntry2.next = linkedEntry.next;
            linkedEntry.next.prev = linkedEntry2;
            LinkedEntry<K, V> linkedEntry3 = this.head;
            linkedEntry.prev = linkedEntry3.prev;
            linkedEntry.next = linkedEntry3;
            linkedEntry3.prev = linkedEntry;
            linkedEntry.prev.next = linkedEntry;
            this.keyToEntry.put(k, linkedEntry);
        } else {
            k.offer();
        }
        if (linkedEntry.values == null) {
            linkedEntry.values = new ArrayList();
        }
        linkedEntry.values.add(v);
    }

    public V removeLast() {
        for (LinkedEntry linkedEntry = this.head.prev; !linkedEntry.equals(this.head); linkedEntry = linkedEntry.prev) {
            V v = (V) linkedEntry.removeLast();
            if (v != null) {
                return v;
            }
            LinkedEntry<K, V> linkedEntry2 = linkedEntry.prev;
            linkedEntry2.next = linkedEntry.next;
            linkedEntry.next.prev = linkedEntry2;
            this.keyToEntry.remove(linkedEntry.key);
            ((Poolable) linkedEntry.key).offer();
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("GroupedLinkedMap( ");
        boolean z = false;
        for (LinkedEntry linkedEntry = this.head.next; !linkedEntry.equals(this.head); linkedEntry = linkedEntry.next) {
            z = true;
            sb.append('{');
            sb.append(linkedEntry.key);
            sb.append(':');
            List<V> list = linkedEntry.values;
            sb.append(list != null ? list.size() : 0);
            sb.append("}, ");
        }
        if (z) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(" )");
        return sb.toString();
    }
}
