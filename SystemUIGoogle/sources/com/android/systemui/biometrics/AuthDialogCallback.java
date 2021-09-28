package com.android.systemui.biometrics;
/* loaded from: classes.dex */
public interface AuthDialogCallback {
    void onDeviceCredentialPressed();

    void onDialogAnimatedIn();

    void onDismissed(int i, byte[] bArr);

    void onStartFingerprintNow();

    void onSystemEvent(int i);

    void onTryAgainPressed();
}
