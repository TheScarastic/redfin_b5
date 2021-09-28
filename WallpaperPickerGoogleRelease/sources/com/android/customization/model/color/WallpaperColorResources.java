package com.android.customization.model.color;

import android.app.WallpaperColors;
import android.util.SparseIntArray;
import com.android.internal.graphics.cam.Cam;
import com.google.material.monet.ColorScheme;
import com.google.material.monet.Shades;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
/* loaded from: classes.dex */
public class WallpaperColorResources {
    public final SparseIntArray mColorOverlay = new SparseIntArray();

    public WallpaperColorResources(WallpaperColors wallpaperColors) {
        int intValue = ((Number) CollectionsKt___CollectionsKt.first(ColorScheme.Companion.getSeedColors(wallpaperColors))).intValue();
        Cam fromInt = Cam.fromInt(intValue == 0 ? -14979341 : intValue);
        float hue = fromInt.getHue();
        float chroma = fromInt.getChroma();
        List<Integer> list = ArraysKt___ArraysKt.toList(Shades.of(hue, chroma < 48.0f ? 48.0f : chroma));
        List<Integer> list2 = ArraysKt___ArraysKt.toList(Shades.of(hue, 16.0f));
        List<Integer> list3 = ArraysKt___ArraysKt.toList(Shades.of(60.0f + hue, 32.0f));
        List<Integer> list4 = ArraysKt___ArraysKt.toList(Shades.of(hue, 4.0f));
        List<Integer> list5 = ArraysKt___ArraysKt.toList(Shades.of(hue, 8.0f));
        addOverlayColor(list4, 17170462);
        addOverlayColor(list5, 17170475);
        addOverlayColor(list, 17170488);
        addOverlayColor(list2, 17170501);
        addOverlayColor(list3, 17170514);
    }

    public final void addOverlayColor(List<Integer> list, int i) {
        for (Integer num : list) {
            this.mColorOverlay.put(i, num.intValue());
            i++;
        }
    }
}
