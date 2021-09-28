package com.android.systemui.biometrics;

import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import com.android.systemui.biometrics.UdfpsController;
/* loaded from: classes.dex */
public final /* synthetic */ class UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ UdfpsController.UdfpsOverlayController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ IUdfpsOverlayControllerCallback f$2;

    public /* synthetic */ UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4(UdfpsController.UdfpsOverlayController udfpsOverlayController, int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
        this.f$0 = udfpsOverlayController;
        this.f$1 = i;
        this.f$2 = iUdfpsOverlayControllerCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$showUdfpsOverlay$0(this.f$1, this.f$2);
    }
}
