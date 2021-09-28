package com.android.systemui.keyguard;

import android.hardware.biometrics.BiometricSourceType;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
/* compiled from: FaceAuthScreenBrightnessController.kt */
/* loaded from: classes.dex */
public final class FaceAuthScreenBrightnessController$keyguardUpdateCallback$1 extends KeyguardUpdateMonitorCallback {
    final /* synthetic */ FaceAuthScreenBrightnessController this$0;

    /* access modifiers changed from: package-private */
    public FaceAuthScreenBrightnessController$keyguardUpdateCallback$1(FaceAuthScreenBrightnessController faceAuthScreenBrightnessController) {
        this.this$0 = faceAuthScreenBrightnessController;
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
        if (biometricSourceType == BiometricSourceType.FACE) {
            FaceAuthScreenBrightnessController faceAuthScreenBrightnessController = this.this$0;
            if (!FaceAuthScreenBrightnessController.access$getEnabled$p(faceAuthScreenBrightnessController)) {
                z = false;
            }
            FaceAuthScreenBrightnessController.access$setOverridingBrightness(faceAuthScreenBrightnessController, z);
        }
    }
}
