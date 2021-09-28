package com.google.android.systemui.columbus.gates;

import com.google.android.systemui.columbus.gates.Gate;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ScreenTouch.kt */
/* loaded from: classes2.dex */
public final class ScreenTouch$gateListener$1 implements Gate.Listener {
    final /* synthetic */ ScreenTouch this$0;

    /* access modifiers changed from: package-private */
    public ScreenTouch$gateListener$1(ScreenTouch screenTouch) {
        this.this$0 = screenTouch;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate.Listener
    public void onGateChanged(Gate gate) {
        Intrinsics.checkNotNullParameter(gate, "gate");
        if (this.this$0.powerState.isBlocking()) {
            this.this$0.stopListeningForTouch();
        } else {
            this.this$0.startListeningForTouch();
        }
    }
}
