package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
/* loaded from: classes.dex */
public final /* synthetic */ class PhoneStatusBarPolicy$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PhoneStatusBarPolicy.AnonymousClass5 f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ PhoneStatusBarPolicy$5$$ExternalSyntheticLambda0(PhoneStatusBarPolicy.AnonymousClass5 r1, boolean z) {
        this.f$0 = r1;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onSensorPrivacyChanged$0(this.f$1);
    }
}
