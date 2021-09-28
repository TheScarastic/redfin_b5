package com.android.systemui.statusbar.phone;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda39 implements Consumer {
    public static final /* synthetic */ StatusBar$$ExternalSyntheticLambda39 INSTANCE = new StatusBar$$ExternalSyntheticLambda39();

    private /* synthetic */ StatusBar$$ExternalSyntheticLambda39() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((LegacySplitScreen) obj).onAppTransitionFinished();
    }
}
