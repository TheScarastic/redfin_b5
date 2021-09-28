package com.android.systemui.recents;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda15 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda15(boolean z) {
        this.f$0 = z;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((LegacySplitScreen) obj).setMinimized(this.f$0);
    }
}
