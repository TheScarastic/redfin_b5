package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
/* loaded from: classes.dex */
public class ItemTouchHelper$RecoverAnimation implements Animator.AnimatorListener {
    public boolean mEnded;
    public final ValueAnimator mValueAnimator;

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        if (this.mEnded) {
            this.mEnded = true;
            return;
        }
        throw null;
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
    }
}
