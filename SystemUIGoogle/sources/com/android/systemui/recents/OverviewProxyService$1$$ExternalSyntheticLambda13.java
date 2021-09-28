package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;
import dagger.Lazy;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda13 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda13(int i) {
        this.f$0 = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        OverviewProxyService.AnonymousClass1.lambda$startScreenPinning$0(this.f$0, (Lazy) obj);
    }
}
