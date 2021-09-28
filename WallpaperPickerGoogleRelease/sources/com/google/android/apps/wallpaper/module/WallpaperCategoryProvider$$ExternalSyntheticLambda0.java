package com.google.android.apps.wallpaper.module;

import com.android.wallpaper.model.Category;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class WallpaperCategoryProvider$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ WallpaperCategoryProvider$$ExternalSyntheticLambda0 INSTANCE = new WallpaperCategoryProvider$$ExternalSyntheticLambda0();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((Category) obj).mPriority > 1;
    }
}
