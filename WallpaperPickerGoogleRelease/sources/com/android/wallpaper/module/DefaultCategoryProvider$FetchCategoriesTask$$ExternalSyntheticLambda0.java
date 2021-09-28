package com.android.wallpaper.module;

import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.WallpaperInfo;
import com.android.wallpaper.module.DefaultCategoryProvider;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda0 INSTANCE = new DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda0();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        int i = DefaultCategoryProvider.FetchCategoriesTask.$r8$clinit;
        return ((LiveWallpaperInfo) ((WallpaperInfo) obj)).mInfo.getPackageName();
    }
}
