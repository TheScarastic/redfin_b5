package com.google.common.collect;

import java.util.Arrays;
/* loaded from: classes.dex */
public class CompactLinkedHashMap<K, V> extends CompactHashMap<K, V> {
    private final boolean accessOrder = false;
    public transient int firstEntry;
    public transient int lastEntry;
    public transient long[] links;

    public CompactLinkedHashMap() {
        super(3);
    }

    @Override // com.google.common.collect.CompactHashMap
    public void accessEntry(int i) {
        if (this.accessOrder) {
            long[] jArr = this.links;
            setSucceeds((int) (jArr[i] >>> 32), (int) jArr[i]);
            setSucceeds(this.lastEntry, i);
            setSucceeds(i, -2);
            this.modCount++;
        }
    }

    @Override // com.google.common.collect.CompactHashMap
    public int adjustAfterRemove(int i, int i2) {
        return i >= this.size ? i2 : i;
    }

    @Override // com.google.common.collect.CompactHashMap
    public void allocArrays() {
        super.allocArrays();
        long[] jArr = new long[this.keys.length];
        this.links = jArr;
        Arrays.fill(jArr, -1L);
    }

    @Override // com.google.common.collect.CompactHashMap, java.util.AbstractMap, java.util.Map
    public void clear() {
        if (!needsAllocArrays()) {
            this.firstEntry = -2;
            this.lastEntry = -2;
            Arrays.fill(this.links, 0, this.size, -1L);
            super.clear();
        }
    }

    @Override // com.google.common.collect.CompactHashMap
    public int firstEntryIndex() {
        return this.firstEntry;
    }

    public final int getPredecessor(int i) {
        return (int) (this.links[i] >>> 32);
    }

    @Override // com.google.common.collect.CompactHashMap
    public int getSuccessor(int i) {
        return (int) this.links[i];
    }

    @Override // com.google.common.collect.CompactHashMap
    public void init(int i) {
        super.init(i);
        this.firstEntry = -2;
        this.lastEntry = -2;
    }

    @Override // com.google.common.collect.CompactHashMap
    public void insertEntry(int i, K k, V v, int i2) {
        super.insertEntry(i, k, v, i2);
        setSucceeds(this.lastEntry, i);
        setSucceeds(i, -2);
    }

    @Override // com.google.common.collect.CompactHashMap
    public void moveLastEntry(int i) {
        int i2 = this.size - 1;
        super.moveLastEntry(i);
        long[] jArr = this.links;
        setSucceeds((int) (jArr[i] >>> 32), (int) jArr[i]);
        if (i < i2) {
            setSucceeds(getPredecessor(i2), i);
            setSucceeds(i, getSuccessor(i2));
        }
        this.links[i2] = -1;
    }

    @Override // com.google.common.collect.CompactHashMap
    public void resizeEntries(int i) {
        super.resizeEntries(i);
        long[] jArr = this.links;
        int length = jArr.length;
        long[] copyOf = Arrays.copyOf(jArr, i);
        this.links = copyOf;
        if (length < i) {
            Arrays.fill(copyOf, length, i, -1L);
        }
    }

    public final void setSucceeds(int i, int i2) {
        if (i == -2) {
            this.firstEntry = i2;
        } else {
            long[] jArr = this.links;
            jArr[i] = (jArr[i] & -4294967296L) | (((long) i2) & 4294967295L);
        }
        if (i2 == -2) {
            this.lastEntry = i;
            return;
        }
        long[] jArr2 = this.links;
        jArr2[i2] = (4294967295L & jArr2[i2]) | (((long) i) << 32);
    }

    public CompactLinkedHashMap(int i) {
        super(i);
    }
}
