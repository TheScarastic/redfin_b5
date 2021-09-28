package com.android.systemui.globalactions;

import com.android.systemui.MultiListLayout;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
/* loaded from: classes.dex */
public final /* synthetic */ class GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda7 implements MultiListLayout.RotationListener {
    public final /* synthetic */ GlobalActionsDialogLite.ActionsDialogLite f$0;

    public /* synthetic */ GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda7(GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite) {
        this.f$0 = actionsDialogLite;
    }

    @Override // com.android.systemui.MultiListLayout.RotationListener
    public final void onRotate(int i, int i2) {
        this.f$0.onRotate(i, i2);
    }
}
