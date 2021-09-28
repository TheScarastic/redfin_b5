package com.android.systemui.animation;

import com.android.systemui.animation.ActivityLaunchAnimator;
/* compiled from: ActivityLaunchAnimator.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ActivityLaunchAnimator$Runner$onTimeout$1 implements Runnable {
    final /* synthetic */ ActivityLaunchAnimator.Runner this$0;

    /* access modifiers changed from: package-private */
    public ActivityLaunchAnimator$Runner$onTimeout$1(ActivityLaunchAnimator.Runner runner) {
        this.this$0 = runner;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.onAnimationTimedOut();
    }
}
