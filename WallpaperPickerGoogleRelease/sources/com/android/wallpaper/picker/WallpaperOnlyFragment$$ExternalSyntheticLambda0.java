package com.android.wallpaper.picker;

import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.model.WallpaperSectionController;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class WallpaperOnlyFragment$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ WallpaperOnlyFragment$$ExternalSyntheticLambda0 INSTANCE = new WallpaperOnlyFragment$$ExternalSyntheticLambda0();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        int i = WallpaperOnlyFragment.$r8$clinit;
        return ((CustomizationSectionController) obj) instanceof WallpaperSectionController;
    }
}
