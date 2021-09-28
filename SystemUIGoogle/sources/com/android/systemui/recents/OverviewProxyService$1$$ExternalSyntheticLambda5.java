package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda5(OverviewProxyService.AnonymousClass1 r1, int i) {
        this.f$0 = r1;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$startScreenPinning$1(this.f$1);
    }
}
