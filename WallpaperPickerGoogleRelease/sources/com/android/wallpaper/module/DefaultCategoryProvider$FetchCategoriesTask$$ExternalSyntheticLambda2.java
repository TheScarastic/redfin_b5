package com.android.wallpaper.module;

import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.module.DefaultCategoryProvider;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda2 INSTANCE = new DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda2();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        int i = DefaultCategoryProvider.FetchCategoriesTask.$r8$clinit;
        return ((Category) obj) instanceof WallpaperCategory;
    }
}
