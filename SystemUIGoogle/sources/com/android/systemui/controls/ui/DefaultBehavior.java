package com.android.systemui.controls.ui;

import android.service.controls.Control;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DefaultBehavior.kt */
/* loaded from: classes.dex */
public final class DefaultBehavior implements Behavior {
    public ControlViewHolder cvh;

    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cvh");
        throw null;
    }

    public final void setCvh(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "<set-?>");
        this.cvh = controlViewHolder;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(ControlWithState controlWithState, int i) {
        CharSequence statusText;
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        ControlViewHolder cvh = getCvh();
        Control control = controlWithState.getControl();
        CharSequence charSequence = "";
        if (!(control == null || (statusText = control.getStatusText()) == null)) {
            charSequence = statusText;
        }
        ControlViewHolder.setStatusText$default(cvh, charSequence, false, 2, null);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), false, i, false, 4, null);
    }
}
