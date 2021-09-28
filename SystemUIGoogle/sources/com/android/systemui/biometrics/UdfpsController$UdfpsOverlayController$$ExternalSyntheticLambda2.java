package com.android.systemui.biometrics;

import com.android.systemui.biometrics.UdfpsController;
/* loaded from: classes.dex */
public final /* synthetic */ class UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ UdfpsController.UdfpsOverlayController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2(UdfpsController.UdfpsOverlayController udfpsOverlayController, int i) {
        this.f$0 = udfpsOverlayController;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onAcquiredGood$2(this.f$1);
    }
}
