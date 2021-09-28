package com.android.systemui.biometrics;

import android.os.Bundle;
import android.view.WindowManager;
/* loaded from: classes.dex */
public interface AuthDialog {
    void animateToCredentialUI();

    void dismissFromSystemServer();

    void dismissWithoutCallback(boolean z);

    String getOpPackageName();

    boolean isAllowDeviceCredentials();

    void onAuthenticationFailed(int i, String str);

    void onAuthenticationSucceeded();

    void onError(int i, String str);

    void onHelp(int i, String str);

    void onOrientationChanged();

    void onSaveState(Bundle bundle);

    void show(WindowManager windowManager, Bundle bundle);

    /* loaded from: classes.dex */
    public static class LayoutParams {
        final int mMediumHeight;
        final int mMediumWidth;

        /* access modifiers changed from: package-private */
        public LayoutParams(int i, int i2) {
            this.mMediumWidth = i;
            this.mMediumHeight = i2;
        }
    }
}
