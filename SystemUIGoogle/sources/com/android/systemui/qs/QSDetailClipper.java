package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.ViewAnimationUtils;
/* loaded from: classes.dex */
public class QSDetailClipper {
    private Animator mAnimator;
    private final TransitionDrawable mBackground;
    private final View mDetail;
    private final Runnable mReverseBackground = new Runnable() { // from class: com.android.systemui.qs.QSDetailClipper.1
        @Override // java.lang.Runnable
        public void run() {
            if (QSDetailClipper.this.mAnimator != null) {
                QSDetailClipper.this.mBackground.reverseTransition((int) (((double) QSDetailClipper.this.mAnimator.getDuration()) * 0.35d));
            }
        }
    };
    private final AnimatorListenerAdapter mVisibleOnStart = new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.QSDetailClipper.2
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            QSDetailClipper.this.mDetail.setVisibility(0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            QSDetailClipper.this.mAnimator = null;
        }
    };
    private final AnimatorListenerAdapter mGoneOnEnd = new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.QSDetailClipper.3
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            QSDetailClipper.this.mDetail.setVisibility(8);
            QSDetailClipper.this.mBackground.resetTransition();
            QSDetailClipper.this.mAnimator = null;
        }
    };

    public QSDetailClipper(View view) {
        this.mDetail = view;
        this.mBackground = (TransitionDrawable) view.getBackground();
    }

    public void animateCircularClip(int i, int i2, boolean z, Animator.AnimatorListener animatorListener) {
        updateCircularClip(true, i, i2, z, animatorListener);
    }

    public void updateCircularClip(boolean z, int i, int i2, boolean z2, Animator.AnimatorListener animatorListener) {
        int i3;
        Animator animator = this.mAnimator;
        if (animator != null) {
            animator.cancel();
        }
        int width = this.mDetail.getWidth() - i;
        int height = this.mDetail.getHeight() - i2;
        int i4 = 0;
        if (i < 0 || width < 0 || i2 < 0 || height < 0) {
            i3 = Math.min(Math.min(Math.min(Math.abs(i), Math.abs(i2)), Math.abs(width)), Math.abs(height));
        } else {
            i3 = 0;
        }
        int i5 = i * i;
        int i6 = i2 * i2;
        int i7 = width * width;
        int i8 = height * height;
        int max = (int) Math.max((double) ((int) Math.max((double) ((int) Math.max((double) ((int) Math.ceil(Math.sqrt((double) (i5 + i6)))), Math.ceil(Math.sqrt((double) (i6 + i7))))), Math.ceil(Math.sqrt((double) (i7 + i8))))), Math.ceil(Math.sqrt((double) (i5 + i8))));
        if (z2) {
            this.mAnimator = ViewAnimationUtils.createCircularReveal(this.mDetail, i, i2, (float) i3, (float) max);
        } else {
            this.mAnimator = ViewAnimationUtils.createCircularReveal(this.mDetail, i, i2, (float) max, (float) i3);
        }
        Animator animator2 = this.mAnimator;
        long j = 0;
        animator2.setDuration(z ? (long) (((double) animator2.getDuration()) * 1.5d) : 0);
        if (animatorListener != null) {
            this.mAnimator.addListener(animatorListener);
        }
        if (z2) {
            TransitionDrawable transitionDrawable = this.mBackground;
            if (z) {
                i4 = (int) (((double) this.mAnimator.getDuration()) * 0.6d);
            }
            transitionDrawable.startTransition(i4);
            this.mAnimator.addListener(this.mVisibleOnStart);
        } else {
            View view = this.mDetail;
            Runnable runnable = this.mReverseBackground;
            if (z) {
                j = (long) (((double) this.mAnimator.getDuration()) * 0.65d);
            }
            view.postDelayed(runnable, j);
            this.mAnimator.addListener(this.mGoneOnEnd);
        }
        this.mAnimator.start();
    }

    public void showBackground() {
        this.mBackground.showSecondLayer();
    }

    public void cancelAnimator() {
        Animator animator = this.mAnimator;
        if (animator != null) {
            animator.cancel();
        }
    }
}
