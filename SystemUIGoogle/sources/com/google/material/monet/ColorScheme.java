package com.google.material.monet;

import android.app.WallpaperColors;
import android.graphics.Color;
import com.android.internal.graphics.cam.Cam;
import com.android.internal.graphics.cam.CamUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt__MathJVMKt;
import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: ColorScheme.kt */
/* loaded from: classes2.dex */
public final class ColorScheme {
    public static final Companion Companion = new Companion(null);
    private final List<Integer> accent1;
    private final List<Integer> accent2;
    private final List<Integer> accent3;
    private final boolean darkTheme;
    private final List<Integer> neutral1;
    private final List<Integer> neutral2;

    public ColorScheme(int i, boolean z) {
        this.darkTheme = z;
        Cam fromInt = Cam.fromInt(i == 0 ? -14979341 : i);
        float hue = fromInt.getHue();
        int[] of = Shades.of(hue, RangesKt___RangesKt.coerceAtLeast(fromInt.getChroma(), 48.0f));
        Intrinsics.checkNotNullExpressionValue(of, "of(hue, chroma)");
        this.accent1 = ArraysKt___ArraysKt.toList(of);
        int[] of2 = Shades.of(hue, 16.0f);
        Intrinsics.checkNotNullExpressionValue(of2, "of(hue, ACCENT2_CHROMA)");
        this.accent2 = ArraysKt___ArraysKt.toList(of2);
        int[] of3 = Shades.of(60.0f + hue, 32.0f);
        Intrinsics.checkNotNullExpressionValue(of3, "of(hue + ACCENT3_HUE_SHIFT, ACCENT3_CHROMA)");
        this.accent3 = ArraysKt___ArraysKt.toList(of3);
        int[] of4 = Shades.of(hue, 4.0f);
        Intrinsics.checkNotNullExpressionValue(of4, "of(hue, NEUTRAL1_CHROMA)");
        this.neutral1 = ArraysKt___ArraysKt.toList(of4);
        int[] of5 = Shades.of(hue, 8.0f);
        Intrinsics.checkNotNullExpressionValue(of5, "of(hue, NEUTRAL2_CHROMA)");
        this.neutral2 = ArraysKt___ArraysKt.toList(of5);
    }

    public final List<Integer> getAccent1() {
        return this.accent1;
    }

