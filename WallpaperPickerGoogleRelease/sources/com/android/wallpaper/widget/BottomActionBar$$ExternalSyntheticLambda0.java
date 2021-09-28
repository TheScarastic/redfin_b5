package com.android.wallpaper.widget;

import android.view.View;
import android.view.WindowInsets;
/* loaded from: classes.dex */
public final /* synthetic */ class BottomActionBar$$ExternalSyntheticLambda0 implements View.OnApplyWindowInsetsListener {
    public static final /* synthetic */ BottomActionBar$$ExternalSyntheticLambda0 INSTANCE = new BottomActionBar$$ExternalSyntheticLambda0();

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        int i = BottomActionBar.$r8$clinit;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsets.getSystemWindowInsetBottom());
        return windowInsets;
    }
}
