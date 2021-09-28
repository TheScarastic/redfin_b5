package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
/* loaded from: classes.dex */
public interface Multimap<K, V> {
    Map<K, Collection<V>> asMap();

    void clear();
}
