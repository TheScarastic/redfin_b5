package com.android.systemui.biometrics;

import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.systemui.biometrics.AuthCredentialPatternView;
/* loaded from: classes.dex */
public final /* synthetic */ class AuthCredentialPatternView$UnlockPatternListener$$ExternalSyntheticLambda0 implements LockPatternChecker.OnVerifyCallback {
    public final /* synthetic */ AuthCredentialPatternView.UnlockPatternListener f$0;

    public /* synthetic */ AuthCredentialPatternView$UnlockPatternListener$$ExternalSyntheticLambda0(AuthCredentialPatternView.UnlockPatternListener unlockPatternListener) {
        this.f$0 = unlockPatternListener;
    }

    public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i) {
        AuthCredentialPatternView.UnlockPatternListener.$r8$lambda$DzEEwr7zVIysKmmyBDaJH_abX3g(this.f$0, verifyCredentialResponse, i);
    }
}
