package com.android.systemui.shared.system;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.SurfaceControl;
/* loaded from: classes.dex */
public class TransactionCompat {
    public final float[] mTmpValues = new float[9];
    public final SurfaceControl.Transaction mTransaction = new SurfaceControl.Transaction();

    public static void setRelativeLayer(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, SurfaceControl surfaceControl2, int i) {
        transaction.setRelativeLayer(surfaceControl, surfaceControl2, i);
    }

    public void apply() {
        this.mTransaction.apply();
    }

    public TransactionCompat hide(SurfaceControlCompat surfaceControlCompat) {
        this.mTransaction.hide(surfaceControlCompat.mSurfaceControl);
        return this;
    }

    public TransactionCompat setAlpha(SurfaceControlCompat surfaceControlCompat, float f) {
        this.mTransaction.setAlpha(surfaceControlCompat.mSurfaceControl, f);
        return this;
    }

    public TransactionCompat setBackgroundBlurRadius(SurfaceControlCompat surfaceControlCompat, int i) {
        this.mTransaction.setBackgroundBlurRadius(surfaceControlCompat.mSurfaceControl, i);
        return this;
    }

    public TransactionCompat setColor(SurfaceControlCompat surfaceControlCompat, float[] fArr) {
        this.mTransaction.setColor(surfaceControlCompat.mSurfaceControl, fArr);
        return this;
    }

    public TransactionCompat setCornerRadius(SurfaceControlCompat surfaceControlCompat, float f) {
        this.mTransaction.setCornerRadius(surfaceControlCompat.mSurfaceControl, f);
        return this;
    }

    public TransactionCompat setLayer(SurfaceControlCompat surfaceControlCompat, int i) {
        this.mTransaction.setLayer(surfaceControlCompat.mSurfaceControl, i);
        return this;
    }

    public TransactionCompat setMatrix(SurfaceControlCompat surfaceControlCompat, float f, float f2, float f3, float f4) {
        this.mTransaction.setMatrix(surfaceControlCompat.mSurfaceControl, f, f2, f3, f4);
        return this;
    }

    public TransactionCompat setOpaque(SurfaceControlCompat surfaceControlCompat, boolean z) {
        this.mTransaction.setOpaque(surfaceControlCompat.mSurfaceControl, z);
        return this;
    }

    public TransactionCompat setPosition(SurfaceControlCompat surfaceControlCompat, float f, float f2) {
        this.mTransaction.setPosition(surfaceControlCompat.mSurfaceControl, f, f2);
        return this;
    }

    public TransactionCompat setSize(SurfaceControlCompat surfaceControlCompat, int i, int i2) {
        this.mTransaction.setBufferSize(surfaceControlCompat.mSurfaceControl, i, i2);
        return this;
    }

    public TransactionCompat setWindowCrop(SurfaceControlCompat surfaceControlCompat, Rect rect) {
        this.mTransaction.setWindowCrop(surfaceControlCompat.mSurfaceControl, rect);
        return this;
    }

    public TransactionCompat show(SurfaceControlCompat surfaceControlCompat) {
        this.mTransaction.show(surfaceControlCompat.mSurfaceControl);
        return this;
    }

    public TransactionCompat setMatrix(SurfaceControlCompat surfaceControlCompat, Matrix matrix) {
        this.mTransaction.setMatrix(surfaceControlCompat.mSurfaceControl, matrix, this.mTmpValues);
        return this;
    }
}
