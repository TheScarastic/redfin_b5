package com.android.systemui.statusbar.notification;

import android.graphics.Bitmap;
import androidx.palette.graphics.Palette;
/* loaded from: classes.dex */
public class MediaNotificationProcessor {
    public static Palette.Swatch findBackgroundSwatch(Bitmap bitmap) {
        return findBackgroundSwatch(generateArtworkPaletteBuilder(bitmap).generate());
    }

    public static Palette.Swatch findBackgroundSwatch(Palette palette) {
        Palette.Swatch dominantSwatch = palette.getDominantSwatch();
        if (dominantSwatch == null) {
            return new Palette.Swatch(-1, 100);
        }
        if (!isWhiteOrBlack(dominantSwatch.getHsl())) {
            return dominantSwatch;
        }
        float f = -1.0f;
        Palette.Swatch swatch = null;
        for (Palette.Swatch swatch2 : palette.getSwatches()) {
            if (swatch2 != dominantSwatch && ((float) swatch2.getPopulation()) > f && !isWhiteOrBlack(swatch2.getHsl())) {
                f = (float) swatch2.getPopulation();
                swatch = swatch2;
            }
        }
        if (swatch == null) {
            return dominantSwatch;
        }
        return ((float) dominantSwatch.getPopulation()) / f > 2.5f ? dominantSwatch : swatch;
    }

    public static Palette.Builder generateArtworkPaletteBuilder(Bitmap bitmap) {
        return Palette.from(bitmap).setRegion(0, 0, bitmap.getWidth() / 2, bitmap.getHeight()).clearFilters().resizeBitmapArea(22500);
    }

    private static boolean isWhiteOrBlack(float[] fArr) {
        return isBlack(fArr) || isWhite(fArr);
    }

    private static boolean isBlack(float[] fArr) {
        return fArr[2] <= 0.08f;
    }

    private static boolean isWhite(float[] fArr) {
        return fArr[2] >= 0.9f;
    }
}
