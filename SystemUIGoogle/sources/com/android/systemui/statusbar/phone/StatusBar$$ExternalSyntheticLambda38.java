package com.android.systemui.statusbar.phone;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda38 implements Consumer {
    public static final /* synthetic */ StatusBar$$ExternalSyntheticLambda38 INSTANCE = new StatusBar$$ExternalSyntheticLambda38();

    private /* synthetic */ StatusBar$$ExternalSyntheticLambda38() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((LegacySplitScreen) obj).onAppTransitionFinished();
    }
}
