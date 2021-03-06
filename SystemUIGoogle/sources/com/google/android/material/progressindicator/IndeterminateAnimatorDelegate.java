package com.google.android.material.progressindicator;

import android.animation.Animator;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class IndeterminateAnimatorDelegate<T extends Animator> {
    protected IndeterminateDrawable drawable;
    protected final int[] segmentColors;
    protected final float[] segmentPositions;

    /* access modifiers changed from: package-private */
    public abstract void cancelAnimatorImmediately();

    /* access modifiers changed from: protected */
    public float getFractionInRange(int i, int i2, int i3) {
        return ((float) (i - i2)) / ((float) i3);
    }

    public abstract void registerAnimatorsCompleteCallback(Animatable2Compat.AnimationCallback animationCallback);

    /* access modifiers changed from: package-private */
    public abstract void requestCancelAnimatorAfterCurrentCycle();

    /* access modifiers changed from: package-private */
    public abstract void startAnimator();

    public abstract void unregisterAnimatorsCompleteCallback();

    /* access modifiers changed from: protected */
    public IndeterminateAnimatorDelegate(int i) {
        this.segmentPositions = new float[i * 2];
        this.segmentColors = new int[i];
    }

    /* access modifiers changed from: protected */
    public void registerDrawable(IndeterminateDrawable indeterminateDrawable) {
        this.drawable = indeterminateDrawable;
    }
}
