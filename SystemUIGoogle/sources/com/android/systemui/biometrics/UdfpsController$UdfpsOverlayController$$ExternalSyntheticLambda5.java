package com.android.systemui.biometrics;

import com.android.systemui.biometrics.UdfpsController;
/* loaded from: classes.dex */
public final /* synthetic */ class UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ UdfpsController.UdfpsOverlayController f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda5(UdfpsController.UdfpsOverlayController udfpsOverlayController, String str) {
        this.f$0 = udfpsOverlayController;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setDebugMessage$5(this.f$1);
    }
}
