package com.android.wallpaper.picker;

import android.graphics.Insets;
import android.view.View;
import android.view.WindowInsets;
import com.android.wallpaper.compat.BuildCompat;
/* loaded from: classes.dex */
public final /* synthetic */ class TopLevelPickerActivity$$ExternalSyntheticLambda0 implements View.OnApplyWindowInsetsListener {
    public static final /* synthetic */ TopLevelPickerActivity$$ExternalSyntheticLambda0 INSTANCE = new TopLevelPickerActivity$$ExternalSyntheticLambda0();

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        int i = TopLevelPickerActivity.$r8$clinit;
        view.setPadding(view.getPaddingLeft(), windowInsets.getSystemWindowInsetTop(), view.getPaddingRight(), view.getPaddingBottom());
        if (!(BuildCompat.sSdk >= 29)) {
            return windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), 0, windowInsets.getStableInsetRight(), windowInsets.getSystemWindowInsetBottom());
        }
        WindowInsets.Builder builder = new WindowInsets.Builder(windowInsets);
        builder.setSystemWindowInsets(Insets.of(windowInsets.getSystemWindowInsetLeft(), 0, windowInsets.getStableInsetRight(), windowInsets.getSystemWindowInsetBottom()));
        return builder.build();
    }
}
