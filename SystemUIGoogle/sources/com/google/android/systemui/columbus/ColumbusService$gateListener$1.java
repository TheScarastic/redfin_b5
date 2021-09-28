package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.gates.Gate;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusService.kt */
/* loaded from: classes2.dex */
public final class ColumbusService$gateListener$1 implements Gate.Listener {
    final /* synthetic */ ColumbusService this$0;

    /* access modifiers changed from: package-private */
    public ColumbusService$gateListener$1(ColumbusService columbusService) {
        this.this$0 = columbusService;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate.Listener
    public void onGateChanged(Gate gate) {
        Intrinsics.checkNotNullParameter(gate, "gate");
        ColumbusService.access$updateSensorListener(this.this$0);
    }
}
