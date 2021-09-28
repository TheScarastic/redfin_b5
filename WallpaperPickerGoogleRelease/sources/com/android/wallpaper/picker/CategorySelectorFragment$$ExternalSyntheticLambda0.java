package com.android.wallpaper.picker;

import android.view.View;
import android.view.WindowInsets;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public final /* synthetic */ class CategorySelectorFragment$$ExternalSyntheticLambda0 implements View.OnApplyWindowInsetsListener {
    public static final /* synthetic */ CategorySelectorFragment$$ExternalSyntheticLambda0 INSTANCE = new CategorySelectorFragment$$ExternalSyntheticLambda0();

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        int i = CategorySelectorFragment.$r8$clinit;
        view.setPadding(view.getPaddingLeft(), windowInsets.getSystemWindowInsetTop(), view.getPaddingRight(), view.getPaddingBottom());
        View findViewById = view.findViewById(R.id.category_grid);
        findViewById.setPadding(findViewById.getPaddingLeft(), findViewById.getPaddingTop(), findViewById.getPaddingRight(), windowInsets.getSystemWindowInsetBottom());
        return windowInsets.consumeSystemWindowInsets();
    }
}
