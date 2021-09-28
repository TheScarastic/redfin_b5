package com.android.systemui.controls.management;

import android.service.controls.Control;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.management.ControlsModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableMap;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: AllModel.kt */
/* loaded from: classes.dex */
public final class AllModel implements ControlsModel {
    private final List<ControlStatus> controls;
    private final ControlsModel.ControlsModelCallback controlsModelCallback;
    private final List<ElementWrapper> elements;
    private final CharSequence emptyZoneString;
    private final List<String> favoriteIds;
    private boolean modified;
    private final Void moveHelper;

    public AllModel(List<ControlStatus> list, List<String> list2, CharSequence charSequence, ControlsModel.ControlsModelCallback controlsModelCallback) {
        Intrinsics.checkNotNullParameter(list, "controls");
        Intrinsics.checkNotNullParameter(list2, "initialFavoriteIds");
        Intrinsics.checkNotNullParameter(charSequence, "emptyZoneString");
        Intrinsics.checkNotNullParameter(controlsModelCallback, "controlsModelCallback");
        this.controls = list;
        this.emptyZoneString = charSequence;
        this.controlsModelCallback = controlsModelCallback;
        HashSet hashSet = new HashSet();
        for (ControlStatus controlStatus : list) {
            hashSet.add(controlStatus.getControl().getControlId());
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : list2) {
            if (hashSet.contains((String) obj)) {
                arrayList.add(obj);
            }
        }
        this.favoriteIds = CollectionsKt___CollectionsKt.toMutableList((Collection) arrayList);
        this.elements = createWrappers(this.controls);
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public Void getMoveHelper() {
        return this.moveHelper;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public List<ControlInfo> getFavorites() {
        ControlInfo controlInfo;
        Object obj;
        List<String> list = this.favoriteIds;
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            Iterator<T> it = this.controls.iterator();
            while (true) {
                controlInfo = null;
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (Intrinsics.areEqual(((ControlStatus) obj).getControl().getControlId(), str)) {
                    break;
                }
            }
            ControlStatus controlStatus = (ControlStatus) obj;
            Control control = controlStatus == null ? null : controlStatus.getControl();
            if (control != null) {
                controlInfo = ControlInfo.Companion.fromControl(control);
            }
            if (controlInfo != null) {
                arrayList.add(controlInfo);
            }
        }
        return arrayList;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public List<ElementWrapper> getElements() {
        return this.elements;
    }

    @Override // com.android.systemui.controls.management.ControlsModel
    public void changeFavoriteStatus(String str, boolean z) {
        Boolean bool;
        Object obj;
        boolean z2;
        ControlStatus controlStatus;
        boolean z3;
        Intrinsics.checkNotNullParameter(str, "controlId");
        Iterator<T> it = getElements().iterator();
        while (true) {
            bool = null;
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            ElementWrapper elementWrapper = (ElementWrapper) obj;
            if (!(elementWrapper instanceof ControlStatusWrapper) || !Intrinsics.areEqual(((ControlStatusWrapper) elementWrapper).getControlStatus().getControl().getControlId(), str)) {
                z3 = false;
                continue;
            } else {
                z3 = true;
                continue;
            }
            if (z3) {
                break;
            }
        }
        ControlStatusWrapper controlStatusWrapper = (ControlStatusWrapper) obj;
        Boolean valueOf = Boolean.valueOf(z);
        if (!(controlStatusWrapper == null || (controlStatus = controlStatusWrapper.getControlStatus()) == null)) {
            bool = Boolean.valueOf(controlStatus.getFavorite());
        }
        if (!Intrinsics.areEqual(valueOf, bool)) {
            if (z) {
                z2 = this.favoriteIds.add(str);
            } else {
                z2 = this.favoriteIds.remove(str);
            }
            if (z2 && !this.modified) {
                this.modified = true;
                this.controlsModelCallback.onFirstChange();
            }
            if (controlStatusWrapper != null) {
                controlStatusWrapper.getControlStatus().setFavorite(z);
            }
        }
    }

    private final List<ElementWrapper> createWrappers(List<ControlStatus> list) {
        OrderedMap orderedMap = new OrderedMap(new ArrayMap());
        for (Object obj : list) {
            CharSequence zone = ((ControlStatus) obj).getControl().getZone();
            if (zone == null) {
                zone = "";
            }
            Object obj2 = orderedMap.get(zone);
            if (obj2 == null) {
                obj2 = new ArrayList();
                orderedMap.put(zone, obj2);
            }
            ((List) obj2).add(obj);
        }
        ArrayList arrayList = new ArrayList();
        Sequence sequence = null;
        for (CharSequence charSequence : orderedMap.getOrderedKeys()) {
            Object value = MapsKt.getValue(orderedMap, charSequence);
            Intrinsics.checkNotNullExpressionValue(value, "map.getValue(zoneName)");
            Sequence sequence2 = SequencesKt___SequencesKt.map(CollectionsKt___CollectionsKt.asSequence((Iterable) value), AllModel$createWrappers$values$1.INSTANCE);
            if (TextUtils.isEmpty(charSequence)) {
                sequence = sequence2;
            } else {
                Intrinsics.checkNotNullExpressionValue(charSequence, "zoneName");
                arrayList.add(new ZoneNameWrapper(charSequence));
                boolean unused = CollectionsKt__MutableCollectionsKt.addAll(arrayList, sequence2);
            }
        }
        if (sequence != null) {
            if (orderedMap.size() != 1) {
                arrayList.add(new ZoneNameWrapper(this.emptyZoneString));
            }
            boolean unused2 = CollectionsKt__MutableCollectionsKt.addAll(arrayList, sequence);
        }
        return arrayList;
    }

    /* compiled from: AllModel.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class OrderedMap<K, V> implements Map<K, V>, KMutableMap {
        private final Map<K, V> map;
        private final List<K> orderedKeys = new ArrayList();

        @Override // java.util.Map
        public boolean containsKey(Object obj) {
            return this.map.containsKey(obj);
        }

        @Override // java.util.Map
        public boolean containsValue(Object obj) {
            return this.map.containsValue(obj);
        }

        @Override // java.util.Map
        public V get(Object obj) {
            return this.map.get(obj);
        }

        public Set<Map.Entry<K, V>> getEntries() {
            return this.map.entrySet();
        }

        public Set<K> getKeys() {
            return this.map.keySet();
        }

        public int getSize() {
            return this.map.size();
        }

        public Collection<V> getValues() {
            return this.map.values();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends V> map) {
            Intrinsics.checkNotNullParameter(map, "from");
            this.map.putAll(map);
        }

        public OrderedMap(Map<K, V> map) {
            Intrinsics.checkNotNullParameter(map, "map");
            this.map = map;
        }

        @Override // java.util.Map
        public final /* bridge */ Set<Map.Entry<K, V>> entrySet() {
            return getEntries();
        }

        @Override // java.util.Map
        public final /* bridge */ Set<K> keySet() {
            return getKeys();
        }

        @Override // java.util.Map
        public final /* bridge */ int size() {
            return getSize();
        }

        @Override // java.util.Map
        public final /* bridge */ Collection<V> values() {
            return getValues();
        }

        public final List<K> getOrderedKeys() {
            return this.orderedKeys;
        }

        @Override // java.util.Map
        public V put(K k, V v) {
            if (!this.map.containsKey(k)) {
                this.orderedKeys.add(k);
            }
            return this.map.put(k, v);
        }

        @Override // java.util.Map
        public void clear() {
            this.orderedKeys.clear();
            this.map.clear();
        }

        @Override // java.util.Map
        public V remove(Object obj) {
            V remove = this.map.remove(obj);
            if (remove != null) {
                this.orderedKeys.remove(obj);
            }
            return remove;
        }
    }
}
