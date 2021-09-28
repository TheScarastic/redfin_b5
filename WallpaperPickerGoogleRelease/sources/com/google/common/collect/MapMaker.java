package com.google.common.collect;

import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapMakerInternalMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: classes.dex */
public final class MapMaker {
    public Equivalence<Object> keyEquivalence;
    public MapMakerInternalMap.Strength keyStrength;
    public boolean useCustomMap;
    public MapMakerInternalMap.Strength valueStrength;
    public int initialCapacity = -1;
    public int concurrencyLevel = -1;

    public MapMakerInternalMap.Strength getKeyStrength() {
        return (MapMakerInternalMap.Strength) MoreObjects.firstNonNull(this.keyStrength, MapMakerInternalMap.Strength.STRONG);
    }

    public MapMakerInternalMap.Strength getValueStrength() {
        return (MapMakerInternalMap.Strength) MoreObjects.firstNonNull(this.valueStrength, MapMakerInternalMap.Strength.STRONG);
    }

    public <K, V> ConcurrentMap<K, V> makeMap() {
        if (!this.useCustomMap) {
            int i = this.initialCapacity;
            if (i == -1) {
                i = 16;
            }
            int i2 = this.concurrencyLevel;
            if (i2 == -1) {
                i2 = 4;
            }
            return new ConcurrentHashMap(i, 0.75f, i2);
        }
        MapMakerInternalMap.WeakValueReference<Object, Object, MapMakerInternalMap.DummyInternalEntry> weakValueReference = MapMakerInternalMap.UNSET_WEAK_VALUE_REFERENCE;
        MapMakerInternalMap.Strength strength = MapMakerInternalMap.Strength.WEAK;
        MapMakerInternalMap.Strength keyStrength = getKeyStrength();
        MapMakerInternalMap.Strength strength2 = MapMakerInternalMap.Strength.STRONG;
        if (keyStrength == strength2 && getValueStrength() == strength2) {
            return new MapMakerInternalMap(this, MapMakerInternalMap.StrongKeyStrongValueEntry.Helper.INSTANCE);
        }
        if (getKeyStrength() == strength2 && getValueStrength() == strength) {
            return new MapMakerInternalMap(this, MapMakerInternalMap.StrongKeyWeakValueEntry.Helper.INSTANCE);
        }
        if (getKeyStrength() == strength && getValueStrength() == strength2) {
            return new MapMakerInternalMap(this, MapMakerInternalMap.WeakKeyStrongValueEntry.Helper.INSTANCE);
        }
        if (getKeyStrength() == strength && getValueStrength() == strength) {
            return new MapMakerInternalMap(this, MapMakerInternalMap.WeakKeyWeakValueEntry.Helper.INSTANCE);
        }
        throw new AssertionError();
    }

    public MapMaker setKeyStrength(MapMakerInternalMap.Strength strength) {
        MapMakerInternalMap.Strength strength2 = this.keyStrength;
        Preconditions.checkState(strength2 == null, "Key strength was already set to %s", strength2);
        Objects.requireNonNull(strength);
        this.keyStrength = strength;
        if (strength != MapMakerInternalMap.Strength.STRONG) {
            this.useCustomMap = true;
        }
        return this;
    }

    public String toString() {
        MoreObjects.ToStringHelper toStringHelper = new MoreObjects.ToStringHelper("MapMaker", null);
        int i = this.initialCapacity;
        if (i != -1) {
            toStringHelper.add("initialCapacity", i);
        }
        int i2 = this.concurrencyLevel;
        if (i2 != -1) {
            toStringHelper.add("concurrencyLevel", i2);
        }
        MapMakerInternalMap.Strength strength = this.keyStrength;
        if (strength != null) {
            String lowerCase = Ascii.toLowerCase(strength.toString());
            MoreObjects.ToStringHelper.ValueHolder valueHolder = new MoreObjects.ToStringHelper.ValueHolder(null);
            toStringHelper.holderTail.next = valueHolder;
            toStringHelper.holderTail = valueHolder;
            valueHolder.value = lowerCase;
            valueHolder.name = "keyStrength";
        }
        MapMakerInternalMap.Strength strength2 = this.valueStrength;
        if (strength2 != null) {
            String lowerCase2 = Ascii.toLowerCase(strength2.toString());
            MoreObjects.ToStringHelper.ValueHolder valueHolder2 = new MoreObjects.ToStringHelper.ValueHolder(null);
            toStringHelper.holderTail.next = valueHolder2;
            toStringHelper.holderTail = valueHolder2;
            valueHolder2.value = lowerCase2;
            valueHolder2.name = "valueStrength";
        }
        if (this.keyEquivalence != null) {
            MoreObjects.ToStringHelper.ValueHolder valueHolder3 = new MoreObjects.ToStringHelper.ValueHolder(null);
            toStringHelper.holderTail.next = valueHolder3;
            toStringHelper.holderTail = valueHolder3;
            valueHolder3.value = "keyEquivalence";
        }
        return toStringHelper.toString();
    }
}
