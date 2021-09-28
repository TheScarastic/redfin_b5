package com.google.material.monet;

import android.app.WallpaperColors;
import android.graphics.Color;
import com.android.internal.graphics.cam.Cam;
import com.android.internal.graphics.cam.CamUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class ColorScheme {
    @NotNull
    public static final Companion Companion = new Companion(null);

    /* loaded from: classes.dex */
    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
        }

        @NotNull
        public final List<Integer> getSeedColors(@NotNull WallpaperColors wallpaperColors) {
            boolean z;
            Object obj;
            boolean z2;
            double d;
            float f;
            int i;
            Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
            Iterator it = wallpaperColors.getAllColors().values().iterator();
            if (it.hasNext()) {
                Object next = it.next();
                while (it.hasNext()) {
                    Integer num = (Integer) it.next();
                    int intValue = ((Integer) next).intValue();
                    Intrinsics.checkNotNullExpressionValue(num, "b");
                    next = Integer.valueOf(num.intValue() + intValue);
                }
                double intValue2 = (double) ((Number) next).intValue();
                boolean z3 = intValue2 == 0.0d;
                if (z3) {
                    List<Color> mainColors = wallpaperColors.getMainColors();
                    Intrinsics.checkNotNullExpressionValue(mainColors, "wallpaperColors.mainColors");
                    ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(mainColors, 10));
                    for (Color color : mainColors) {
                        arrayList.add(Integer.valueOf(color.toArgb()));
                    }
                    List list = CollectionsKt___CollectionsKt.toList(new LinkedHashSet(arrayList));
                    ArrayList arrayList2 = new ArrayList();
                    for (Object obj2 : list) {
                        int intValue3 = ((Number) obj2).intValue();
                        if (Cam.fromInt(intValue3).getChroma() >= 15.0f && CamUtils.lstarFromInt(intValue3) >= 10.0f) {
                            arrayList2.add(obj2);
                        }
                    }
                    List<Integer> list2 = CollectionsKt___CollectionsKt.toList(arrayList2);
                    return list2.isEmpty() ? CollectionsKt__CollectionsKt.listOf(-14979341) : list2;
                }
                Map allColors = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors, "wallpaperColors.allColors");
                LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(allColors.size()));
                for (Iterator it2 = allColors.entrySet().iterator(); it2.hasNext(); it2 = it2) {
                    Map.Entry entry = (Map.Entry) it2.next();
                    linkedHashMap.put(entry.getKey(), Double.valueOf(((double) ((Number) entry.getValue()).intValue()) / intValue2));
                }
                Map allColors2 = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors2, "wallpaperColors.allColors");
                LinkedHashMap linkedHashMap2 = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(allColors2.size()));
                for (Map.Entry entry2 : allColors2.entrySet()) {
                    Object key = entry2.getKey();
                    Object key2 = entry2.getKey();
                    Intrinsics.checkNotNullExpressionValue(key2, "it.key");
                    linkedHashMap2.put(key, Cam.fromInt(((Number) key2).intValue()));
                }
                int i2 = 360;
                ArrayList arrayList3 = new ArrayList(360);
                for (int i3 = 0; i3 < 360; i3++) {
                    arrayList3.add(Double.valueOf(0.0d));
                }
                List mutableList = CollectionsKt___CollectionsKt.toMutableList(arrayList3);
                for (Map.Entry entry3 : linkedHashMap.entrySet()) {
                    Double d2 = (Double) linkedHashMap.get(entry3.getKey());
                    Intrinsics.checkNotNull(d2);
                    double doubleValue = d2.doubleValue();
                    Cam cam = (Cam) linkedHashMap2.get(entry3.getKey());
                    Intrinsics.checkNotNull(cam);
                    float hue = cam.getHue();
                    if (!Float.isNaN(hue)) {
                        int round = Math.round(hue) % 360;
                        ArrayList arrayList4 = (ArrayList) mutableList;
                        arrayList4.set(round, Double.valueOf(((Number) arrayList4.get(round)).doubleValue() + doubleValue));
                    } else {
                        throw new IllegalArgumentException("Cannot round NaN value.");
                    }
                }
                Map allColors3 = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors3, "wallpaperColors.allColors");
                LinkedHashMap linkedHashMap3 = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(allColors3.size()));
                for (Map.Entry entry4 : allColors3.entrySet()) {
                    Object key3 = entry4.getKey();
                    Cam cam2 = (Cam) linkedHashMap2.get(entry4.getKey());
                    Intrinsics.checkNotNull(cam2);
                    float hue2 = cam2.getHue();
                    if (!Float.isNaN(hue2)) {
                        int round2 = Math.round(hue2);
                        int i4 = round2 - 15;
                        int i5 = round2 + 15;
                        double d3 = 0.0d;
                        if (i4 <= i5) {
                            while (true) {
                                int i6 = i4 + 1;
                                if (i4 < 0) {
                                    i = (i4 % 360) + i2;
                                } else {
                                    i = i4 >= i2 ? i4 % 360 : i4;
                                }
                                d3 = ((Number) ((ArrayList) mutableList).get(i)).doubleValue() + d3;
                                if (i4 == i5) {
                                    break;
                                }
                                i4 = i6;
                                i2 = 360;
                            }
                        }
                        linkedHashMap3.put(key3, Double.valueOf(d3));
                        i2 = 360;
                    } else {
                        throw new IllegalArgumentException("Cannot round NaN value.");
                    }
                }
                LinkedHashMap linkedHashMap4 = new LinkedHashMap();
                for (Map.Entry entry5 : linkedHashMap2.entrySet()) {
                    Object key4 = entry5.getKey();
                    Intrinsics.checkNotNullExpressionValue(key4, "it.key");
                    float lstarFromInt = CamUtils.lstarFromInt(((Number) key4).intValue());
                    Double d4 = (Double) linkedHashMap3.get(entry5.getKey());
                    Intrinsics.checkNotNull(d4);
                    if (((Cam) entry5.getValue()).getChroma() >= 15.0f && lstarFromInt >= 10.0f && (z3 || d4.doubleValue() > 0.01d)) {
                        linkedHashMap4.put(entry5.getKey(), entry5.getValue());
                    }
                }
                ArrayList arrayList5 = new ArrayList();
                LinkedHashMap linkedHashMap5 = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(linkedHashMap4.size()));
                for (Map.Entry entry6 : linkedHashMap4.entrySet()) {
                    Object key5 = entry6.getKey();
                    Object value = entry6.getValue();
                    Intrinsics.checkNotNullExpressionValue(value, "it.value");
                    Cam cam3 = (Cam) value;
                    Double d5 = (Double) linkedHashMap3.get(entry6.getKey());
                    Intrinsics.checkNotNull(d5);
                    double doubleValue2 = d5.doubleValue() * 70.0d;
                    if (cam3.getChroma() < 48.0f) {
                        d = 0.1d;
                        f = cam3.getChroma();
                    } else {
                        d = 0.3d;
                        f = cam3.getChroma();
                    }
                    linkedHashMap5.put(key5, Double.valueOf((((double) (f - 48.0f)) * d) + doubleValue2));
                }
                List mutableList2 = CollectionsKt___CollectionsKt.toMutableList(linkedHashMap5.entrySet());
                ArrayList arrayList6 = (ArrayList) mutableList2;
                if (arrayList6.size() > 1) {
                    ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1 colorScheme$Companion$getSeedColors$$inlined$sortByDescending$1 = new ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1();
                    z = true;
                    if (arrayList6.size() > 1) {
                        Collections.sort(mutableList2, colorScheme$Companion$getSeedColors$$inlined$sortByDescending$1);
                    }
                } else {
                    z = true;
                }
                Iterator it3 = arrayList6.iterator();
                while (it3.hasNext()) {
                    Integer num2 = (Integer) ((Map.Entry) it3.next()).getKey();
                    Iterator it4 = arrayList5.iterator();
                    while (true) {
                        if (!it4.hasNext()) {
                            obj = null;
                            break;
                        }
                        obj = it4.next();
                        int intValue4 = ((Number) obj).intValue();
                        Cam cam4 = (Cam) linkedHashMap2.get(num2);
                        Intrinsics.checkNotNull(cam4);
                        float hue3 = cam4.getHue();
                        Cam cam5 = (Cam) linkedHashMap2.get(Integer.valueOf(intValue4));
                        Intrinsics.checkNotNull(cam5);
                        if (180.0f - Math.abs(Math.abs(hue3 - cam5.getHue()) - 180.0f) < 15.0f) {
                            z2 = z;
                            continue;
                        } else {
                            z2 = false;
                            continue;
                        }
                        if (z2) {
                            break;
                        }
                    }
                    if (!(obj != null ? z : false)) {
                        Intrinsics.checkNotNullExpressionValue(num2, "int");
                        arrayList5.add(num2);
                    }
                }
                if (arrayList5.isEmpty()) {
                    arrayList5.add(-14979341);
                }
                return arrayList5;
            }
            throw new UnsupportedOperationException("Empty collection can't be reduced.");
        }
    }
}
