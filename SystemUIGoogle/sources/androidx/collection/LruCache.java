package androidx.collection;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    protected V create(K k) {
        return null;
    }

    protected void entryRemoved(boolean z, K k, V v, V v2) {
    }

    protected int sizeOf(K k, V v) {
        return 1;
    }

    public LruCache(int i) {
        if (i > 0) {
            this.maxSize = i;
            this.map = new LinkedHashMap<>(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }

    public final V get(K k) {
        V put;
        Objects.requireNonNull(k, "key == null");
        synchronized (this) {
            V v = this.map.get(k);
            if (v != null) {
                this.hitCount++;
                return v;
            }
            this.missCount++;
            V create = create(k);
            if (create == null) {
                return null;
            }
            synchronized (this) {
                this.createCount++;
                put = this.map.put(k, create);
                if (put != null) {
                    this.map.put(k, put);
                } else {
                    this.size += safeSizeOf(k, create);
                }
            }
            if (put != null) {
                entryRemoved(false, k, create, put);
                return put;
            }
            trimToSize(this.maxSize);
            return create;
        }
    }

    public final V put(K k, V v) {
        V put;
        if (k == null || v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        synchronized (this) {
            this.putCount++;
            this.size += safeSizeOf(k, v);
            put = this.map.put(k, v);
            if (put != null) {
                this.size -= safeSizeOf(k, put);
            }
        }
        if (put != null) {
            entryRemoved(false, k, put, v);
        }
        trimToSize(this.maxSize);
        return put;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0070, code lost:
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void trimToSize(int r5) {
        /*
            r4 = this;
        L_0x0000:
            monitor-enter(r4)
            int r0 = r4.size     // Catch: all -> 0x0071
            if (r0 < 0) goto L_0x0052
            java.util.LinkedHashMap<K, V> r0 = r4.map     // Catch: all -> 0x0071
            boolean r0 = r0.isEmpty()     // Catch: all -> 0x0071
            if (r0 == 0) goto L_0x0011
            int r0 = r4.size     // Catch: all -> 0x0071
            if (r0 != 0) goto L_0x0052
        L_0x0011:
            int r0 = r4.size     // Catch: all -> 0x0071
            if (r0 <= r5) goto L_0x0050
            java.util.LinkedHashMap<K, V> r0 = r4.map     // Catch: all -> 0x0071
            boolean r0 = r0.isEmpty()     // Catch: all -> 0x0071
            if (r0 == 0) goto L_0x001e
            goto L_0x0050
        L_0x001e:
            java.util.LinkedHashMap<K, V> r0 = r4.map     // Catch: all -> 0x0071
            java.util.Set r0 = r0.entrySet()     // Catch: all -> 0x0071
            java.util.Iterator r0 = r0.iterator()     // Catch: all -> 0x0071
            java.lang.Object r0 = r0.next()     // Catch: all -> 0x0071
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch: all -> 0x0071
            java.lang.Object r1 = r0.getKey()     // Catch: all -> 0x0071
            java.lang.Object r0 = r0.getValue()     // Catch: all -> 0x0071
            java.util.LinkedHashMap<K, V> r2 = r4.map     // Catch: all -> 0x0071
            r2.remove(r1)     // Catch: all -> 0x0071
            int r2 = r4.size     // Catch: all -> 0x0071
            int r3 = r4.safeSizeOf(r1, r0)     // Catch: all -> 0x0071
            int r2 = r2 - r3
            r4.size = r2     // Catch: all -> 0x0071
            int r2 = r4.evictionCount     // Catch: all -> 0x0071
            r3 = 1
            int r2 = r2 + r3
            r4.evictionCount = r2     // Catch: all -> 0x0071
            monitor-exit(r4)     // Catch: all -> 0x0071
            r2 = 0
            r4.entryRemoved(r3, r1, r0, r2)
            goto L_0x0000
        L_0x0050:
            monitor-exit(r4)     // Catch: all -> 0x0071
            return
        L_0x0052:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException     // Catch: all -> 0x0071
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: all -> 0x0071
            r0.<init>()     // Catch: all -> 0x0071
            java.lang.Class r1 = r4.getClass()     // Catch: all -> 0x0071
            java.lang.String r1 = r1.getName()     // Catch: all -> 0x0071
            r0.append(r1)     // Catch: all -> 0x0071
            java.lang.String r1 = ".sizeOf() is reporting inconsistent results!"
            r0.append(r1)     // Catch: all -> 0x0071
            java.lang.String r0 = r0.toString()     // Catch: all -> 0x0071
            r5.<init>(r0)     // Catch: all -> 0x0071
            throw r5     // Catch: all -> 0x0071
        L_0x0071:
            r5 = move-exception
            monitor-exit(r4)     // Catch: all -> 0x0071
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.collection.LruCache.trimToSize(int):void");
    }

    public final V remove(K k) {
        V remove;
        Objects.requireNonNull(k, "key == null");
        synchronized (this) {
            remove = this.map.remove(k);
            if (remove != null) {
                this.size -= safeSizeOf(k, remove);
            }
        }
        if (remove != null) {
            entryRemoved(false, k, remove, null);
        }
        return remove;
    }

    private int safeSizeOf(K k, V v) {
        int sizeOf = sizeOf(k, v);
        if (sizeOf >= 0) {
            return sizeOf;
        }
        throw new IllegalStateException("Negative size: " + k + "=" + v);
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final synchronized int size() {
        return this.size;
    }

    public final synchronized Map<K, V> snapshot() {
        return new LinkedHashMap(this.map);
    }

    public final synchronized String toString() {
        int i;
        int i2;
        i = this.hitCount;
        i2 = this.missCount + i;
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(i2 != 0 ? (i * 100) / i2 : 0));
    }
}
