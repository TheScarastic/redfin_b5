package com.android.systemui.recents;

import com.android.wm.shell.pip.Pip;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda16 implements Consumer {
    public static final /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda16 INSTANCE = new OverviewProxyService$1$$ExternalSyntheticLambda16();

    private /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda16() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((Pip) obj).setPinnedStackAnimationType(1);
    }
}
