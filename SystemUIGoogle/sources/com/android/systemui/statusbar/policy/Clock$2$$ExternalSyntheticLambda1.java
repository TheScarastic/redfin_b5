package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.Clock;
/* loaded from: classes.dex */
public final /* synthetic */ class Clock$2$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Clock.AnonymousClass2 f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ Clock$2$$ExternalSyntheticLambda1(Clock.AnonymousClass2 r1, String str) {
        this.f$0 = r1;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onReceive$0(this.f$1);
    }
}
