package com.android.keyguard;

import android.telephony.PinResult;
import com.android.keyguard.KeyguardSimPukViewController;
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardSimPukViewController$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyguardSimPukViewController.AnonymousClass3 f$0;
    public final /* synthetic */ PinResult f$1;

    public /* synthetic */ KeyguardSimPukViewController$3$$ExternalSyntheticLambda0(KeyguardSimPukViewController.AnonymousClass3 r1, PinResult pinResult) {
        this.f$0 = r1;
        this.f$1 = pinResult;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onSimLockChangedResponse$0(this.f$1);
    }
}
