package com.android.keyguard;

import android.graphics.fonts.Font;
import android.graphics.fonts.FontVariationAxis;
import android.util.MathUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: FontInterpolator.kt */
/* loaded from: classes.dex */
public final class FontInterpolator {
    public static final Companion Companion = new Companion(null);
    private static final FontVariationAxis[] EMPTY_AXES = new FontVariationAxis[0];
    private final HashMap<InterpKey, Font> interpCache = new HashMap<>();
    private final HashMap<VarFontKey, Font> verFontCache = new HashMap<>();
    private final InterpKey tmpInterpKey = new InterpKey(null, null, 0.0f);
    private final VarFontKey tmpVarFontKey = new VarFontKey(0, 0, new ArrayList());

    /* compiled from: FontInterpolator.kt */
    /* loaded from: classes.dex */
    private static final class InterpKey {
        private Font l;
        private float progress;
        private Font r;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof InterpKey)) {
                return false;
            }
            InterpKey interpKey = (InterpKey) obj;
            return Intrinsics.areEqual(this.l, interpKey.l) && Intrinsics.areEqual(this.r, interpKey.r) && Intrinsics.areEqual(Float.valueOf(this.progress), Float.valueOf(interpKey.progress));
        }

        public int hashCode() {
            Font font = this.l;
            int i = 0;
            int hashCode = (font == null ? 0 : font.hashCode()) * 31;
            Font font2 = this.r;
            if (font2 != null) {
                i = font2.hashCode();
            }
            return ((hashCode + i) * 31) + Float.hashCode(this.progress);
        }

        public String toString() {
            return "InterpKey(l=" + this.l + ", r=" + this.r + ", progress=" + this.progress + ')';
        }

        public InterpKey(Font font, Font font2, float f) {
            this.l = font;
            this.r = font2;
            this.progress = f;
        }

        public final void set(Font font, Font font2, float f) {
            Intrinsics.checkNotNullParameter(font, "l");
            Intrinsics.checkNotNullParameter(font2, "r");
            this.l = font;
            this.r = font2;
            this.progress = f;
        }
    }

    /* compiled from: FontInterpolator.kt */
    /* loaded from: classes.dex */
    private static final class VarFontKey {
        private int index;
        private final List<FontVariationAxis> sortedAxes;
        private int sourceId;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof VarFontKey)) {
                return false;
            }
            VarFontKey varFontKey = (VarFontKey) obj;
            return this.sourceId == varFontKey.sourceId && this.index == varFontKey.index && Intrinsics.areEqual(this.sortedAxes, varFontKey.sortedAxes);
        }

        public int hashCode() {
            return (((Integer.hashCode(this.sourceId) * 31) + Integer.hashCode(this.index)) * 31) + this.sortedAxes.hashCode();
        }

        public String toString() {
            return "VarFontKey(sourceId=" + this.sourceId + ", index=" + this.index + ", sortedAxes=" + this.sortedAxes + ')';
        }

        public VarFontKey(int i, int i2, List<FontVariationAxis> list) {
            Intrinsics.checkNotNullParameter(list, "sortedAxes");
            this.sourceId = i;
            this.index = i2;
            this.sortedAxes = list;
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public VarFontKey(android.graphics.fonts.Font r4, java.util.List<android.graphics.fonts.FontVariationAxis> r5) {
            /*
                r3 = this;
                java.lang.String r0 = "font"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
                java.lang.String r0 = "axes"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
                int r0 = r4.getSourceIdentifier()
                int r4 = r4.getTtcIndex()
                java.util.List r5 = kotlin.collections.CollectionsKt.toMutableList(r5)
                int r1 = r5.size()
                r2 = 1
                if (r1 <= r2) goto L_0x0025
                com.android.keyguard.FontInterpolator$VarFontKey$_init_$lambda-1$$inlined$sortBy$1 r1 = new com.android.keyguard.FontInterpolator$VarFontKey$_init_$lambda-1$$inlined$sortBy$1
                r1.<init>()
                kotlin.collections.CollectionsKt.sortWith(r5, r1)
            L_0x0025:
                kotlin.Unit r1 = kotlin.Unit.INSTANCE
                r3.<init>(r0, r4, r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.FontInterpolator.VarFontKey.<init>(android.graphics.fonts.Font, java.util.List):void");
        }

        public final void set(Font font, List<FontVariationAxis> list) {
            Intrinsics.checkNotNullParameter(font, "font");
            Intrinsics.checkNotNullParameter(list, "axes");
            this.sourceId = font.getSourceIdentifier();
            this.index = font.getTtcIndex();
            this.sortedAxes.clear();
            this.sortedAxes.addAll(list);
            List<FontVariationAxis> list2 = this.sortedAxes;
            if (list2.size() > 1) {
                CollectionsKt__MutableCollectionsJVMKt.sortWith(list2, new FontInterpolator$VarFontKey$set$$inlined$sortBy$1());
            }
        }
    }

    public final Font lerp(Font font, Font font2, float f) {
        Intrinsics.checkNotNullParameter(font, "start");
        Intrinsics.checkNotNullParameter(font2, "end");
        boolean z = true;
        if (f == 0.0f) {
            return font;
        }
        if (f == 1.0f) {
            return font2;
        }
        FontVariationAxis[] axes = font.getAxes();
        if (axes == null) {
            axes = EMPTY_AXES;
        }
        FontVariationAxis[] axes2 = font2.getAxes();
        if (axes2 == null) {
            axes2 = EMPTY_AXES;
        }
        if (axes.length == 0) {
            if (axes2.length != 0) {
                z = false;
            }
            if (z) {
                return font;
            }
        }
        this.tmpInterpKey.set(font, font2, f);
        Font font3 = this.interpCache.get(this.tmpInterpKey);
        if (font3 != null) {
            return font3;
        }
        List<FontVariationAxis> lerp = lerp(axes, axes2, new Function3<String, Float, Float, Float>(this, f) { // from class: com.android.keyguard.FontInterpolator$lerp$newAxes$1
            final /* synthetic */ float $progress;
            final /* synthetic */ FontInterpolator this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$progress = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object, java.lang.Object] */
            @Override // kotlin.jvm.functions.Function3
            public /* bridge */ /* synthetic */ Float invoke(String str, Float f2, Float f3) {
                return Float.valueOf(invoke(str, f2, f3));
            }

            public final float invoke(String str, Float f2, Float f3) {
                float f4;
                float f5;
                Intrinsics.checkNotNullParameter(str, "tag");
                if (Intrinsics.areEqual(str, "wght")) {
                    FontInterpolator fontInterpolator = this.this$0;
                    float f6 = 400.0f;
                    if (f2 == null) {
                        f5 = 400.0f;
                    } else {
                        f5 = f2.floatValue();
                    }
                    if (f3 != null) {
                        f6 = f3.floatValue();
                    }
                    return FontInterpolator.access$adjustWeight(fontInterpolator, MathUtils.lerp(f5, f6, this.$progress));
                } else if (Intrinsics.areEqual(str, "ital")) {
                    FontInterpolator fontInterpolator2 = this.this$0;
                    float f7 = 0.0f;
                    if (f2 == null) {
                        f4 = 0.0f;
                    } else {
                        f4 = f2.floatValue();
                    }
                    if (f3 != null) {
                        f7 = f3.floatValue();
                    }
                    return FontInterpolator.access$adjustItalic(fontInterpolator2, MathUtils.lerp(f4, f7, this.$progress));
                } else {
                    if ((f2 == null || f3 == null) ? false : true) {
                        return MathUtils.lerp(f2.floatValue(), f3.floatValue(), this.$progress);
                    }
                    throw new IllegalArgumentException(Intrinsics.stringPlus("Unable to interpolate due to unknown default axes value : ", str).toString());
                }
            }
        });
        this.tmpVarFontKey.set(font, lerp);
        Font font4 = this.verFontCache.get(this.tmpVarFontKey);
        if (font4 != null) {
            this.interpCache.put(new InterpKey(font, font2, f), font4);
            return font4;
        }
        Font.Builder builder = new Font.Builder(font);
        Object[] array = lerp.toArray(new FontVariationAxis[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
        Font build = builder.setFontVariationSettings((FontVariationAxis[]) array).build();
        HashMap<InterpKey, Font> hashMap = this.interpCache;
        InterpKey interpKey = new InterpKey(font, font2, f);
        Intrinsics.checkNotNullExpressionValue(build, "newFont");
        hashMap.put(interpKey, build);
        this.verFontCache.put(new VarFontKey(font, lerp), build);
        return build;
    }

    /* access modifiers changed from: private */
    public final float adjustWeight(float f) {
        return coerceInWithStep(f, 0.0f, 1000.0f, 10.0f);
    }

    /* access modifiers changed from: private */
    public final float adjustItalic(float f) {
        return coerceInWithStep(f, 0.0f, 1.0f, 0.1f);
    }

    private final float coerceInWithStep(float f, float f2, float f3, float f4) {
        return ((float) ((int) (RangesKt___RangesKt.coerceIn(f, f2, f3) / f4))) * f4;
    }

    /* compiled from: FontInterpolator.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean canInterpolate(Font font, Font font2) {
            Intrinsics.checkNotNullParameter(font, "start");
            Intrinsics.checkNotNullParameter(font2, "end");
            return font.getTtcIndex() == font2.getTtcIndex() && font.getSourceIdentifier() == font2.getSourceIdentifier();
        }
    }

    private final List<FontVariationAxis> lerp(FontVariationAxis[] fontVariationAxisArr, FontVariationAxis[] fontVariationAxisArr2, Function3<? super String, ? super Float, ? super Float, Float> function3) {
        int i;
        int i2;
        FontVariationAxis fontVariationAxis;
        if (fontVariationAxisArr.length > 1) {
            ArraysKt___ArraysJvmKt.sortWith(fontVariationAxisArr, new Comparator<T>() { // from class: com.android.keyguard.FontInterpolator$lerp$$inlined$sortBy$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    int compareValues;
                    compareValues = ComparisonsKt__ComparisonsKt.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                    return compareValues;
                }
            });
        }
        if (fontVariationAxisArr2.length > 1) {
            ArraysKt___ArraysJvmKt.sortWith(fontVariationAxisArr2, new Comparator<T>() { // from class: com.android.keyguard.FontInterpolator$lerp$$inlined$sortBy$2
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    int compareValues;
                    compareValues = ComparisonsKt__ComparisonsKt.compareValues(((FontVariationAxis) t).getTag(), ((FontVariationAxis) t2).getTag());
                    return compareValues;
                }
            });
        }
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i3 >= fontVariationAxisArr.length && i4 >= fontVariationAxisArr2.length) {
                return arrayList;
            }
            String tag = i3 < fontVariationAxisArr.length ? fontVariationAxisArr[i3].getTag() : null;
            String tag2 = i4 < fontVariationAxisArr2.length ? fontVariationAxisArr2[i4].getTag() : null;
            if (tag == null) {
                i = 1;
            } else {
                i = tag2 == null ? -1 : tag.compareTo(tag2);
            }
            if (i == 0) {
                Intrinsics.checkNotNull(tag);
                int i5 = i4 + 1;
                fontVariationAxis = new FontVariationAxis(tag, function3.invoke(tag, Float.valueOf(fontVariationAxisArr[i3].getStyleValue()), Float.valueOf(fontVariationAxisArr2[i4].getStyleValue())).floatValue());
                i3++;
                i2 = i5;
            } else if (i < 0) {
                Intrinsics.checkNotNull(tag);
                fontVariationAxis = new FontVariationAxis(tag, function3.invoke(tag, Float.valueOf(fontVariationAxisArr[i3].getStyleValue()), null).floatValue());
                i2 = i4;
                i3++;
            } else {
                Intrinsics.checkNotNull(tag2);
                i2 = i4 + 1;
                fontVariationAxis = new FontVariationAxis(tag2, function3.invoke(tag2, null, Float.valueOf(fontVariationAxisArr2[i4].getStyleValue())).floatValue());
            }
            arrayList.add(fontVariationAxis);
            i4 = i2;
        }
    }
}
