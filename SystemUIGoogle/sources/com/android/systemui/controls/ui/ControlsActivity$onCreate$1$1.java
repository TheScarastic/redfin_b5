package com.android.systemui.controls.ui;

import android.view.View;
import android.view.WindowInsets;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsActivity.kt */
/* loaded from: classes.dex */
final class ControlsActivity$onCreate$1$1 implements View.OnApplyWindowInsetsListener {
    public static final ControlsActivity$onCreate$1$1 INSTANCE = new ControlsActivity$onCreate$1$1();

    ControlsActivity$onCreate$1$1() {
    }

    @Override // android.view.View.OnApplyWindowInsetsListener
    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        Intrinsics.checkNotNullParameter(view, "v");
        Intrinsics.checkNotNullParameter(windowInsets, "insets");
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsets.getInsets(WindowInsets.Type.systemBars()).bottom);
        return WindowInsets.CONSUMED;
    }
}
