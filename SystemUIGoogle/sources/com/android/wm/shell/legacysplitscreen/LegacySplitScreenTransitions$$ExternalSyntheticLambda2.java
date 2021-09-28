package com.android.wm.shell.legacysplitscreen;

import android.animation.ValueAnimator;
/* loaded from: classes2.dex */
public final /* synthetic */ class LegacySplitScreenTransitions$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ValueAnimator f$0;

    public /* synthetic */ LegacySplitScreenTransitions$$ExternalSyntheticLambda2(ValueAnimator valueAnimator) {
        this.f$0 = valueAnimator;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.start();
    }
}
