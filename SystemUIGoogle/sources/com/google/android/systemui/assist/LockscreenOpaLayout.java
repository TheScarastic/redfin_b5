package com.google.android.systemui.assist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class LockscreenOpaLayout extends FrameLayout implements FeedbackEffect {
    private View mBlue;
    private AnimatorSet mCannedAnimatorSet;
    private AnimatorSet mGestureAnimatorSet;
    private View mGreen;
    private AnimatorSet mLineAnimatorSet;
    private View mRed;
    private Resources mResources;
    private View mYellow;
    private final int RED_YELLOW_START_DELAY = 17;
    private final Interpolator INTERPOLATOR_5_100 = new PathInterpolator(1.0f, 0.0f, 0.95f, 1.0f);
    private int mGestureState = 0;
    private final ArraySet<Animator> mCurrentAnimators = new ArraySet<>();
    private final ArrayList<View> mAnimatedViews = new ArrayList<>();

    public LockscreenOpaLayout(Context context) {
        super(context);
    }

    public LockscreenOpaLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LockscreenOpaLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public LockscreenOpaLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mResources = getResources();
        this.mBlue = findViewById(R$id.blue);
        this.mRed = findViewById(R$id.red);
        this.mYellow = findViewById(R$id.yellow);
        this.mGreen = findViewById(R$id.green);
        this.mAnimatedViews.add(this.mBlue);
        this.mAnimatedViews.add(this.mRed);
        this.mAnimatedViews.add(this.mYellow);
        this.mAnimatedViews.add(this.mGreen);
    }

    private void startCannedAnimation() {
        if (isAttachedToWindow()) {
            skipToStartingValue();
            this.mGestureState = 3;
            AnimatorSet cannedAnimatorSet = getCannedAnimatorSet();
            this.mGestureAnimatorSet = cannedAnimatorSet;
            cannedAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.LockscreenOpaLayout.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    LockscreenOpaLayout.this.mGestureState = 1;
                    LockscreenOpaLayout lockscreenOpaLayout = LockscreenOpaLayout.this;
                    lockscreenOpaLayout.mGestureAnimatorSet = lockscreenOpaLayout.getLineAnimatorSet();
                    LockscreenOpaLayout.this.mGestureAnimatorSet.setCurrentPlayTime(0);
                }
            });
            this.mGestureAnimatorSet.start();
            return;
        }
        skipToStartingValue();
    }

    /* access modifiers changed from: private */
    public void startRetractAnimation() {
        if (isAttachedToWindow()) {
            AnimatorSet animatorSet = this.mGestureAnimatorSet;
            if (animatorSet != null) {
                animatorSet.removeAllListeners();
                this.mGestureAnimatorSet.cancel();
            }
            this.mCurrentAnimators.clear();
            this.mCurrentAnimators.addAll((ArraySet<? extends Animator>) getRetractAnimatorSet());
            startAll(this.mCurrentAnimators);
            this.mGestureState = 4;
            return;
        }
        skipToStartingValue();
    }

    /* access modifiers changed from: private */
    public void startCollapseAnimation() {
        if (isAttachedToWindow()) {
            this.mCurrentAnimators.clear();
            this.mCurrentAnimators.addAll((ArraySet<? extends Animator>) getCollapseAnimatorSet());
            startAll(this.mCurrentAnimators);
            this.mGestureState = 2;
            return;
        }
        skipToStartingValue();
    }

    private void startAll(ArraySet<Animator> arraySet) {
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            arraySet.valueAt(size).start();
        }
    }

    private ArraySet<Animator> getRetractAnimatorSet() {
        ArraySet<Animator> arraySet = new ArraySet<>();
        View view = this.mRed;
        Interpolator interpolator = OpaUtils.INTERPOLATOR_40_OUT;
        arraySet.add(OpaUtils.getTranslationAnimatorX(view, interpolator, 190));
        arraySet.add(OpaUtils.getTranslationAnimatorX(this.mBlue, interpolator, 190));
        arraySet.add(OpaUtils.getTranslationAnimatorX(this.mGreen, interpolator, 190));
        arraySet.add(OpaUtils.getTranslationAnimatorX(this.mYellow, interpolator, 190));
        OpaUtils.getLongestAnim(arraySet).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.LockscreenOpaLayout.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                LockscreenOpaLayout.this.mCurrentAnimators.clear();
                LockscreenOpaLayout.this.skipToStartingValue();
                LockscreenOpaLayout.this.mGestureState = 0;
                LockscreenOpaLayout.this.mGestureAnimatorSet = null;
            }
        });
        return arraySet;
    }

    private ArraySet<Animator> getCollapseAnimatorSet() {
        ArraySet<Animator> arraySet = new ArraySet<>();
        View view = this.mRed;
        Interpolator interpolator = OpaUtils.INTERPOLATOR_40_OUT;
        arraySet.add(OpaUtils.getTranslationAnimatorX(view, interpolator, 133));
        arraySet.add(OpaUtils.getTranslationAnimatorX(this.mBlue, interpolator, 150));
        arraySet.add(OpaUtils.getTranslationAnimatorX(this.mYellow, interpolator, 133));
        arraySet.add(OpaUtils.getTranslationAnimatorX(this.mGreen, interpolator, 150));
        OpaUtils.getLongestAnim(arraySet).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.LockscreenOpaLayout.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                LockscreenOpaLayout.this.mCurrentAnimators.clear();
                LockscreenOpaLayout.this.mGestureAnimatorSet = null;
                LockscreenOpaLayout.this.mGestureState = 0;
                LockscreenOpaLayout.this.skipToStartingValue();
            }
        });
        return arraySet;
    }

    /* access modifiers changed from: private */
    public void skipToStartingValue() {
        int size = this.mAnimatedViews.size();
        for (int i = 0; i < size; i++) {
            View view = this.mAnimatedViews.get(i);
            view.setAlpha(0.0f);
            view.setTranslationX(0.0f);
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
        int i = this.mGestureState;
        if (i != 2 && i != 4) {
            if (i == 3) {
                if (this.mGestureAnimatorSet.isRunning()) {
                    this.mGestureAnimatorSet.removeAllListeners();
                    this.mGestureAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.LockscreenOpaLayout.4
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            LockscreenOpaLayout.this.startRetractAnimation();
                        }
                    });
                    return;
                }
                this.mGestureState = 4;
                startRetractAnimation();
            } else if (i == 1) {
                startRetractAnimation();
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        int i2 = this.mGestureState;
        if (i2 != 2) {
            if (i2 == 4) {
                endCurrentAnimation();
            }
            if (f == 0.0f) {
                this.mGestureState = 0;
                return;
            }
            long max = Math.max(0L, ((long) (f * 533.0f)) - 167);
            int i3 = this.mGestureState;
            if (i3 == 0) {
                startCannedAnimation();
            } else if (i3 == 1) {
                this.mGestureAnimatorSet.setCurrentPlayTime(max);
            } else if (i3 == 3 && max >= 167) {
                this.mGestureAnimatorSet.end();
                if (this.mGestureState == 1) {
                    this.mGestureAnimatorSet.setCurrentPlayTime(max);
                }
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        int i = this.mGestureState;
        if (i != 4 && i != 2) {
            if (i == 3) {
                this.mGestureState = 2;
                this.mGestureAnimatorSet.removeAllListeners();
                this.mGestureAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.LockscreenOpaLayout.5
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        LockscreenOpaLayout lockscreenOpaLayout = LockscreenOpaLayout.this;
                        lockscreenOpaLayout.mGestureAnimatorSet = lockscreenOpaLayout.getLineAnimatorSet();
                        LockscreenOpaLayout.this.mGestureAnimatorSet.removeAllListeners();
                        LockscreenOpaLayout.this.mGestureAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.LockscreenOpaLayout.5.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator2) {
                                LockscreenOpaLayout.this.startCollapseAnimation();
                            }
                        });
                        LockscreenOpaLayout.this.mGestureAnimatorSet.end();
                    }
                });
                return;
            }
            AnimatorSet animatorSet = this.mGestureAnimatorSet;
            if (animatorSet != null) {
                this.mGestureState = 2;
                animatorSet.removeAllListeners();
                this.mGestureAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.LockscreenOpaLayout.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        LockscreenOpaLayout.this.startCollapseAnimation();
                    }
                });
                if (!this.mGestureAnimatorSet.isStarted()) {
                    this.mGestureAnimatorSet.start();
                }
            }
        }
    }

    private AnimatorSet getCannedAnimatorSet() {
        AnimatorSet animatorSet = this.mCannedAnimatorSet;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            this.mCannedAnimatorSet.cancel();
            return this.mCannedAnimatorSet;
        }
        this.mCannedAnimatorSet = new AnimatorSet();
        View view = this.mRed;
        Interpolator interpolator = OpaUtils.INTERPOLATOR_40_40;
        Resources resources = this.mResources;
        int i = R$dimen.opa_lockscreen_canned_ry;
        ObjectAnimator translationObjectAnimatorX = OpaUtils.getTranslationObjectAnimatorX(view, interpolator, -OpaUtils.getPxVal(resources, i), this.mRed.getX(), 83);
        translationObjectAnimatorX.setStartDelay(17);
        ObjectAnimator translationObjectAnimatorX2 = OpaUtils.getTranslationObjectAnimatorX(this.mYellow, interpolator, OpaUtils.getPxVal(this.mResources, i), this.mYellow.getX(), 83);
        translationObjectAnimatorX2.setStartDelay(17);
        AnimatorSet.Builder with = this.mCannedAnimatorSet.play(translationObjectAnimatorX).with(translationObjectAnimatorX2);
        View view2 = this.mBlue;
        Resources resources2 = this.mResources;
        int i2 = R$dimen.opa_lockscreen_canned_bg;
        AnimatorSet.Builder with2 = with.with(OpaUtils.getTranslationObjectAnimatorX(view2, interpolator, -OpaUtils.getPxVal(resources2, i2), this.mBlue.getX(), 167)).with(OpaUtils.getTranslationObjectAnimatorX(this.mGreen, interpolator, OpaUtils.getPxVal(this.mResources, i2), this.mGreen.getX(), 167));
        View view3 = this.mRed;
        Interpolator interpolator2 = Interpolators.LINEAR;
        with2.with(OpaUtils.getAlphaObjectAnimator(view3, 1.0f, 50, 130, interpolator2)).with(OpaUtils.getAlphaObjectAnimator(this.mYellow, 1.0f, 50, 130, interpolator2)).with(OpaUtils.getAlphaObjectAnimator(this.mBlue, 1.0f, 50, 113, interpolator2)).with(OpaUtils.getAlphaObjectAnimator(this.mGreen, 1.0f, 50, 113, interpolator2));
        return this.mCannedAnimatorSet;
    }

    /* access modifiers changed from: private */
    public AnimatorSet getLineAnimatorSet() {
        AnimatorSet animatorSet = this.mLineAnimatorSet;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            this.mLineAnimatorSet.cancel();
            return this.mLineAnimatorSet;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mLineAnimatorSet = animatorSet2;
        View view = this.mRed;
        Interpolator interpolator = this.INTERPOLATOR_5_100;
        Resources resources = this.mResources;
        int i = R$dimen.opa_lockscreen_translation_ry;
        AnimatorSet.Builder with = animatorSet2.play(OpaUtils.getTranslationObjectAnimatorX(view, interpolator, -OpaUtils.getPxVal(resources, i), this.mRed.getX(), 366)).with(OpaUtils.getTranslationObjectAnimatorX(this.mYellow, this.INTERPOLATOR_5_100, OpaUtils.getPxVal(this.mResources, i), this.mYellow.getX(), 366));
        View view2 = this.mGreen;
        Interpolator interpolator2 = this.INTERPOLATOR_5_100;
        Resources resources2 = this.mResources;
        int i2 = R$dimen.opa_lockscreen_translation_bg;
        with.with(OpaUtils.getTranslationObjectAnimatorX(view2, interpolator2, OpaUtils.getPxVal(resources2, i2), this.mGreen.getX(), 366)).with(OpaUtils.getTranslationObjectAnimatorX(this.mBlue, this.INTERPOLATOR_5_100, -OpaUtils.getPxVal(this.mResources, i2), this.mBlue.getX(), 366));
        return this.mLineAnimatorSet;
    }

    private void endCurrentAnimation() {
        if (!this.mCurrentAnimators.isEmpty()) {
            for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
                Animator valueAt = this.mCurrentAnimators.valueAt(size);
                valueAt.removeAllListeners();
                valueAt.end();
            }
            this.mCurrentAnimators.clear();
        }
        this.mGestureState = 0;
    }
}
