package com.android.wm.shell.legacysplitscreen;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ LegacySplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda3(LegacySplitScreenController.SplitScreenImpl splitScreenImpl, Consumer consumer) {
        this.f$0 = splitScreenImpl;
        this.f$1 = consumer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$registerInSplitScreenListener$5(this.f$1);
    }
}
