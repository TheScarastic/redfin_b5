package com.android.wallpaper.model;
/* loaded from: classes.dex */
public interface CategoryReceiver {
    void doneFetchingCategories();

    void onCategoryReceived(Category category);
}
