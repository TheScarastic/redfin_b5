package com.android.wallpaper.picker.individual;

import android.view.View;
import android.view.WindowInsets;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public final /* synthetic */ class IndividualPickerFragment$$ExternalSyntheticLambda0 implements View.OnApplyWindowInsetsListener {
    public static final /* synthetic */ IndividualPickerFragment$$ExternalSyntheticLambda0 INSTANCE = new IndividualPickerFragment$$ExternalSyntheticLambda0();

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        int i = IndividualPickerFragment.$r8$clinit;
        view.setPadding(view.getPaddingLeft(), windowInsets.getSystemWindowInsetTop(), view.getPaddingRight(), view.getPaddingBottom());
        View findViewById = view.findViewById(R.id.wallpaper_grid);
        findViewById.setPadding(findViewById.getPaddingLeft(), findViewById.getPaddingTop(), findViewById.getPaddingRight(), windowInsets.getSystemWindowInsetBottom());
        return windowInsets.consumeSystemWindowInsets();
    }
}
