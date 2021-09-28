package com.android.wm.shell.legacysplitscreen;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.function.BiConsumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ LegacySplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ BiConsumer f$1;

    public /* synthetic */ LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda2(LegacySplitScreenController.SplitScreenImpl splitScreenImpl, BiConsumer biConsumer) {
        this.f$0 = splitScreenImpl;
        this.f$1 = biConsumer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$registerBoundsChangeListener$7(this.f$1);
    }
}
