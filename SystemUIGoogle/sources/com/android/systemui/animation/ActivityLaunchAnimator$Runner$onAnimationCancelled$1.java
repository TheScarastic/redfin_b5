package com.android.systemui.animation;

import android.animation.ValueAnimator;
import com.android.systemui.animation.ActivityLaunchAnimator;
/* compiled from: ActivityLaunchAnimator.kt */
/* loaded from: classes.dex */
final class ActivityLaunchAnimator$Runner$onAnimationCancelled$1 implements Runnable {
    final /* synthetic */ ActivityLaunchAnimator.Runner this$0;

    /* access modifiers changed from: package-private */
    public ActivityLaunchAnimator$Runner$onAnimationCancelled$1(ActivityLaunchAnimator.Runner runner) {
        this.this$0 = runner;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ValueAnimator valueAnimator = this.this$0.animator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.this$0.controller.onLaunchAnimationCancelled();
    }
}
