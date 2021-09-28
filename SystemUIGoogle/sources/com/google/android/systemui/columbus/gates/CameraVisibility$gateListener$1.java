package com.google.android.systemui.columbus.gates;

import com.google.android.systemui.columbus.gates.Gate;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CameraVisibility.kt */
/* loaded from: classes2.dex */
public final class CameraVisibility$gateListener$1 implements Gate.Listener {
    final /* synthetic */ CameraVisibility this$0;

    /* access modifiers changed from: package-private */
    public CameraVisibility$gateListener$1(CameraVisibility cameraVisibility) {
        this.this$0 = cameraVisibility;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate.Listener
    public void onGateChanged(Gate gate) {
        Intrinsics.checkNotNullParameter(gate, "gate");
        this.this$0.updateHandler.post(new Runnable(this.this$0) { // from class: com.google.android.systemui.columbus.gates.CameraVisibility$gateListener$1$onGateChanged$1
            final /* synthetic */ CameraVisibility this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.updateCameraIsShowing();
            }
        });
    }
}
