package com.android.wm.shell.legacysplitscreen;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
/* loaded from: classes2.dex */
public final /* synthetic */ class LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ LegacySplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ boolean[] f$1;

    public /* synthetic */ LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda7(LegacySplitScreenController.SplitScreenImpl splitScreenImpl, boolean[] zArr) {
        this.f$0 = splitScreenImpl;
        this.f$1 = zArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$isDividerVisible$0(this.f$1);
    }
}
