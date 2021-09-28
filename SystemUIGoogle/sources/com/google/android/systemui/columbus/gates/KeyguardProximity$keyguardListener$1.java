package com.google.android.systemui.columbus.gates;

import com.google.android.systemui.columbus.gates.Gate;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardProximity.kt */
/* loaded from: classes2.dex */
public final class KeyguardProximity$keyguardListener$1 implements Gate.Listener {
    final /* synthetic */ KeyguardProximity this$0;

    /* access modifiers changed from: package-private */
    public KeyguardProximity$keyguardListener$1(KeyguardProximity keyguardProximity) {
        this.this$0 = keyguardProximity;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate.Listener
    public void onGateChanged(Gate gate) {
        Intrinsics.checkNotNullParameter(gate, "gate");
        this.this$0.updateProximityListener();
    }
}
