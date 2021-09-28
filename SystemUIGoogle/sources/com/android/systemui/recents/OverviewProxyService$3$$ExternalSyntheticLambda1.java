package com.android.systemui.recents;

import android.os.Bundle;
import com.android.systemui.recents.OverviewProxyService;
import com.android.wm.shell.pip.Pip;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$3$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ Bundle f$0;

    public /* synthetic */ OverviewProxyService$3$$ExternalSyntheticLambda1(Bundle bundle) {
        this.f$0 = bundle;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        OverviewProxyService.AnonymousClass3.lambda$onServiceConnected$0(this.f$0, (Pip) obj);
    }
}
