package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Property;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.google.android.material.color.MaterialColors;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class CircularIndeterminateAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {
    private float animationFraction;
    private ObjectAnimator animator;
    private final BaseProgressIndicatorSpec baseSpec;
    private ObjectAnimator completeEndAnimator;
    private float completeEndFraction;
    private static final int[] DELAY_TO_EXPAND_IN_MS = {0, 1350, 2700, 4050};
    private static final int[] DELAY_TO_COLLAPSE_IN_MS = {667, 2017, 3367, 4717};
    private static final int[] DELAY_TO_FADE_IN_MS = {1000, 2350, 3700, 5050};
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> ANIMATION_FRACTION = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "animationFraction") { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.3
        public Float get(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate) {
            return Float.valueOf(circularIndeterminateAnimatorDelegate.getAnimationFraction());
        }

        public void set(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate, Float f) {
            circularIndeterminateAnimatorDelegate.setAnimationFraction(f.floatValue());
        }
    };
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> COMPLETE_END_FRACTION = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "completeEndFraction") { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.4
        public Float get(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate) {
            return Float.valueOf(circularIndeterminateAnimatorDelegate.getCompleteEndFraction());
        }

        public void set(CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate, Float f) {
            circularIndeterminateAnimatorDelegate.setCompleteEndFraction(f.floatValue());
        }
    };
    private int indicatorColorIndexOffset = 0;
    Animatable2Compat.AnimationCallback animatorCompleteCallback = null;
    private final FastOutSlowInInterpolator interpolator = new FastOutSlowInInterpolator();

    public CircularIndeterminateAnimatorDelegate(CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        super(1);
        this.baseSpec = circularProgressIndicatorSpec;
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void startAnimator() {
        maybeInitializeAnimators();
        resetPropertiesForNewStart();
        this.animator.start();
    }

    private void maybeInitializeAnimators() {
        if (this.animator == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, ANIMATION_FRACTION, 0.0f, 1.0f);
            this.animator = ofFloat;
            ofFloat.setDuration(5400L);
            this.animator.setInterpolator(null);
            this.animator.setRepeatCount(-1);
            this.animator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                    super.onAnimationRepeat(animator);
                    CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate = CircularIndeterminateAnimatorDelegate.this;
                    circularIndeterminateAnimatorDelegate.indicatorColorIndexOffset = (circularIndeterminateAnimatorDelegate.indicatorColorIndexOffset + 4) % CircularIndeterminateAnimatorDelegate.this.baseSpec.indicatorColors.length;
                }
            });
        }
        if (this.completeEndAnimator == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, COMPLETE_END_FRACTION, 0.0f, 1.0f);
            this.completeEndAnimator = ofFloat2;
            ofFloat2.setDuration(333L);
            this.completeEndAnimator.setInterpolator(this.interpolator);
            this.completeEndAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.progressindicator.CircularIndeterminateAnimatorDelegate.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    CircularIndeterminateAnimatorDelegate.this.cancelAnimatorImmediately();
                    CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate = CircularIndeterminateAnimatorDelegate.this;
                    circularIndeterminateAnimatorDelegate.animatorCompleteCallback.onAnimationEnd(circularIndeterminateAnimatorDelegate.drawable);
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void cancelAnimatorImmediately() {
        ObjectAnimator objectAnimator = this.animator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void requestCancelAnimatorAfterCurrentCycle() {
        if (!this.completeEndAnimator.isRunning()) {
            if (this.drawable.isVisible()) {
                this.completeEndAnimator.start();
            } else {
                cancelAnimatorImmediately();
            }
        }
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void registerAnimatorsCompleteCallback(Animatable2Compat.AnimationCallback animationCallback) {
        this.animatorCompleteCallback = animationCallback;
    }

    @Override // com.google.android.material.progressindicator.IndeterminateAnimatorDelegate
    public void unregisterAnimatorsCompleteCallback() {
        this.animatorCompleteCallback = null;
    }

    private void updateSegmentPositions(int i) {
        float[] fArr = this.segmentPositions;
        float f = this.animationFraction;
        fArr[0] = (f * 1520.0f) - 0.21875f;
        fArr[1] = f * 1520.0f;
        for (int i2 = 0; i2 < 4; i2++) {
            float fractionInRange = getFractionInRange(i, DELAY_TO_EXPAND_IN_MS[i2], 667);
            float[] fArr2 = this.segmentPositions;
            fArr2[1] = fArr2[1] + (this.interpolator.getInterpolation(fractionInRange) * 250.0f);
            float fractionInRange2 = getFractionInRange(i, DELAY_TO_COLLAPSE_IN_MS[i2], 667);
            float[] fArr3 = this.segmentPositions;
            fArr3[0] = fArr3[0] + (this.interpolator.getInterpolation(fractionInRange2) * 250.0f);
        }
        float[] fArr4 = this.segmentPositions;
        fArr4[0] = fArr4[0] + ((fArr4[1] - fArr4[0]) * this.completeEndFraction);
        fArr4[0] = fArr4[0] / 360.0f;
        fArr4[1] = fArr4[1] / 360.0f;
    }

    private void maybeUpdateSegmentColors(int i) {
        for (int i2 = 0; i2 < 4; i2++) {
            float fractionInRange = getFractionInRange(i, DELAY_TO_FADE_IN_MS[i2], 333);
            if (fractionInRange >= 0.0f && fractionInRange <= 1.0f) {
                int i3 = i2 + this.indicatorColorIndexOffset;
                int[] iArr = this.baseSpec.indicatorColors;
                int length = i3 % iArr.length;
                this.segmentColors[0] = ArgbEvaluatorCompat.getInstance().evaluate(this.interpolator.getInterpolation(fractionInRange), Integer.valueOf(MaterialColors.compositeARGBWithAlpha(iArr[length], this.drawable.getAlpha())), Integer.valueOf(MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[(length + 1) % iArr.length], this.drawable.getAlpha()))).intValue();
                return;
            }
        }
    }

    void resetPropertiesForNewStart() {
        this.indicatorColorIndexOffset = 0;
        this.segmentColors[0] = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[0], this.drawable.getAlpha());
        this.completeEndFraction = 0.0f;
    }

    /* access modifiers changed from: private */
    public float getAnimationFraction() {
        return this.animationFraction;
    }

    void setAnimationFraction(float f) {
        this.animationFraction = f;
        int i = (int) (f * 5400.0f);
        updateSegmentPositions(i);
        maybeUpdateSegmentColors(i);
        this.drawable.invalidateSelf();
    }

    /* access modifiers changed from: private */
    public float getCompleteEndFraction() {
        return this.completeEndFraction;
    }

    /* access modifiers changed from: private */
    public void setCompleteEndFraction(float f) {
        this.completeEndFraction = f;
    }
}
