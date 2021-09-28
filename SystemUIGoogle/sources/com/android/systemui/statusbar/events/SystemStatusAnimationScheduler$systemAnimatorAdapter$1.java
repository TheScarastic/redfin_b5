package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
/* compiled from: SystemStatusAnimationScheduler.kt */
/* loaded from: classes.dex */
public final class SystemStatusAnimationScheduler$systemAnimatorAdapter$1 extends AnimatorListenerAdapter {
    final /* synthetic */ SystemStatusAnimationScheduler this$0;

    /* access modifiers changed from: package-private */
    public SystemStatusAnimationScheduler$systemAnimatorAdapter$1(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        this.this$0 = systemStatusAnimationScheduler;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.this$0.notifySystemFinish();
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        this.this$0.notifySystemStart();
    }
}
