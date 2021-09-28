package com.android.systemui.recents;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$$ExternalSyntheticLambda9 implements Consumer {
    public static final /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda9 INSTANCE = new OverviewProxyService$$ExternalSyntheticLambda9();

    private /* synthetic */ OverviewProxyService$$ExternalSyntheticLambda9() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((LegacySplitScreen) obj).setMinimized(false);
    }
}
