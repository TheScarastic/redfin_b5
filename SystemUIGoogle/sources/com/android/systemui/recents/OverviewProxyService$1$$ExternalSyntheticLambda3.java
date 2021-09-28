package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ float f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda3(OverviewProxyService.AnonymousClass1 r1, float f) {
        this.f$0 = r1;
        this.f$1 = f;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onAssistantGestureCompletion$12(this.f$1);
    }
}
