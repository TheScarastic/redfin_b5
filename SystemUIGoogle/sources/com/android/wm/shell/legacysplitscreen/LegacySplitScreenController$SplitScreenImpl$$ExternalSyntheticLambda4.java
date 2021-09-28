package com.android.wm.shell.legacysplitscreen;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
/* loaded from: classes2.dex */
public final /* synthetic */ class LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ LegacySplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda4(LegacySplitScreenController.SplitScreenImpl splitScreenImpl, boolean z) {
        this.f$0 = splitScreenImpl;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onKeyguardVisibilityChanged$1(this.f$1);
    }
}