    public final List<Integer> getAllAccentColors() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.accent1);
        arrayList.addAll(this.accent2);
        arrayList.addAll(this.accent3);
        return arrayList;
    }

    public final List<Integer> getAllNeutralColors() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.neutral1);
        arrayList.addAll(this.neutral2);
        return arrayList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ColorScheme {\n  neutral1: ");
        Companion companion = Companion;
        sb.append(companion.humanReadable(this.neutral1));
        sb.append("\n  neutral2: ");
        sb.append(companion.humanReadable(this.neutral2));
        sb.append("\n  accent1: ");
        sb.append(companion.humanReadable(this.accent1));
        sb.append("\n  accent2: ");
        sb.append(companion.humanReadable(this.accent2));
        sb.append("\n  accent3: ");
        sb.append(companion.humanReadable(this.accent3));
        sb.append("\n}");
        return sb.toString();
    }

    /* compiled from: ColorScheme.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int getSeedColor(WallpaperColors wallpaperColors) {
            Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
            return ((Number) CollectionsKt.first((List<? extends Object>) getSeedColors(wallpaperColors))).intValue();
        }

        public final List<Integer> getSeedColors(WallpaperColors wallpaperColors) {
            Object obj;
            boolean z;
            Intrinsics.checkNotNullParameter(wallpaperColors, "wallpaperColors");
            Iterator it = wallpaperColors.getAllColors().values().iterator();
            if (it.hasNext()) {
                Object next = it.next();
                while (it.hasNext()) {
                    Integer num = (Integer) it.next();
                    int intValue = ((Integer) next).intValue();
                    Intrinsics.checkNotNullExpressionValue(num, "b");
                    next = Integer.valueOf(intValue + num.intValue());
                }
                double intValue2 = (double) ((Number) next).intValue();
                boolean z2 = intValue2 == 0.0d;
                if (z2) {
                    List<Color> mainColors = wallpaperColors.getMainColors();
                    Intrinsics.checkNotNullExpressionValue(mainColors, "wallpaperColors.mainColors");
                    ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(mainColors, 10));
                    for (Color color : mainColors) {
                        arrayList.add(Integer.valueOf(color.toArgb()));
                    }
                    List list = CollectionsKt___CollectionsKt.distinct(arrayList);
                    ArrayList arrayList2 = new ArrayList();
                    for (Object obj2 : list) {
                        int intValue3 = ((Number) obj2).intValue();
                        if (Cam.fromInt(intValue3).getChroma() >= 15.0f && CamUtils.lstarFromInt(intValue3) >= 10.0f) {
                            arrayList2.add(obj2);
                        }
                    }
                    List<Integer> list2 = CollectionsKt___CollectionsKt.toList(arrayList2);
                    return list2.isEmpty() ? CollectionsKt__CollectionsJVMKt.listOf(-14979341) : list2;
                }
                Map allColors = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors, "wallpaperColors.allColors");
                Map<Integer, Double> linkedHashMap = new LinkedHashMap<>(MapsKt__MapsJVMKt.mapCapacity(allColors.size()));
                for (Map.Entry entry : allColors.entrySet()) {
                    linkedHashMap.put(entry.getKey(), Double.valueOf(((double) ((Number) entry.getValue()).intValue()) / intValue2));
                }
                Map allColors2 = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors2, "wallpaperColors.allColors");
                Map<Integer, ? extends Cam> linkedHashMap2 = new LinkedHashMap<>(MapsKt__MapsJVMKt.mapCapacity(allColors2.size()));
                for (Map.Entry entry2 : allColors2.entrySet()) {
                    Object key = entry2.getKey();
                    Object key2 = entry2.getKey();
                    Intrinsics.checkNotNullExpressionValue(key2, "it.key");
                    linkedHashMap2.put(key, Cam.fromInt(((Number) key2).intValue()));
                }
                List<Double> huePopulations = huePopulations(linkedHashMap2, linkedHashMap);
                Map allColors3 = wallpaperColors.getAllColors();
                Intrinsics.checkNotNullExpressionValue(allColors3, "wallpaperColors.allColors");
                LinkedHashMap linkedHashMap3 = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(allColors3.size()));
                for (Map.Entry entry3 : allColors3.entrySet()) {
                    Object key3 = entry3.getKey();
                    Cam cam = (Cam) linkedHashMap2.get(entry3.getKey());
                    Intrinsics.checkNotNull(cam);
                    int i = MathKt__MathJVMKt.roundToInt(cam.getHue());
                    int i2 = i - 15;
                    int i3 = i + 15;
                    double d = 0.0d;
                    if (i2 <= i3) {
                        while (true) {
                            int i4 = i2 + 1;
                            d += huePopulations.get(ColorScheme.Companion.wrapDegrees(i2)).doubleValue();
                            if (i2 == i3) {
                                break;
                            }
                            i2 = i4;
                        }
                    }
                    linkedHashMap3.put(key3, Double.valueOf(d));
                }
                LinkedHashMap linkedHashMap4 = new LinkedHashMap();
                for (Map.Entry<Integer, ? extends Cam> entry4 : linkedHashMap2.entrySet()) {
                    Integer key4 = entry4.getKey();
                    Intrinsics.checkNotNullExpressionValue(key4, "it.key");
                    float lstarFromInt = CamUtils.lstarFromInt(key4.intValue());
                    Double d2 = (Double) linkedHashMap3.get(entry4.getKey());
                    Intrinsics.checkNotNull(d2);
                    if (((Cam) entry4.getValue()).getChroma() >= 15.0f && lstarFromInt >= 10.0f && (z2 || d2.doubleValue() > 0.01d)) {
                        linkedHashMap4.put(entry4.getKey(), entry4.getValue());
                    }
                }
                ArrayList arrayList3 = new ArrayList();
                LinkedHashMap linkedHashMap5 = new LinkedHashMap(MapsKt__MapsJVMKt.mapCapacity(linkedHashMap4.size()));
                for (Map.Entry entry5 : linkedHashMap4.entrySet()) {
                    Object key5 = entry5.getKey();
                    Companion companion = ColorScheme.Companion;
                    Object value = entry5.getValue();
                    Intrinsics.checkNotNullExpressionValue(value, "it.value");
                    Double d3 = (Double) linkedHashMap3.get(entry5.getKey());
                    Intrinsics.checkNotNull(d3);
                    linkedHashMap5.put(key5, Double.valueOf(companion.score((Cam) value, d3.doubleValue())));
                }
                List<Map.Entry> list3 = CollectionsKt___CollectionsKt.toMutableList((Collection) linkedHashMap5.entrySet());
                if (list3.size() > 1) {
                    CollectionsKt__MutableCollectionsJVMKt.sortWith(list3, new ColorScheme$Companion$getSeedColors$$inlined$sortByDescending$1());
                }
                for (Map.Entry entry6 : list3) {
                    Integer num2 = (Integer) entry6.getKey();
                    Iterator it2 = arrayList3.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            obj = null;
                            break;
                        }
                        obj = it2.next();
                        int intValue4 = ((Number) obj).intValue();
                        Cam cam2 = (Cam) linkedHashMap2.get(num2);
                        Intrinsics.checkNotNull(cam2);
                        float hue = cam2.getHue();
                        Cam cam3 = (Cam) linkedHashMap2.get(Integer.valueOf(intValue4));
                        Intrinsics.checkNotNull(cam3);
                        if (ColorScheme.Companion.hueDiff(hue, cam3.getHue()) < 15.0f) {
                            z = true;
                            continue;
                        } else {
                            z = false;
                            continue;
                        }
                        if (z) {
                            break;
                        }
                    }
                    if (!(obj != null)) {
                        Intrinsics.checkNotNullExpressionValue(num2, "int");
                        arrayList3.add(num2);
                    }
                }
                if (arrayList3.isEmpty()) {
                    arrayList3.add(-14979341);
                }
                return arrayList3;
            }
            throw new UnsupportedOperationException("Empty collection can't be reduced.");
        }

        private final int wrapDegrees(int i) {
            if (i < 0) {
                return (i % 360) + 360;
            }
            return i >= 360 ? i % 360 : i;
        }

        private final float hueDiff(float f, float f2) {
            return 180.0f - Math.abs(Math.abs(f - f2) - 180.0f);
        }

        /* access modifiers changed from: private */
        public final String humanReadable(List<Integer> list) {
            return CollectionsKt___CollectionsKt.joinToString$default(list, null, null, null, 0, null, ColorScheme$Companion$humanReadable$1.INSTANCE, 31, null);
        }

        private final double score(Cam cam, double d) {
            float f;
            double d2;
            double d3 = d * 70.0d;
            if (cam.getChroma() < 48.0f) {
                d2 = 0.1d;
                f = cam.getChroma();
            } else {
                d2 = 0.3d;
                f = cam.getChroma();
            }
            return (((double) (f - 48.0f)) * d2) + d3;
        }

        private final List<Double> huePopulations(Map<Integer, ? extends Cam> map, Map<Integer, Double> map2) {
            ArrayList arrayList = new ArrayList(360);
            for (int i = 0; i < 360; i++) {
                arrayList.add(Double.valueOf(0.0d));
            }
            List<Double> list = CollectionsKt___CollectionsKt.toMutableList((Collection) arrayList);
            for (Map.Entry<Integer, Double> entry : map2.entrySet()) {
                Double d = map2.get(entry.getKey());
                Intrinsics.checkNotNull(d);
                double doubleValue = d.doubleValue();
                Cam cam = (Cam) map.get(entry.getKey());
                Intrinsics.checkNotNull(cam);
                int i2 = MathKt__MathJVMKt.roundToInt(cam.getHue()) % 360;
                list.set(i2, Double.valueOf(list.get(i2).doubleValue() + doubleValue));
            }
            return list;
        }
    }
}
