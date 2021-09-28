package com.android.customization.picker.grid;

import android.view.View;
import android.view.WindowInsets;
/* loaded from: classes.dex */
public final /* synthetic */ class GridFragment$$ExternalSyntheticLambda0 implements View.OnApplyWindowInsetsListener {
    public static final /* synthetic */ GridFragment$$ExternalSyntheticLambda0 INSTANCE = new GridFragment$$ExternalSyntheticLambda0();

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        int i = GridFragment.$r8$clinit;
        view.setPadding(view.getPaddingLeft(), windowInsets.getSystemWindowInsetTop(), view.getPaddingRight(), windowInsets.getSystemWindowInsetBottom());
        return windowInsets.consumeSystemWindowInsets();
    }
}
