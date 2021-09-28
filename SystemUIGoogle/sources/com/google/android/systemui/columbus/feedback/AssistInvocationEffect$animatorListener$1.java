package com.google.android.systemui.columbus.feedback;

import android.animation.Animator;
/* compiled from: AssistInvocationEffect.kt */
/* loaded from: classes2.dex */
public final class AssistInvocationEffect$animatorListener$1 implements Animator.AnimatorListener {
    final /* synthetic */ AssistInvocationEffect this$0;

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
    }

    /* access modifiers changed from: package-private */
    public AssistInvocationEffect$animatorListener$1(AssistInvocationEffect assistInvocationEffect) {
        this.this$0 = assistInvocationEffect;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        AssistInvocationEffect.access$setProgress$p(this.this$0, 0.0f);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
        AssistInvocationEffect.access$setProgress$p(this.this$0, 0.0f);
    }
}
