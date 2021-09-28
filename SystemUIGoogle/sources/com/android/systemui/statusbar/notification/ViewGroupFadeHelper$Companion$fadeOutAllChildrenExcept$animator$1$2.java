package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
/* compiled from: ViewGroupFadeHelper.kt */
/* loaded from: classes.dex */
public final class ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2 extends AnimatorListenerAdapter {
    final /* synthetic */ Runnable $endRunnable;

    /* access modifiers changed from: package-private */
    public ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2(Runnable runnable) {
        this.$endRunnable = runnable;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        Runnable runnable = this.$endRunnable;
        if (runnable != null) {
            runnable.run();
        }
    }
}
