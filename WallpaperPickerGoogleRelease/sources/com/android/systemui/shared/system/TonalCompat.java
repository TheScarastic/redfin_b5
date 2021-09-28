package com.android.systemui.shared.system;

import android.app.WallpaperColors;
import android.content.Context;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.colorextraction.types.Tonal;
/* loaded from: classes.dex */
public class TonalCompat {
    private final Tonal mTonal;

    /* loaded from: classes.dex */
    public static class ExtractionInfo {
        public int mainColor;
        public int secondaryColor;
        public boolean supportsDarkText;
        public boolean supportsDarkTheme;
    }

    public TonalCompat(Context context) {
        this.mTonal = new Tonal(context);
    }

    public ExtractionInfo extractDarkColors(WallpaperColors wallpaperColors) {
        ColorExtractor.GradientColors gradientColors = new ColorExtractor.GradientColors();
        this.mTonal.extractInto(wallpaperColors, new ColorExtractor.GradientColors(), gradientColors, new ColorExtractor.GradientColors());
        ExtractionInfo extractionInfo = new ExtractionInfo();
        extractionInfo.mainColor = gradientColors.getMainColor();
        extractionInfo.secondaryColor = gradientColors.getSecondaryColor();
        extractionInfo.supportsDarkText = gradientColors.supportsDarkText();
        if (wallpaperColors != null) {
            extractionInfo.supportsDarkTheme = (wallpaperColors.getColorHints() & 2) != 0;
        }
        return extractionInfo;
    }
}
