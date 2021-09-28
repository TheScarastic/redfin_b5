package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda4(OverviewProxyService.AnonymousClass1 r1, float f, boolean z) {
        this.f$0 = r1;
        this.f$1 = f;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setNavBarButtonAlpha$10(this.f$1, this.f$2);
    }
}
