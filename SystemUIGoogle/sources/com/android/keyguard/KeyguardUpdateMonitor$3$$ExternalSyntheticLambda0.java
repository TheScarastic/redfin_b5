package com.android.keyguard;

import com.android.keyguard.KeyguardUpdateMonitor;
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardUpdateMonitor$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyguardUpdateMonitor.AnonymousClass3 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ KeyguardUpdateMonitor$3$$ExternalSyntheticLambda0(KeyguardUpdateMonitor.AnonymousClass3 r1, int i, boolean z) {
        this.f$0 = r1;
        this.f$1 = i;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onChanged$0(this.f$1, this.f$2);
    }
}
