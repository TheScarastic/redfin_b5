package com.android.wallpaper.module;

import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.DefaultCategoryProvider;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda3 implements Predicate {
    public static final /* synthetic */ DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda3 INSTANCE = new DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda3();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        int i = DefaultCategoryProvider.FetchCategoriesTask.$r8$clinit;
        return ((WallpaperInfo) obj) instanceof LiveWallpaperInfo;
    }
}
