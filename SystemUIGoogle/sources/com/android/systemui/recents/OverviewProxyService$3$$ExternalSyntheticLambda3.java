package com.android.systemui.recents;

import android.os.Bundle;
import com.android.systemui.recents.OverviewProxyService;
import com.android.wm.shell.startingsurface.StartingSurface;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$3$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ Bundle f$0;

    public /* synthetic */ OverviewProxyService$3$$ExternalSyntheticLambda3(Bundle bundle) {
        this.f$0 = bundle;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        OverviewProxyService.AnonymousClass3.lambda$onServiceConnected$3(this.f$0, (StartingSurface) obj);
    }
}
