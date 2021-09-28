package com.android.systemui.recents;

import android.view.MotionEvent;
import com.android.systemui.recents.OverviewProxyService;
import dagger.Lazy;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda14 implements Consumer {
    public final /* synthetic */ OverviewProxyService.AnonymousClass1 f$0;
    public final /* synthetic */ MotionEvent f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda14(OverviewProxyService.AnonymousClass1 r1, MotionEvent motionEvent) {
        this.f$0 = r1;
        this.f$1 = motionEvent;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onStatusBarMotionEvent$4(this.f$1, (Lazy) obj);
    }
}
