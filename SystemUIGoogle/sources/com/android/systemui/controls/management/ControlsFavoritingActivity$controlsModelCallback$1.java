package com.android.systemui.controls.management;

import android.view.View;
import com.android.systemui.controls.management.ControlsModel;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritingActivity$controlsModelCallback$1 implements ControlsModel.ControlsModelCallback {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    /* access modifiers changed from: package-private */
    public ControlsFavoritingActivity$controlsModelCallback$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    @Override // com.android.systemui.controls.management.ControlsModel.ControlsModelCallback
    public void onFirstChange() {
        View view = this.this$0.doneButton;
        if (view != null) {
            view.setEnabled(true);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("doneButton");
            throw null;
        }
    }
}
