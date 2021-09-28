package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class AnimatorTracker {
    private Animator currentAnimator;

    public void onNextAnimationStart(Animator animator) {
        cancelCurrent();
        this.currentAnimator = animator;
    }

    public void cancelCurrent() {
        Animator animator = this.currentAnimator;
        if (animator != null) {
            animator.cancel();
        }
    }

    public void clear() {
        this.currentAnimator = null;
    }
}
