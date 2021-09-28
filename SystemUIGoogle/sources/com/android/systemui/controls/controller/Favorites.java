package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: ControlsControllerImpl.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Favorites {
    public static final Favorites INSTANCE = new Favorites();
    private static Map<ComponentName, ? extends List<StructureInfo>> favMap = MapsKt__MapsKt.emptyMap();

    private Favorites() {
    }

    public final List<StructureInfo> getAllStructures() {
        Map<ComponentName, ? extends List<StructureInfo>> map = favMap;
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<ComponentName, ? extends List<StructureInfo>> entry : map.entrySet()) {
            boolean unused = CollectionsKt__MutableCollectionsKt.addAll(arrayList, (List) entry.getValue());
        }
        return arrayList;
    }

    public final List<StructureInfo> getStructuresForComponent(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        List<StructureInfo> list = (List) favMap.get(componentName);
        return list == null ? CollectionsKt__CollectionsKt.emptyList() : list;
    }

    public final List<ControlInfo> getControlsForStructure(StructureInfo structureInfo) {
        List<ControlInfo> list;
        Object obj;
        Intrinsics.checkNotNullParameter(structureInfo, "structure");
        Iterator<T> it = getStructuresForComponent(structureInfo.getComponentName()).iterator();
        while (true) {
            list = null;
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (Intrinsics.areEqual(((StructureInfo) obj).getStructure(), structureInfo.getStructure())) {
                break;
            }
        }
        StructureInfo structureInfo2 = (StructureInfo) obj;
        if (structureInfo2 != null) {
            list = structureInfo2.getControls();
        }
        return list == null ? CollectionsKt__CollectionsKt.emptyList() : list;
    }

    public final List<ControlInfo> getControlsForComponent(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        List<StructureInfo> structuresForComponent = getStructuresForComponent(componentName);
        ArrayList arrayList = new ArrayList();
        for (StructureInfo structureInfo : structuresForComponent) {
            boolean unused = CollectionsKt__MutableCollectionsKt.addAll(arrayList, structureInfo.getControls());
        }
        return arrayList;
    }

    public final void removeStructures(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Map<ComponentName, ? extends List<StructureInfo>> map = MapsKt__MapsKt.toMutableMap(favMap);
        map.remove(componentName);
        favMap = map;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v3, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    public final boolean addFavorite(ComponentName componentName, CharSequence charSequence, ControlInfo controlInfo) {
        boolean z;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(charSequence, "structureName");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        List<ControlInfo> controlsForComponent = getControlsForComponent(componentName);
        if (!(controlsForComponent instanceof Collection) || !controlsForComponent.isEmpty()) {
            for (ControlInfo controlInfo2 : controlsForComponent) {
                if (Intrinsics.areEqual(controlInfo2.getControlId(), controlInfo.getControlId())) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (z) {
            return false;
        }
        List list = (List) favMap.get(componentName);
        StructureInfo structureInfo = null;
        if (list != null) {
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                if (Intrinsics.areEqual(((StructureInfo) next).getStructure(), charSequence)) {
                    structureInfo = next;
                    break;
                }
            }
            structureInfo = structureInfo;
        }
        if (structureInfo == null) {
            structureInfo = new StructureInfo(componentName, charSequence, CollectionsKt__CollectionsKt.emptyList());
        }
        replaceControls(StructureInfo.copy$default(structureInfo, null, null, CollectionsKt___CollectionsKt.plus(structureInfo.getControls(), controlInfo), 3, null));
        return true;
    }

    public final void replaceControls(StructureInfo structureInfo) {
        Intrinsics.checkNotNullParameter(structureInfo, "updatedStructure");
        Map<ComponentName, ? extends List<StructureInfo>> map = MapsKt__MapsKt.toMutableMap(favMap);
        ArrayList arrayList = new ArrayList();
        ComponentName componentName = structureInfo.getComponentName();
        boolean z = false;
        for (StructureInfo structureInfo2 : getStructuresForComponent(componentName)) {
            if (Intrinsics.areEqual(structureInfo2.getStructure(), structureInfo.getStructure())) {
                z = true;
                structureInfo2 = structureInfo;
            }
            if (!structureInfo2.getControls().isEmpty()) {
                arrayList.add(structureInfo2);
            }
        }
        if (!z && !structureInfo.getControls().isEmpty()) {
            arrayList.add(structureInfo);
        }
        map.put(componentName, arrayList);
        favMap = map;
    }

    public final void clear() {
        favMap = MapsKt__MapsKt.emptyMap();
    }

    public final boolean updateControls(ComponentName componentName, List<Control> list) {
        Pair pair;
        boolean z;
        ControlInfo controlInfo;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(list, "controls");
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt___RangesKt.coerceAtLeast(MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10)), 16));
        for (Object obj : list) {
            linkedHashMap.put(((Control) obj).getControlId(), obj);
        }
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        boolean z2 = false;
        for (StructureInfo structureInfo : getStructuresForComponent(componentName)) {
            for (ControlInfo controlInfo2 : structureInfo.getControls()) {
                Control control = (Control) linkedHashMap.get(controlInfo2.getControlId());
                if (control == null) {
                    pair = null;
                } else {
                    if (!Intrinsics.areEqual(control.getTitle(), controlInfo2.getControlTitle()) || !Intrinsics.areEqual(control.getSubtitle(), controlInfo2.getControlSubtitle()) || control.getDeviceType() != controlInfo2.getDeviceType()) {
                        CharSequence title = control.getTitle();
                        Intrinsics.checkNotNullExpressionValue(title, "updatedControl.title");
                        CharSequence subtitle = control.getSubtitle();
                        Intrinsics.checkNotNullExpressionValue(subtitle, "updatedControl.subtitle");
                        controlInfo = ControlInfo.copy$default(controlInfo2, null, title, subtitle, control.getDeviceType(), 1, null);
                        z = true;
                    } else {
                        z = z2;
                        controlInfo = controlInfo2;
                    }
                    CharSequence structure = control.getStructure();
                    if (structure == null) {
                        structure = "";
                    }
                    if (!Intrinsics.areEqual(structureInfo.getStructure(), structure)) {
                        z = true;
                    }
                    pair = new Pair(structure, controlInfo);
                    z2 = z;
                }
                if (pair == null) {
                    pair = new Pair(structureInfo.getStructure(), controlInfo2);
                }
                CharSequence charSequence = (CharSequence) pair.component1();
                ControlInfo controlInfo3 = (ControlInfo) pair.component2();
                Object obj2 = linkedHashMap2.get(charSequence);
                if (obj2 == null) {
                    obj2 = new ArrayList();
                    linkedHashMap2.put(charSequence, obj2);
                }
                ((List) obj2).add(controlInfo3);
            }
        }
        if (!z2) {
            return false;
        }
        ArrayList arrayList = new ArrayList(linkedHashMap2.size());
        for (Map.Entry entry : linkedHashMap2.entrySet()) {
            arrayList.add(new StructureInfo(componentName, (CharSequence) entry.getKey(), (List) entry.getValue()));
        }
        Map<ComponentName, ? extends List<StructureInfo>> map = MapsKt__MapsKt.toMutableMap(favMap);
        map.put(componentName, arrayList);
        favMap = map;
        return true;
    }

    public final void load(List<StructureInfo> list) {
        Intrinsics.checkNotNullParameter(list, "structures");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : list) {
            ComponentName componentName = ((StructureInfo) obj).getComponentName();
            Object obj2 = linkedHashMap.get(componentName);
            if (obj2 == null) {
                obj2 = new ArrayList();
                linkedHashMap.put(componentName, obj2);
            }
            ((List) obj2).add(obj);
        }
        favMap = linkedHashMap;
    }
}
