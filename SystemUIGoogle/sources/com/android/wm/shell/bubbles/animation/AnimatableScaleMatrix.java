package com.android.wm.shell.bubbles.animation;

import android.graphics.Matrix;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
/* loaded from: classes2.dex */
public class AnimatableScaleMatrix extends Matrix {
    public static final FloatPropertyCompat<AnimatableScaleMatrix> SCALE_X = new FloatPropertyCompat<AnimatableScaleMatrix>("matrixScaleX") { // from class: com.android.wm.shell.bubbles.animation.AnimatableScaleMatrix.1
        public float getValue(AnimatableScaleMatrix animatableScaleMatrix) {
            return AnimatableScaleMatrix.getAnimatableValueForScaleFactor(animatableScaleMatrix.mScaleX);
        }

        public void setValue(AnimatableScaleMatrix animatableScaleMatrix, float f) {
            animatableScaleMatrix.setScaleX(f * 0.002f);
        }
    };
    public static final FloatPropertyCompat<AnimatableScaleMatrix> SCALE_Y = new FloatPropertyCompat<AnimatableScaleMatrix>("matrixScaleY") { // from class: com.android.wm.shell.bubbles.animation.AnimatableScaleMatrix.2
        public float getValue(AnimatableScaleMatrix animatableScaleMatrix) {
            return AnimatableScaleMatrix.getAnimatableValueForScaleFactor(animatableScaleMatrix.mScaleY);
        }

        public void setValue(AnimatableScaleMatrix animatableScaleMatrix, float f) {
            animatableScaleMatrix.setScaleY(f * 0.002f);
        }
    };
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;
    private float mPivotX = 0.0f;
    private float mPivotY = 0.0f;

    public static float getAnimatableValueForScaleFactor(float f) {
        return f * 499.99997f;
    }

    @Override // android.graphics.Matrix, java.lang.Object
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override // android.graphics.Matrix
    public void setScale(float f, float f2, float f3, float f4) {
        this.mScaleX = f;
        this.mScaleY = f2;
        this.mPivotX = f3;
        this.mPivotY = f4;
        super.setScale(f, f2, f3, f4);
    }

    public void setScaleX(float f) {
        this.mScaleX = f;
        super.setScale(f, this.mScaleY, this.mPivotX, this.mPivotY);
    }

    public void setScaleY(float f) {
        this.mScaleY = f;
        super.setScale(this.mScaleX, f, this.mPivotX, this.mPivotY);
    }
}
