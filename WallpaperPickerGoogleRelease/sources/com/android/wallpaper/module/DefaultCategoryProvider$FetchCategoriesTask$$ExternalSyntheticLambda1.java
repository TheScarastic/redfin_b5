package com.android.wallpaper.module;

import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.module.DefaultCategoryProvider;
import java.util.Collections;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda1 INSTANCE = new DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda1();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        int i = DefaultCategoryProvider.FetchCategoriesTask.$r8$clinit;
        return Collections.unmodifiableList(((WallpaperCategory) ((Category) obj)).mWallpapers).stream().filter(DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda3.INSTANCE).map(DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda0.INSTANCE);
    }
}
