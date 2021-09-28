package com.android.systemui.controls.management;

import android.view.View;
import android.widget.TextView;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
final class ZoneHolder extends Holder {
    private final TextView zone = (TextView) this.itemView;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ZoneHolder(View view) {
        super(view, null);
        Intrinsics.checkNotNullParameter(view, "view");
    }

    @Override // com.android.systemui.controls.management.Holder
    public void bindData(ElementWrapper elementWrapper) {
        Intrinsics.checkNotNullParameter(elementWrapper, "wrapper");
        this.zone.setText(((ZoneNameWrapper) elementWrapper).getZoneName());
    }
}
