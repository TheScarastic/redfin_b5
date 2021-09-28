package com.android.systemui.controls.management;

import android.view.View;
import android.widget.TextView;
import com.android.systemui.controls.management.FavoritesModel;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsEditingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsEditingActivity$favoritesModelCallback$1 implements FavoritesModel.FavoritesModelCallback {
    final /* synthetic */ ControlsEditingActivity this$0;

    /* access modifiers changed from: package-private */
    public ControlsEditingActivity$favoritesModelCallback$1(ControlsEditingActivity controlsEditingActivity) {
        this.this$0 = controlsEditingActivity;
    }

    @Override // com.android.systemui.controls.management.FavoritesModel.FavoritesModelCallback
    public void onNoneChanged(boolean z) {
        if (z) {
            TextView textView = this.this$0.subtitle;
            if (textView != null) {
                textView.setText(ControlsEditingActivity.EMPTY_TEXT_ID);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("subtitle");
                throw null;
            }
        } else {
            TextView textView2 = this.this$0.subtitle;
            if (textView2 != null) {
                textView2.setText(ControlsEditingActivity.SUBTITLE_ID);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("subtitle");
                throw null;
            }
        }
    }

    @Override // com.android.systemui.controls.management.ControlsModel.ControlsModelCallback
    public void onFirstChange() {
        View view = this.this$0.saveButton;
        if (view != null) {
            view.setEnabled(true);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("saveButton");
            throw null;
        }
    }
}
