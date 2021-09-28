package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda9(OverviewProxyService.AnonymousClass1 r1, boolean z) {
        this.f$0 = r1;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onOverviewShown$8(this.f$1);
    }
}
