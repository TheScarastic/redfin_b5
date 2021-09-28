package com.android.wallpaper.picker;

import android.view.View;
import android.view.WindowInsets;
/* loaded from: classes.dex */
public final /* synthetic */ class CategoryFragment$$ExternalSyntheticLambda1 implements View.OnApplyWindowInsetsListener {
    public static final /* synthetic */ CategoryFragment$$ExternalSyntheticLambda1 INSTANCE = new CategoryFragment$$ExternalSyntheticLambda1();

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        int i = CategoryFragment.$r8$clinit;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsets.getSystemWindowInsetBottom());
        return windowInsets;
    }
}
