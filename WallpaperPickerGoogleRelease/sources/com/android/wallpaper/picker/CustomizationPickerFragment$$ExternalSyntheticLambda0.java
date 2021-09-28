package com.android.wallpaper.picker;

import android.view.View;
import android.view.WindowInsets;
/* loaded from: classes.dex */
public final /* synthetic */ class CustomizationPickerFragment$$ExternalSyntheticLambda0 implements View.OnApplyWindowInsetsListener {
    public static final /* synthetic */ CustomizationPickerFragment$$ExternalSyntheticLambda0 INSTANCE = new CustomizationPickerFragment$$ExternalSyntheticLambda0();

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        int i = CustomizationPickerFragment.$r8$clinit;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsets.getSystemWindowInsetBottom());
        return windowInsets.consumeSystemWindowInsets();
    }
}
