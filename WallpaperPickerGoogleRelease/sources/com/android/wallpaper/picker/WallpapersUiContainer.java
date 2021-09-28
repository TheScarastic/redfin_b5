package com.android.wallpaper.picker;
/* loaded from: classes.dex */
public interface WallpapersUiContainer {
    void doneFetchingCategories();

    CategorySelectorFragment getCategorySelectorFragment();

    void onWallpapersReady();
}
